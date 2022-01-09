package com.invicto.nddb.process;

import com.invicto.nddb.entity.Contract;
import com.invicto.nddb.entity.ContractData;
import com.invicto.nddb.entity.ContractDataAnalytics;
import com.invicto.nddb.entity.RunBook;
import com.invicto.nddb.model.EquityDerivativeCsvRecord;
import com.invicto.nddb.repository.ContractDataAnalyticsRepo;
import com.invicto.nddb.repository.ContractDataRepo;
import com.invicto.nddb.repository.ContractRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class BhavCopyRecordProcessor {


    private ContractRepo contractRepo;
    private ContractDataRepo contractDataRepo;
    private ContractDataAnalyticsRepo contractDataAnalyticsRepo;

    @Autowired
    public BhavCopyRecordProcessor(ContractRepo contractRepo, ContractDataRepo contractDataRepo, ContractDataAnalyticsRepo contractDataAnalytics) {
        this.contractRepo = contractRepo;
        this.contractDataRepo = contractDataRepo;
        this.contractDataAnalyticsRepo = contractDataAnalytics;
    }

    public void process(EquityDerivativeCsvRecord record, RunBook runBook) {
        Optional<Contract> optionalContract = contractRepo.findContractByInstrumentAndExpiryDateAndSymbol(record.getInstrument(), record.getExpiryDt(), record.getSymbol());
        Contract contract = null;
        if (optionalContract.isPresent()) {
            Optional<ContractData> latestDataOptional = contractDataRepo.findTop1ByContractOrderByCollectionDateDesc(optionalContract.get());
            contract = optionalContract.get();
            log.info("Processing data for existing contract {}{}", contract.getSymbol(), contract.getExpiryDate());
            ContractData contractData = new ContractData();
            contractData.setClose(record.getClose());
            contractData.setOpen(record.getOpen());
            contractData.setHigh(record.getHigh());
            contractData.setLow(record.getLow());
            contractData.setCollectionDate(record.getTimestamp());
            contractData.setOpenInterest(record.getOi());
            contractData.setVolume(record.getContracts());
            contractData.setContract(contract);
            contractData.setRunBook(runBook);
            contractDataRepo.save(contractData);
            if (latestDataOptional.isPresent()) {
                ContractData latestContractData = latestDataOptional.get();
                log.info("latest contract data is dated {}", latestContractData.getCollectionDate());
                ContractDataAnalytics contractDataAnalytics = new ContractDataAnalytics();
                contractDataAnalytics.setDeltaCloseP(getDeltaPercentage(contractData.getClose(), latestContractData.getClose()));
                contractDataAnalytics.setDeltaVolumeP(getDeltaPercentage(contractData.getVolume(), latestContractData.getVolume()));
                contractDataAnalytics.setDeltaOiP(getDeltaPercentage(contractData.getOpenInterest(), latestContractData.getOpenInterest()));
                if (contractDataAnalytics.getDeltaVolumeP() > 0.0 && contractDataAnalytics.getDeltaOiP() > 0.0 && contractDataAnalytics.getDeltaCloseP() > 0.0 && contractData.getHigh() > latestContractData.getHigh())
                    contractDataAnalytics.setSignal("LONG_BUILD_UP");
                if (contractDataAnalytics.getDeltaVolumeP() > 1.0 && contractDataAnalytics.getDeltaOiP() > 0.0 && contractDataAnalytics.getDeltaCloseP() < 0.0 && contractData.getLow() < latestContractData.getLow())
                    contractDataAnalytics.setSignal("SHORT_BUILD_UP");
                Optional<ContractDataAnalytics> optionalContractDataAnalytics = contractDataAnalyticsRepo.findTop1ByContractOrderByAnalyticsDateDesc(contract);
                if (optionalContractDataAnalytics.isPresent()) {
                    ContractDataAnalytics latestAnalytics = optionalContractDataAnalytics.get();
                    if (contractData.getHigh() > latestContractData.getHigh())
                        contractDataAnalytics.setHigherHighCount(latestAnalytics.getHigherHighCount() + 1);
                    if (contractData.getLow() < latestContractData.getLow())
                        contractDataAnalytics.setLowerLowCount(latestAnalytics.getLowerLowCount() + 1);
                    if (contractData.getHigh() < latestContractData.getHigh())
                        contractDataAnalytics.setLowerHighCount(latestAnalytics.getLowerHighCount() + 1);
                    if (contractData.getLow() > latestContractData.getLow())
                        contractDataAnalytics.setHigherLowCount(latestAnalytics.getHigherLowCount() + 1);
                    if (contractData.getClose() > latestContractData.getClose() && contractData.getHigh() > latestContractData.getHigh() && contractData.getVolume() > latestContractData.getVolume() && contractData.getLow() > latestContractData.getLow())
                        contractDataAnalytics.setBuyersWonCount(latestAnalytics.getBuyersWonCount() + 1);
                    else
                        contractDataAnalytics.setBuyersWonCount(latestAnalytics.getBuyersWonCount());
                    if (contractData.getClose() < latestContractData.getClose() && contractData.getLow() < latestContractData.getLow() && contractData.getVolume() > latestContractData.getVolume() && contractData.getHigh() < latestContractData.getHigh())
                        contractDataAnalytics.setSellersWonCount(latestAnalytics.getSellersWonCount() + 1);
                    else
                        contractDataAnalytics.setSellersWonCount(latestAnalytics.getSellersWonCount());
                }
                contractDataAnalytics.setAnalyticsDate(contractData.getCollectionDate());
                contractDataAnalytics.setContract(contract);
                contractDataAnalyticsRepo.save(contractDataAnalytics);
            }
        } else {
            contract = new Contract();
            contract.setInstrument(record.getInstrument());
            contract.setExpiryDate(record.getExpiryDt());
            contract.setSymbol(record.getSymbol());
            contractRepo.save(contract);
            ContractData contractData = new ContractData();
            contractData.setClose(record.getClose());
            contractData.setOpen(record.getOpen());
            contractData.setHigh(record.getHigh());
            contractData.setLow(record.getLow());
            contractData.setCollectionDate(record.getTimestamp());
            contractData.setOpenInterest(record.getOi());
            contractData.setVolume(record.getContracts());
            contractData.setRunBook(runBook);
            contractData.setContract(contract);
            contractDataRepo.save(contractData);
        }
    }

    private double getDeltaPercentage(double num, double deno) {
        return 100 * ((num - deno) / deno);
    }
}

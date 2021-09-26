package com.invicto.nddb.process;

import com.invicto.nddb.entity.Contract;
import com.invicto.nddb.entity.ContractData;
import com.invicto.nddb.entity.ContractDataAnalytics;
import com.invicto.nddb.entity.RunBook;
import com.invicto.nddb.model.EquityDerivativeCsvRecord;
import com.invicto.nddb.repository.ContractDataAnalyticsRepo;
import com.invicto.nddb.repository.ContractDataRepo;
import com.invicto.nddb.repository.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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

    public void process(EquityDerivativeCsvRecord record, RunBook runBook){
        Optional<Contract> optionalContract = contractRepo.findContractByInstrumentAndExpiryDateAndSymbol(record.getInstrument(),record.getExpiryDt(),record.getSymbol());
        Contract contract = null;
        if(optionalContract.isPresent()){
            Optional<ContractData> latestDataOptional  = contractDataRepo.findTop1ByContractOrderByCollectionDateDesc(optionalContract.get());
            contract = optionalContract.get();
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

            if(latestDataOptional.isPresent()){
                ContractDataAnalytics contractDataAnalytics = new ContractDataAnalytics();
                contractDataAnalytics.setDeltaCloseP(getDeltaPercentage(contractData.getClose(),latestDataOptional.get().getClose()));
                contractDataAnalytics.setDeltaVolumeP(getDeltaPercentage(contractData.getVolume(),latestDataOptional.get().getVolume()));
                contractDataAnalytics.setDeltaOiP(getDeltaPercentage(contractData.getOpenInterest(),latestDataOptional.get().getOpenInterest()));
                if(contractDataAnalytics.getDeltaVolumeP() > 0.0 && contractDataAnalytics.getDeltaOiP() > 0.0 && contractDataAnalytics.getDeltaCloseP() > 0.0)
                    contractDataAnalytics.setSignal("LONG_BUILD_UP");
                if(contractDataAnalytics.getDeltaVolumeP() > 0.0 && contractDataAnalytics.getDeltaOiP() > 0.0 && contractDataAnalytics.getDeltaCloseP() < 0.0)
                    contractDataAnalytics.setSignal("SHORT_BUILD_UP");
                contractDataAnalytics.setAnalyticsDate(contractData.getCollectionDate());
                contractDataAnalytics.setContract(contract);
                contractDataAnalyticsRepo.save(contractDataAnalytics);

            }
        }
        else
        {
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

    private double getDeltaPercentage(double num,double deno){
        return 100 * ((num -deno)/deno);
    }
}

package com.invicto.nddb.process;

import com.invicto.nddb.entity.Contract;
import com.invicto.nddb.entity.ContractData;
import com.invicto.nddb.entity.RunBook;
import com.invicto.nddb.model.EquityDerivativeCsvRecord;
import com.invicto.nddb.repository.ContractDataRepo;
import com.invicto.nddb.repository.ContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BhavCopyRecordProcessor {


    private ContractRepo contractRepo;
    private ContractDataRepo contractDataRepo;

    @Autowired
    public BhavCopyRecordProcessor(ContractRepo contractRepo, ContractDataRepo contractDataRepo) {
        this.contractRepo = contractRepo;
        this.contractDataRepo = contractDataRepo;
    }

    public void process(EquityDerivativeCsvRecord record, RunBook runBook){
        Optional<Contract> optionalContract = contractRepo.findContractByInstrumentAndExpiryDateAndSymbol(record.getInstrument(),record.getExpiryDt(),record.getSymbol());
        Contract contract = null;
        if(optionalContract.isPresent()){
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
}

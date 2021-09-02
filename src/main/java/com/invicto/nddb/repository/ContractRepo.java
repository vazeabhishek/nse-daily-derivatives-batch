package com.invicto.nddb.repository;

import com.invicto.nddb.entity.Contract;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ContractRepo  extends CrudRepository<Contract,Long> {
    Optional<Contract> findContractByInstrumentAndExpiryDateAndSymbol(String instrument, LocalDate localDate, String symbol);
}

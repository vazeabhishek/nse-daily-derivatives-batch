package com.invicto.nddb.repository;

import com.invicto.nddb.entity.ContractData;
import com.invicto.nddb.entity.RunBook;
import org.springframework.data.repository.CrudRepository;

public interface ContractDataRepo extends CrudRepository<ContractData,Long> {
}

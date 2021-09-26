package com.invicto.nddb.repository;

import com.invicto.nddb.entity.Contract;
import com.invicto.nddb.entity.ContractData;
import com.invicto.nddb.entity.RunBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractDataRepo extends CrudRepository<ContractData,Long> {
    Optional<ContractData> findTop1ByContractOrderByCollectionDateDesc(Contract contract);

}

package com.invicto.nddb.repository;

import com.invicto.nddb.entity.Contract;
import com.invicto.nddb.entity.ContractDataAnalytics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ContractDataAnalyticsRepo extends CrudRepository<ContractDataAnalytics, Long> {
    Optional<ContractDataAnalytics> findTop1ByContractOrderByAnalyticsDateDesc(Contract contract);
}

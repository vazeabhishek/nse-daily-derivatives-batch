package com.invicto.nddb.repository;

import com.invicto.nddb.entity.ContractDataAnalytics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContractDataAnalyticsRepo extends CrudRepository<ContractDataAnalytics,Long> {
}

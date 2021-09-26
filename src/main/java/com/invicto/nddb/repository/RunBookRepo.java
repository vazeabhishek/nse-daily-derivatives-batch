package com.invicto.nddb.repository;

import com.invicto.nddb.entity.RunBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunBookRepo extends CrudRepository<RunBook,Long> {
}

package com.ringodev.factory;

import com.ringodev.factory.data.Restriction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictionRepository extends CrudRepository<Restriction,Long> {
}

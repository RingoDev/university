package com.ringodev.factory;

import com.ringodev.factory.data.OrderImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderImpl,Long> {
}

package com.nttdata.bbva.customer.repositories;

import com.nttdata.bbva.customer.documents.CustomerType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICustomerTypeRepository extends ReactiveMongoRepository<CustomerType, String> {}

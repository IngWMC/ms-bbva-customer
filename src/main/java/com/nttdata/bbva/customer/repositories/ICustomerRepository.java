package com.nttdata.bbva.customer.repositories;

import com.nttdata.bbva.customer.documents.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICustomerRepository extends ReactiveMongoRepository<Customer, String> {}

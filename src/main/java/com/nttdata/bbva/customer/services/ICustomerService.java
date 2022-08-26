package com.nttdata.bbva.customer.services;

import com.nttdata.bbva.customer.documents.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICustomerService extends ICRUD<Customer, String> {
	Mono<Customer> findByIdentificationDocument(String identificationDocument);
	Mono<Customer> findOperationByIdentificationDocumentAndProductId(String identificationDocument, String productId);
}

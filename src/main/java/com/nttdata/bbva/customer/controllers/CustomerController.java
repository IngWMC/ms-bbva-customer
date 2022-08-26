package com.nttdata.bbva.customer.controllers;

import com.nttdata.bbva.customer.documents.Customer;
import com.nttdata.bbva.customer.services.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/1.0.0/customers")
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	private ICustomerService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Customer>>> findAll(){
		logger.info("Inicio CustomerController ::: findAll");
		Flux<Customer> customers = service.findAll().doOnNext(x -> logger.info("Fin CustomerController ::: findAll"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(customers));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Mono<Customer>>> findById(@PathVariable("id") String id){
		logger.info("Inicio CustomerController ::: findById ::: " + id);
		Mono<Customer> customer = service.findById(id).doOnNext(x -> logger.info("Fin CustomerController ::: findById"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(customer));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Mono<Customer>>> insert(@Valid @RequestBody Customer obj){
		logger.info("Inicio CustomerController ::: insert ::: " + obj);
		Mono<Customer> customer = service.insert(obj).doOnNext(x -> logger.info("Fin CustomerController ::: insert"));
		return Mono.just(new ResponseEntity<Mono<Customer>>(customer, HttpStatus.CREATED));
	}
	
	@PutMapping
	public Mono<ResponseEntity<Mono<Customer>>> update(@Valid @RequestBody Customer obj){
		logger.info("Inicio CustomerController ::: update ::: " + obj);
		Mono<Customer> customer = service.update(obj).doOnNext(x -> logger.info("Fin CustomerController ::: update"));
		return Mono.just(new ResponseEntity<Mono<Customer>>(customer, HttpStatus.CREATED));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		logger.info("Inicio CustomerController ::: delete ::: " + id);
		service.delete(id).doOnNext(x -> logger.info("Fin CustomerController ::: delete"));
		return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}

	@GetMapping("/document/{identificationDocument}")
	public Mono<ResponseEntity<Mono<Customer>>> findByIdentificationDocument(@PathVariable("identificationDocument") String identificationDocument){
		logger.info("Inicio CustomerController ::: findByIdentificationDocument ::: " + identificationDocument);
		Mono<Customer> customer = service.findByIdentificationDocument(identificationDocument).doOnNext(x -> logger.info("Fin CustomerController ::: findByIdentificationDocument"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(customer));
	}

	@GetMapping("/operations/{identificationDocument}/{productID}")
	public Mono<ResponseEntity<Mono<Customer>>> findOperationByIdentificationDocumentAndProductId(
			@PathVariable("identificationDocument") String identificationDocument,
			@PathVariable("productID") String productID){
		logger.info("Inicio CustomerController ::: findOperationByIdentificationDocumentAndProductId ::: " + identificationDocument + " ::: " + productID);
		Mono<Customer> customer = service.findOperationByIdentificationDocumentAndProductId(identificationDocument, productID).doOnNext(x -> logger.info("Fin CustomerController ::: findOperationByIdentificationDocumentAndProductId"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(customer));
	}
}

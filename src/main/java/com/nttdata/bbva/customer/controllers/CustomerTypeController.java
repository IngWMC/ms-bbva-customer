package com.nttdata.bbva.customer.controllers;

import com.nttdata.bbva.customer.documents.CustomerType;
import com.nttdata.bbva.customer.services.ICustomerTypeService;
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
@RequestMapping("api/1.0.0/customertypes")
public class CustomerTypeController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerTypeController.class);
	@Autowired
	private ICustomerTypeService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<CustomerType>>> findAll(){
		logger.info("Inicio CustomerTypeController ::: findAll");
		Flux<CustomerType> customerTypes = service.findAll().doOnNext(x -> logger.info("Fin CustomerTypeController ::: findAll"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(customerTypes));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Mono<CustomerType>>> findById(@PathVariable("id") String id){
		logger.info("Inicio CustomerTypeController ::: findById ::: " + id);
		Mono<CustomerType> customerType = service.findById(id).doOnNext(x -> logger.info("Fin CustomerTypeController ::: findById"));
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(customerType));
	}
	
	@PostMapping
	public Mono<ResponseEntity<Mono<CustomerType>>> insert(@Valid @RequestBody CustomerType obj){
		logger.info("Inicio CustomerTypeController ::: insert ::: " + obj);
		Mono<CustomerType> customerType = service.insert(obj).doOnNext(x -> logger.info("Fin CustomerTypeController ::: insert"));
		return Mono.just(new ResponseEntity<Mono<CustomerType>>(customerType, HttpStatus.CREATED));
	}
	
	@PutMapping
	public Mono<ResponseEntity<Mono<CustomerType>>> update(@Valid @RequestBody CustomerType obj){
		logger.info("Inicio CustomerTypeController ::: update ::: " + obj);
		Mono<CustomerType> customerType = service.update(obj).doOnNext(x -> logger.info("Fin CustomerTypeController ::: update"));
		return Mono.just(new ResponseEntity<>(customerType, HttpStatus.CREATED));
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
		logger.info("Inicio CustomerTypeController ::: delete ::: " + id);
		service.delete(id).doOnNext(x -> logger.info("Fin CustomerTypeController ::: delete"));
		return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
	}
}

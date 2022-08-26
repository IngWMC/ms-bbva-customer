package com.nttdata.bbva.customer.services.impl;

import com.nttdata.bbva.customer.clients.OpenAccountClient;
import com.nttdata.bbva.customer.clients.OperationClient;
import com.nttdata.bbva.customer.documents.Customer;
import com.nttdata.bbva.customer.exceptions.BadRequestException;
import com.nttdata.bbva.customer.exceptions.ModelNotFoundException;
import com.nttdata.bbva.customer.repositories.ICustomerRepository;
import com.nttdata.bbva.customer.services.ICustomerService;
import com.nttdata.bbva.customer.services.ICustomerTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements ICustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	@Autowired
	private OpenAccountClient openAccountClient;
	@Autowired
	private OperationClient operationClient;
	@Autowired
	private ICustomerTypeService customerTypeService;
	@Autowired
	private ICustomerRepository repo;
	
	@Override
	public Mono<Customer> insert(Customer obj) {
		return customerTypeService.findById(obj.getCustomerTypeId())
				.switchIfEmpty(Mono.error(() -> new BadRequestException("El campo customerTypeId tiene un valor no válido.")))
				.flatMap(customerType -> this.existIdentificationDocument(obj.getIdentificationDocument())
						.flatMap(existIdentificationDocument -> {
							if (existIdentificationDocument) return Mono.error(() -> new BadRequestException("El documento de identificación ya está registrado."));
							else return this.existEmailAddress(obj.getEmailAddress())
										.flatMap(existEmailAddress -> {
											if (existEmailAddress) return Mono.error(() -> new BadRequestException("El correo electrónico ya está registrado."));
											else return repo.save(obj)
													.map(customer -> {
														customer.setCustomerType(customerType);
														return customer;
													});
										});
						})
				)
				.doOnNext(c -> logger.info("SE INSERTÓ EL CLIENTE ::: " + c.getId()));
	}

	@Override
	public Mono<Customer> update(Customer obj) {
		if (obj.getId() == null || obj.getId().isEmpty())
			return Mono.error(() -> new BadRequestException("El campo id es requerido."));
		
		return repo.findById(obj.getId())
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("CLIENTE NO ENCONTRADO")))
				.flatMap(c -> customerTypeService.findById(obj.getCustomerTypeId())
						.switchIfEmpty(Mono.error(() -> new BadRequestException("El campo customerTypeId tiene un valor no válido.")))
						.flatMap(customerType -> repo.save(obj)
								.map(customer -> {
									customer.setCustomerType(customerType);
									return customer;
								})
						)
				)
				.doOnNext(c -> logger.info("SE ACTUALIZÓ EL CLIENTE ::: " + c.getId()));
	}

	@Override
	public Flux<Customer> findAll() {
		return repo.findAll()
				.flatMap(customer -> customerTypeService.findById(customer.getCustomerTypeId())
						.map(customerType -> {
							customer.setCustomerType(customerType);
							return customer;
						})
				);
	}

	@Override
	public Mono<Customer> findById(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("CLIENTE NO ENCONTRADO")))
				.flatMap(customer -> customerTypeService.findById(customer.getCustomerTypeId())
						.map(customerType -> {
							customer.setCustomerType(customerType);
							return customer;
						})
				)
				.doOnNext(c -> logger.info("SE ENCONTRÓ EL CLIENTE ::: " + id));
	}

	@Override
	public Mono<Void> delete(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("CLIENTE NO ENCONTRADO")))
				.flatMap(customer -> repo.deleteById(customer.getId()))
				.doOnNext(c -> logger.info("SE ELIMINÓ EL CLIENTE ::: " + id));
	}

	@Override
	public Mono<Customer> findByIdentificationDocument(String identificationDocument){
		return repo.findAll()
				.filter(customers -> customers.getIdentificationDocument().equals(identificationDocument))
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("CLIENTE NO ENCONTRADO ::: " + identificationDocument)))
				.next()
				.flatMap(customer -> customerTypeService.findById(customer.getCustomerTypeId())
						.map(customerType -> {
							customer.setCustomerType(customerType);
							return customer;
						})
				)
				.doOnNext(c -> logger.info("SE ENCONTRÓ EL CLIENTE CON NÚMERO DE DOCUMENTO ::: " + identificationDocument));
	}

	@Override
	public Mono<Customer> findOperationByIdentificationDocumentAndProductId(String identificationDocument, String productId){
		return  this.findAll()
				.filter(c -> c.getIdentificationDocument().equals(identificationDocument))
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("CLIENTE NO ENCONTRADO")))
				.flatMap(customer -> {
					String customerID = customer.getId();
					customer.setCustomerTypeId(null);
					return openAccountClient.findAll()
							.filter(openAccount -> openAccount.getCustomerId().equals(customerID) && openAccount.getProductId().equals(productId))
							.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("EL CLIENTE NO TIENE UN CONTRATO CON EL PRODUCTO ::: " + productId)))
							.flatMap( openAccount ->  operationClient.findByOpenAccountId(openAccount.getId())
											.map(operation -> { operation.setOpenAccountId(null); return operation; })
											.collectList()
											.flatMapMany(operations -> {
												openAccount.setOperations(operations);
												return Flux.just(openAccount);
											})
									)
							.map(openAccount -> { openAccount.setProductId(null); openAccount.setCustomerId(null); openAccount.setCustomer(null); return openAccount; })
							.collectList()
							.flatMapMany(openAccounts -> {
								customer.setOpenAccounts(openAccounts);
								return Flux.just(customer);
							});
				}).next()
				.doOnNext(c -> logger.info("SE ENCONTRÓ LAS OPERACIONES DEL CLIENTE CON NÚMERO DE DOCUMENTO ::: " + identificationDocument + " Y PRODUCTO ::: " + productId));
	}

	private Mono<Boolean> existIdentificationDocument(String identificationDocument) {
		return repo.findAll()
				.filter(customers -> customers.getIdentificationDocument().equals(identificationDocument))
				.next()
				.hasElement()
				.doOnNext(exist -> {
					if (exist) logger.info("SE ENCONTRÓ EL DOCUMENTO DE IDENTIDAD ::: " + identificationDocument);
					else logger.info("NO SE ENCONTRÓ EL DOCUMENTO DE IDENTIDAD ::: " + identificationDocument);
				});
	}
	private Mono<Boolean> existEmailAddress(String emailAddress) {
		return repo.findAll()
				.filter(customers -> customers.getEmailAddress().equals(emailAddress))
				.next()
				.hasElement()
				.doOnNext(exist -> {
					if (exist) logger.info("SE ENCONTRÓ EL CORREO ::: " + emailAddress);
					else logger.info("NO SE ENCONTRÓ EL CORREO ::: " + emailAddress);
				});
	}
}

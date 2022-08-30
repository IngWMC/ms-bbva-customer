package com.nttdata.bbva.customer.services.impl;

import com.nttdata.bbva.customer.documents.CustomerType;
import com.nttdata.bbva.customer.exceptions.BadRequestException;
import com.nttdata.bbva.customer.exceptions.ModelNotFoundException;
import com.nttdata.bbva.customer.repositories.ICustomerTypeRepository;
import com.nttdata.bbva.customer.services.ICustomerTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerTypeServiceImpl implements ICustomerTypeService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
			
	@Autowired
	private ICustomerTypeRepository repo;
	
	@Override
	public Mono<CustomerType> insert(CustomerType obj) {
		return repo.save(obj)
				.doOnNext(tc -> logger.info("SE INSERTÓ EL TIPO CLIENTE ::: " + tc.getId()));
	}

	@Override
	public Mono<CustomerType> update(CustomerType obj) {
		if (obj.getId() == null || obj.getId().isEmpty())
			return Mono.error(() -> new BadRequestException("El campo id es requerido."));

		return repo.findById(obj.getId())
				.switchIfEmpty(Mono.error(() -> new BadRequestException("El campo id no es válido.")))
				.flatMap(customerType -> repo.save(obj))
				.doOnNext(pt -> logger.info("SE ACTUALIZÓ EL TIPO CLIENTE ::: " + pt.getId()));
	}

	@Override
	public Flux<CustomerType> findAll() {
		return repo.findAll();
	}

	@Override
	public Mono<CustomerType> findById(String id) {
		return repo.findById(id)
				.switchIfEmpty(Mono.error(() -> new ModelNotFoundException("TIPO CLIENTE NO ENCONTRADO")));
	}

	@Override
	public Mono<Void> delete(String id) {
		return repo.deleteById(id);
	}

}

package com.nttdata.bbva.customer.clients;

import com.nttdata.bbva.customer.documents.OpenAccount;
import com.nttdata.bbva.customer.documents.Operation;
import com.nttdata.bbva.customer.exceptions.ModelNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OperationClient {
    private static final Logger logger = LoggerFactory.getLogger(OperationClient.class);
    private final WebClient webClient;

    public OperationClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:7074/api/1.0.0/operations").build();
    }

    @CircuitBreaker(name = "operation", fallbackMethod = "fallBackFindByOpenAccountId")
    public Flux<Operation> findByOpenAccountId(String openAccountId){
        logger.info("Inicio OperationClient ::: findByOpenAccountId ::: " + openAccountId);
        return this.webClient.get()
                .uri("/openaccounts/{openAccountId}", openAccountId)
                .retrieve()
                //.onStatus()
                .bodyToFlux(Operation.class)
                .doOnNext(x -> logger.info("Fin OperationClient ::: findByOpenAccountId"));
    }

    private Mono<String> fallBackFindByOpenAccountId(String openAccountId, RuntimeException e) {
        return Mono.error(() -> new ModelNotFoundException("Microservicio Operation no está repondiendo."));
    }
}

package com.nttdata.bbva.customer.clients;

import com.nttdata.bbva.customer.documents.OpenAccount;
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
public class OpenAccountClient {
    private static final Logger logger = LoggerFactory.getLogger(OpenAccountClient.class);
    private final WebClient webClient;

    public OpenAccountClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:7073/api/1.0.0/openaccounts").build();
    }

    @CircuitBreaker(name = "openaccount", fallbackMethod = "fallBackFindAll")
    public Flux<OpenAccount> findAll(){
        logger.info("Inicio OpenAccountClient ::: findAll");
        return this.webClient.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(OpenAccount.class)
                .doOnNext(x -> logger.info("Fin OpenAccountClient ::: findAll"));
    }

    private Mono<String> fallBackFindAll(String id, RuntimeException e) {
        return Mono.error(() -> new ModelNotFoundException("Microservicio OpenAccount no está repondiendo."));
    }
}

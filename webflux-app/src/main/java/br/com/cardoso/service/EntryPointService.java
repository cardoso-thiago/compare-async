package br.com.cardoso.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EntryPointService {

    Mono<String> getSequentialResult();
    Flux<String> getParallelResult();
}

package br.com.cardoso.service.impl;

import br.com.cardoso.service.EntryPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EntryPointServiceImpl implements EntryPointService {

    private final Logger logger = LoggerFactory.getLogger(EntryPointServiceImpl.class);

    public Mono<String> getSequentialResult() {
        long startTime = System.currentTimeMillis();
        return getResult("a", null)
                .flatMap(responseA -> getResult("b", responseA)
                        .flatMap(responseB -> getResult("c", responseB)
                                .flatMap(responseC -> getResult("d", responseC)
                                        .flatMap(responseD -> getResult("e", responseD)
                                        ))))
                .doOnSuccess(success -> {
                    long endTime = System.currentTimeMillis();
                    logger.info("Sequential Result executed in " + (endTime - startTime) + "ms");
                });
    }

    @Override
    public Flux<String> getParallelResult() {
        long startTime = System.currentTimeMillis();
        return Flux.merge(getResult("a", null),
                getResult("b", "a"),
                getResult("c", "b"),
                getResult("d", "c"),
                getResult("e", "d")
        ).doOnComplete(() -> {
            long endTime = System.currentTimeMillis();
            logger.info("Parallel Result executed in " + (endTime - startTime) + "ms");
        });
    }

    private Mono<String> getResult(String path, String param) {
        String uri = path;
        if (param != null) {
            uri = uri + "/" + param;
        }
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        return webClient.get().uri(uri).retrieve().bodyToMono(String.class);
    }
}

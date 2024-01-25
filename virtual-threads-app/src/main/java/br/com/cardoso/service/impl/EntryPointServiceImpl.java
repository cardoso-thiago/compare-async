package br.com.cardoso.service.impl;

import br.com.cardoso.service.EntryPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EntryPointServiceImpl implements EntryPointService {

    private final Logger logger = LoggerFactory.getLogger(EntryPointServiceImpl.class);

    public String getSequentialResult() {
        long startTime = System.currentTimeMillis();

        CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> getResult("a", null));
        CompletableFuture<String> futureB = futureA.thenCompose(responseA -> CompletableFuture.supplyAsync(() -> getResult("b", responseA)));
        CompletableFuture<String> futureC = futureB.thenCompose(responseB -> CompletableFuture.supplyAsync(() -> getResult("c", responseB)));
        CompletableFuture<String> futureD = futureC.thenCompose(responseC -> CompletableFuture.supplyAsync(() -> getResult("d", responseC)));
        CompletableFuture<String> futureE = futureD.thenCompose(responseD -> CompletableFuture.supplyAsync(() -> getResult("e", responseD)));

        return futureE.thenApply(result -> {
            long endTime = System.currentTimeMillis();
            System.out.println("Sequential Result executed in " + (endTime - startTime) + "ms on " + Thread.currentThread());
            return result;
        }).join();
    }

    @Override
    public String getParallelResult() {
        long startTime = System.currentTimeMillis();
        List<CompletableFuture<String>> futures = new ArrayList<>();
        futures.add(CompletableFuture.supplyAsync(() -> getResult("a", null)));
        futures.add(CompletableFuture.supplyAsync(() -> getResult("b", "a")));
        futures.add(CompletableFuture.supplyAsync(() -> getResult("c", "b")));
        futures.add(CompletableFuture.supplyAsync(() -> getResult("d", "c")));
        futures.add(CompletableFuture.supplyAsync(() -> getResult("e", "d")));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        CompletableFuture<String> combinedResult = allOf.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.joining(""))
        );

        String response = combinedResult.join();
        long endTime = System.currentTimeMillis();
        logger.info("Parallel Result executed in " + (endTime - startTime) + "ms on " + Thread.currentThread());
        return response;
    }

    private String getResult(String path, String param) {
        String uri = path;
        if (param != null) {
            uri = uri + "/" + param;
        }
        RestClient restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
        return restClient.get().uri(uri).retrieve().body(String.class);
    }
}

package br.com.cardoso.controller;

import br.com.cardoso.service.EntryPointService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/entrypoint")
public class WebFluxEntryPoint {

    private final EntryPointService entryPointService;

    public WebFluxEntryPoint(EntryPointService entryPointService) {
        this.entryPointService = entryPointService;
    }

    @GetMapping("/sequential")
    public Mono<String> getSequentialResult() {
        return entryPointService.getSequentialResult();
    }

    @GetMapping("/parallel")
    public Flux<String> getParallelResult() {
        return entryPointService.getParallelResult();
    }
}

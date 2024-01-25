package br.com.cardoso.controller;

import br.com.cardoso.service.EntryPointService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entrypoint")
public class VirtualThreadsEntryPoint {

    private final EntryPointService entryPointService;

    public VirtualThreadsEntryPoint(EntryPointService entryPointService) {
        this.entryPointService = entryPointService;
    }

    @GetMapping("/sequential")
    public String getSequentialResult() throws Exception {
        return entryPointService.getSequentialResult();
    }

    @GetMapping("/parallel")
    public String getParallelResult() throws Exception {
        return entryPointService.getParallelResult();
    }
}

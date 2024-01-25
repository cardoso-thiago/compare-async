package br.com.cardoso.service.impl;

import br.com.cardoso.service.EntryPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class EntryPointServiceImpl implements EntryPointService {

    private final AsyncTaskExecutor asyncTaskExecutor;

    private final Logger logger = LoggerFactory.getLogger(EntryPointServiceImpl.class);

    public EntryPointServiceImpl(AsyncTaskExecutor asyncTaskExecutor) {
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public String getSequentialResult() throws Exception {
        long startTime = System.currentTimeMillis();
        var future1 = asyncTaskExecutor.submit(() -> getResult("a", null));
        var future2 = asyncTaskExecutor.submit(() -> getResult("b", future1.get()));
        var future3 = asyncTaskExecutor.submit(() -> getResult("c", future2.get()));
        var future4 = asyncTaskExecutor.submit(() -> getResult("d", future3.get()));
        var future5 = asyncTaskExecutor.submit(() -> getResult("e", future4.get()));
        var response = future5.get();
        long endTime = System.currentTimeMillis();
        logger.info("Sequential Result executed in " + (endTime - startTime) + "ms on " + Thread.currentThread());
        return response;
    }

    @Override
    public String getParallelResult() throws Exception {
        long startTime = System.currentTimeMillis();
        var future1 = asyncTaskExecutor.submit(() -> getResult("a", null));
        var future2 = asyncTaskExecutor.submit(() -> getResult("b", "a"));
        var future3 = asyncTaskExecutor.submit(() -> getResult("c", "b"));
        var future4 = asyncTaskExecutor.submit(() -> getResult("d", "c"));
        var future5 = asyncTaskExecutor.submit(() -> getResult("e", "d"));
        var response = future1.get() + future2.get() + future3.get() + future4.get() + future5.get();
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

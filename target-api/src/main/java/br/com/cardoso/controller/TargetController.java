package br.com.cardoso.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TargetController {

    @GetMapping("/a")
    public String getA() throws InterruptedException {
        Thread.sleep(50);
        return "a";
    }

    @GetMapping("/b/{param}")
    public String getB(@PathVariable("param") String param) throws Exception {
        Thread.sleep(50);
        if (param.equals("a")) {
            return "b";
        }
        throw new Exception("Erro ao obter o param \"a\"");
    }

    @GetMapping("/c/{param}")
    public String getC(@PathVariable("param") String param) throws Exception {
        Thread.sleep(50);
        if (param.equals("b")) {
            return "c";
        }
        throw new Exception("Erro ao obter o param \"b\"");
    }

    @GetMapping("/d/{param}")
    public String getD(@PathVariable("param") String param) throws Exception {
        Thread.sleep(50);
        if (param.equals("c")) {
            return "d";
        }
        throw new Exception("Erro ao obter o param \"c\"");
    }

    @GetMapping("/e/{param}")
    public String getE(@PathVariable("param") String param) throws Exception {
        Thread.sleep(50);
        if (param.equals("d")) {
            return "e";
        }
        throw new Exception("Erro ao obter o param \"d\"");
    }
}

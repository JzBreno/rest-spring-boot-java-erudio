package br.com.jzbreno.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//com o lombock e sl4j de dependencias, podemos usar o @SLF4J e evitar ter que instanciar um objeto logger
@RestController
@RequestMapping("/log")
public class TestLogController {

    private final Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @GetMapping()
    @RequestMapping("/test")
    public String TestLogController() {
        logger.info("Test log generated successfully");
        logger.warn("Test log generated successfully");
        logger.error("Test log generated successfully");
        logger.debug("Test log generated successfully teste");
        return "Logs generated successfully";
    }
}

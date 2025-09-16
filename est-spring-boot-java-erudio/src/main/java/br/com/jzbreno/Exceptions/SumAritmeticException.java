package br.com.jzbreno.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
//usando para iserir qual status se ira retornar
//esse cara ira gerar um status code
public class SumAritmeticException extends RuntimeException {
    public SumAritmeticException(String message) {
        super(message);
    }
}

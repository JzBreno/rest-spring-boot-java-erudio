package br.com.jzbreno.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)//usando para iserir qual status se ira retornar//esse cara ira gerar um status code
public class InvalidJwtAutheticationException extends AuthenticationException{

    public InvalidJwtAutheticationException(String message) {
        super(message);
    }

    public InvalidJwtAutheticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

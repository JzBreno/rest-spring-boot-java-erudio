package br.com.jzbreno.Exceptions.handler;

import br.com.jzbreno.Exceptions.ExceptionResponse;
import br.com.jzbreno.Exceptions.SumAritmeticException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.IllegalFormatException;

@RestController
@ControllerAdvice
//Essa anotacao vai ser usada sempre que precisar centralizar um tratamento que seria espalhado em todos os controllers
//quando um controller lancar uma excesao, caso nao seja fornecido um tratamento mais adequado, ele ira cair nesse tratamento global do controller advice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    };

    @ExceptionHandler(NumberFormatException.class)
    public final ResponseEntity<ExceptionResponse> handleIlegalAritmeticExceptions(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    };

    @ExceptionHandler(SumAritmeticException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request){
        ExceptionResponse response = new ExceptionResponse(new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    };

}

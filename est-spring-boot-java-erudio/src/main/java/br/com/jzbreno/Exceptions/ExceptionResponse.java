package br.com.jzbreno.Exceptions;

import java.util.Date;
//esse cara ira gerar um json
public record ExceptionResponse(Date timestamp, String message, String details) {
}

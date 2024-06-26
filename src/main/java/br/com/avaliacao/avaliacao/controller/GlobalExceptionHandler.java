package br.com.avaliacao.avaliacao.controller;

import br.com.avaliacao.avaliacao.exception.*;
import br.com.avaliacao.avaliacao.model.dto.RetornoErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            PautaFinalizadaException.class,
            CpfInvalidoException.class,
            PautaJaIniciadaException.class,
            PautaNaoExistenteException.class,
            VotoRealizadoException.class
    })
    public ResponseEntity<RetornoErroDTO> handleCustomExceptions(Exception ex, WebRequest request) {
        HttpStatus status;
        if (ex instanceof PautaFinalizadaException) {
            status = HttpStatus.GONE;
        } else if (ex instanceof CpfInvalidoException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof PautaJaIniciadaException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof PautaNaoExistenteException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof VotoRealizadoException) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR; // Default status for unknown exceptions
        }

        RetornoErroDTO errorResponse = new RetornoErroDTO(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorResponse, status);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RetornoErroDTO> handleGlobalException(Exception ex, WebRequest request) {
        RetornoErroDTO errorResponse = new RetornoErroDTO("Internal Server Error", request.getDescription(false));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

package io.github.VitorPalazzo.rest.controller;

import io.github.VitorPalazzo.exception.PedidoNaoEncontradoException;
import io.github.VitorPalazzo.exception.RegraNegocioException;
import io.github.VitorPalazzo.rest.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicarionControllerAdvice {

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleRegraNegocioException(RegraNegocioException ex){
       String mensagemErro = ex.getMessage();
       return new ApiErrors(mensagemErro);
    }
    @ExceptionHandler(PedidoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrors handlePeididoNotFoundException(PedidoNaoEncontradoException ex)
    {
        return new ApiErrors(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleMethodNotValidException(MethodArgumentNotValidException ex){
     List<String> errors = ex.getBindingResult().getAllErrors().stream().map(erro -> erro.getDefaultMessage()).collect(Collectors.toList());
    return new ApiErrors(errors);
    }

}

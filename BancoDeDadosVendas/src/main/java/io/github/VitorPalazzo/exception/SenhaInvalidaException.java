package io.github.VitorPalazzo.exception;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException(){
        super("Senha Invalida");
    }
}

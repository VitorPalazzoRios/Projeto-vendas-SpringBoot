package io.github.VitorPalazzo.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(){
        super("Pedido não encontrado.");
    }
}

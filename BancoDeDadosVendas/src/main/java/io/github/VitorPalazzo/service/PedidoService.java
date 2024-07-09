package io.github.VitorPalazzo.service;

import io.github.VitorPalazzo.domain.entity.Pedido;
import io.github.VitorPalazzo.domain.enums.StatusPedido;
import io.github.VitorPalazzo.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar (PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}

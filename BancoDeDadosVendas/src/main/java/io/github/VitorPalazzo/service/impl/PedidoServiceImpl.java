package io.github.VitorPalazzo.service.impl;

import io.github.VitorPalazzo.domain.entity.Cliente;
import io.github.VitorPalazzo.domain.entity.ItemPedido;
import io.github.VitorPalazzo.domain.entity.Pedido;
import io.github.VitorPalazzo.domain.entity.Produto;
import io.github.VitorPalazzo.domain.enums.StatusPedido;
import io.github.VitorPalazzo.domain.repository.Clientes;
import io.github.VitorPalazzo.domain.repository.ItemsPedido;
import io.github.VitorPalazzo.domain.repository.Pedidos;
import io.github.VitorPalazzo.domain.repository.Produtos;
import io.github.VitorPalazzo.exception.PedidoNaoEncontradoException;
import io.github.VitorPalazzo.exception.RegraNegocioException;
import io.github.VitorPalazzo.rest.dto.ItemPedidoDTO;
import io.github.VitorPalazzo.rest.dto.PedidoDTO;
import io.github.VitorPalazzo.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItemsPedido itemsPedidoRepository;



    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository.findById(idCliente).orElseThrow(() -> new RegraNegocioException("Código de cliente invalido."));
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);
        converterItems(pedido, dto.getItems());
        List<ItemPedido> itemsPedidos = converterItems(pedido, dto.getItems());
        repository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedidos);
        pedido.setItens(itemsPedidos);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                return repository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException());

    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items){
       if(items.isEmpty()) {
           throw new RegraNegocioException("Não é possivel realizar um pedido sem items.");
       }
       return items.stream().map( dto -> {
           Integer IdProduto = dto.getProduto();
           Produto produto = produtosRepository.findById(IdProduto).orElseThrow(() -> new RegraNegocioException("Código de produto invalido." + IdProduto));

           ItemPedido itemPedido = new ItemPedido();
           itemPedido.setQuantidade(dto.getQuantidade());
           itemPedido.setPedido(pedido);
           itemPedido.setProduto(produto);
           return itemPedido;
       }).collect(Collectors.toList());
    }

}

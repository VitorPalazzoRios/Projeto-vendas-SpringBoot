package io.github.VitorPalazzo.domain.repository;


import io.github.VitorPalazzo.domain.entity.Cliente;
import io.github.VitorPalazzo.domain.entity.ItemPedido;
import io.github.VitorPalazzo.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemsPedido extends JpaRepository<ItemPedido, Integer> {


}

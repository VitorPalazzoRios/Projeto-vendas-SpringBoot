package io.github.VitorPalazzo.rest.controller;


import io.github.VitorPalazzo.domain.entity.Cliente;
import io.github.VitorPalazzo.domain.entity.Produto;
import io.github.VitorPalazzo.domain.repository.Clientes;
import io.github.VitorPalazzo.domain.repository.Produtos;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    //Repository
    private Produtos produtos;

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @GetMapping("/{id}")
    public Produto getProdutoById(@PathVariable Integer id){
        return produtos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    @PostMapping("/array")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Produto> saveArray(@RequestBody Produto[] arrayProdutos){
        List<Produto> listaProdutos = Arrays.asList(arrayProdutos);
        // Salva cada produto no banco de dados
        for (Produto produto : listaProdutos) {
            produtos.save(produto);
        }
        // Retorna a lista
        return listaProdutos;
    }



    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save( @RequestBody @Valid Produto produto){
        return produtos.save(produto);

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        produtos.findById(id).map( produto -> {
            produtos.delete(produto);
            return produto;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable @Valid Integer id, @RequestBody Produto produto) {
        produtos.findById(id).map(produtoExistente -> {
                    produto.setId(produtoExistente.getId());
                    produtos.save(produto);
                    return produtoExistente;
                }
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

    }

    @GetMapping("")
    public List<Produto> find(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher( ExampleMatcher.StringMatcher.CONTAINING);
        Example example = Example.of(filtro, matcher);
        return produtos.findAll(example);

    }

}

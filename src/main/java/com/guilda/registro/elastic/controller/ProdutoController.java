package com.guilda.registro.elastic.controller;

import com.guilda.registro.elastic.dto.*;
import com.guilda.registro.elastic.model.Produto;
import com.guilda.registro.elastic.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // ---------- Parte A ----------
    @GetMapping("/busca/nome")
    public List<Produto> buscarPorNome(@RequestParam String termo) throws Exception {
        return produtoService.buscarPorNome(termo);
    }

    @GetMapping("/busca/descricao")
    public List<Produto> buscarPorDescricao(@RequestParam String termo) throws Exception {
        return produtoService.buscarPorDescricao(termo);
    }

    @GetMapping("/busca/frase")
    public List<Produto> buscarFraseExata(@RequestParam String termo) throws Exception {
        return produtoService.buscarFraseExata(termo);
    }

    @GetMapping("/busca/fuzzy")
    public List<Produto> buscarFuzzy(@RequestParam String termo) throws Exception {
        return produtoService.buscarFuzzy(termo);
    }

    @GetMapping("/busca/multicampos")
    public List<Produto> buscarMultiCampos(@RequestParam String termo) throws Exception {
        return produtoService.buscarMultiCampos(termo);
    }

    // ---------- Parte B ----------
    @GetMapping("/busca/com-filtro")
    public List<Produto> buscarPorDescricaoECategoria(
            @RequestParam String termo,
            @RequestParam String categoria) throws Exception {
        return produtoService.buscarPorDescricaoECategoria(termo, categoria);
    }

    @GetMapping("/busca/faixa-preco")
    public List<Produto> buscarPorFaixaPreco(
            @RequestParam Double min,
            @RequestParam Double max) throws Exception {
        return produtoService.buscarPorFaixaPreco(min, max);
    }

    @GetMapping("/busca/avancada")
    public List<Produto> buscaAvancada(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String raridade,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max) throws Exception {
        return produtoService.buscarAvancada(categoria, raridade, min, max);
    }

    // ---------- Parte C ----------
    @GetMapping("/agregacoes/por-categoria")
    public List<CategoriaCountDTO> contarPorCategoria() throws Exception {
        return produtoService.contarPorCategoria();
    }

    @GetMapping("/agregacoes/por-raridade")
    public List<RaridadeCountDTO> contarPorRaridade() throws Exception {
        return produtoService.contarPorRaridade();
    }

    @GetMapping("/agregacoes/preco-medio")
    public PrecoMedioDTO precoMedio() throws Exception {
        return produtoService.precoMedio();
    }

    @GetMapping("/agregacoes/faixas-preco")
    public List<FaixaPrecoDTO> faixasPreco() throws Exception {
        return produtoService.faixasPreco();
    }
}
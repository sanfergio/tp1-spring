package com.guilda.registro.elastic.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AvgAggregate;
import com.guilda.registro.elastic.dto.*;
import com.guilda.registro.elastic.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    // ---------- Parte A: Buscas Textuais ----------

    public List<Produto> buscarPorNome(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo)))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Produto> buscarPorDescricao(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("descricao").query(termo)))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Produto> buscarFraseExata(String frase) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.matchPhrase(m -> m.field("descricao").query(frase)))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Produto> buscarFuzzy(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.fuzzy(f -> f.field("nome").value(termo).fuzziness("AUTO")))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Produto> buscarMultiCampos(String termo) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m.fields("nome", "descricao").query(termo)))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    // ---------- Parte B: Buscas com Filtros ----------

    public List<Produto> buscarPorDescricaoECategoria(String termo, String categoria) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .must(m -> m.match(match -> match.field("descricao").query(termo)))
                        .must(m -> m.term(t -> t.field("categoria").value(categoria)))
                ))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Produto> buscarPorFaixaPreco(Double min, Double max) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r.field("preco").gte(min).lte(max)))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    public List<Produto> buscarAvancada(String categoria, String raridade, Double min, Double max) {
        var boolBuilder = new co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder();
        if (categoria != null)
            boolBuilder.must(m -> m.term(t -> t.field("categoria").value(categoria)));
        if (raridade != null)
            boolBuilder.must(m -> m.term(t -> t.field("raridade").value(raridade)));
        if (min != null && max != null)
            boolBuilder.must(m -> m.range(r -> r.field("preco").gte(min).lte(max)));

        var query = NativeQuery.builder()
                .withQuery(q -> q.bool(boolBuilder.build()))
                .build();
        return elasticsearchOperations.search(query, Produto.class)
                .map(SearchHit::getContent)
                .toList();
    }

    // ---------- Parte C: Agregações ----------

    public List<CategoriaCountDTO> contarPorCategoria() {
        String aggName = "por_categoria";
        Aggregation aggregation = Aggregation.of(a -> a.terms(t -> t.field("categoria").size(100)));
        var query = NativeQuery.builder()
                .withAggregation(aggName, aggregation)
                .build();
        var result = elasticsearchOperations.search(query, Produto.class);
        var termsAgg = result.getAggregations().get(aggName).getAggregation().getAggregate().getSterms();
        return termsAgg.buckets().array().stream()
                .map(bucket -> new CategoriaCountDTO(bucket.key().stringValue(), bucket.docCount()))
                .collect(Collectors.toList());
    }

    public List<RaridadeCountDTO> contarPorRaridade() {
        String aggName = "por_raridade";
        Aggregation aggregation = Aggregation.of(a -> a.terms(t -> t.field("raridade").size(100)));
        var query = NativeQuery.builder()
                .withAggregation(aggName, aggregation)
                .build();
        var result = elasticsearchOperations.search(query, Produto.class);
        var termsAgg = result.getAggregations().get(aggName).getAggregation().getAggregate().getSterms();
        return termsAgg.buckets().array().stream()
                .map(bucket -> new RaridadeCountDTO(bucket.key().stringValue(), bucket.docCount()))
                .collect(Collectors.toList());
    }

    public PrecoMedioDTO precoMedio() {
        String aggName = "preco_medio";
        Aggregation aggregation = Aggregation.of(a -> a.avg(avg -> avg.field("preco")));
        var query = NativeQuery.builder()
                .withAggregation(aggName, aggregation)
                .build();
        var result = elasticsearchOperations.search(query, Produto.class);
        AvgAggregate avg = result.getAggregations().get(aggName).getAggregation().getAggregate().getAvg();
        return new PrecoMedioDTO(avg.value());
    }

    public List<FaixaPrecoDTO> faixasPreco() {
        return List.of(
                new FaixaPrecoDTO("Abaixo de 100", contarPorFaixa(null, 100.0)),
                new FaixaPrecoDTO("100 a 300", contarPorFaixa(100.0, 300.0)),
                new FaixaPrecoDTO("300 a 700", contarPorFaixa(300.0, 700.0)),
                new FaixaPrecoDTO("Acima de 700", contarPorFaixa(700.0, null))
        );
    }

    private long contarPorFaixa(Double min, Double max) {
        var query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> {
                    if (min != null) r.gte(min);
                    if (max != null) r.lte(max);
                    return r;
                }))
                .build();
        return elasticsearchOperations.count(query, Produto.class);
    }
}
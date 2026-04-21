package com.guilda.registro.elastic.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AvgAggregate;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.guilda.registro.elastic.dto.*;
import com.guilda.registro.elastic.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ElasticsearchClient esClient;

    private static final String INDEX_NAME = "guilda_loja";

    // ---------- Parte A: Buscas Textuais ----------

    public List<Produto> buscarPorNome(String termo) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.match(m -> m.field("nome").query(termo)))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<Produto> buscarPorDescricao(String termo) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.match(m -> m.field("descricao").query(termo)))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<Produto> buscarFraseExata(String frase) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.matchPhrase(m -> m.field("descricao").query(frase)))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<Produto> buscarFuzzy(String termo) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.fuzzy(f -> f.field("nome").value(termo).fuzziness("AUTO")))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<Produto> buscarMultiCampos(String termo) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.multiMatch(m -> m.fields("nome", "descricao").query(termo)))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    // ---------- Parte B: Buscas com Filtros ----------

    public List<Produto> buscarPorDescricaoECategoria(String termo, String categoria) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.bool(b -> b
                                .must(m -> m.match(mt -> mt.field("descricao").query(termo)))
                                .must(m -> m.term(t -> t.field("categoria").value(categoria)))
                        ))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<Produto> buscarPorFaixaPreco(Double min, Double max) throws Exception {
        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.range(r -> r.field("preco")
                                .gte(JsonData.of(min))
                                .lte(JsonData.of(max))))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    public List<Produto> buscarAvancada(String categoria, String raridade, Double min, Double max) throws Exception {
        var boolBuilder = new co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder();
        if (categoria != null)
            boolBuilder.must(m -> m.term(t -> t.field("categoria").value(categoria)));
        if (raridade != null)
            boolBuilder.must(m -> m.term(t -> t.field("raridade").value(raridade)));
        if (min != null && max != null)
            boolBuilder.must(m -> m.range(r -> r.field("preco")
                    .gte(JsonData.of(min))
                    .lte(JsonData.of(max))));

        SearchResponse<Produto> response = esClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(q -> q.bool(boolBuilder.build()))
                        .size(100),
                Produto.class);
        return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
    }

    // ---------- Parte C: Agregações ----------

    public List<CategoriaCountDTO> contarPorCategoria() throws Exception {
        SearchResponse<Void> response = esClient.search(b -> b
                        .index(INDEX_NAME)
                        .size(0)
                        .aggregations("por_categoria", Aggregation.of(a -> a
                                .terms(t -> t.field("categoria").size(100)))),
                Void.class);

        var terms = response.aggregations().get("por_categoria").sterms();
        return terms.buckets().array().stream()
                .map(bucket -> new CategoriaCountDTO(bucket.key().stringValue(), bucket.docCount()))
                .collect(Collectors.toList());
    }

    public List<RaridadeCountDTO> contarPorRaridade() throws Exception {
        SearchResponse<Void> response = esClient.search(b -> b
                        .index(INDEX_NAME)
                        .size(0)
                        .aggregations("por_raridade", Aggregation.of(a -> a
                                .terms(t -> t.field("raridade").size(100)))),
                Void.class);

        var terms = response.aggregations().get("por_raridade").sterms();
        return terms.buckets().array().stream()
                .map(bucket -> new RaridadeCountDTO(bucket.key().stringValue(), bucket.docCount()))
                .collect(Collectors.toList());
    }

    public PrecoMedioDTO precoMedio() throws Exception {
        SearchResponse<Void> response = esClient.search(b -> b
                        .index(INDEX_NAME)
                        .size(0)
                        .aggregations("preco_medio", Aggregation.of(a -> a
                                .avg(avg -> avg.field("preco")))),
                Void.class);

        AvgAggregate avg = response.aggregations().get("preco_medio").avg();
        return new PrecoMedioDTO(avg.value());
    }

    public List<FaixaPrecoDTO> faixasPreco() throws Exception {
        return List.of(
                new FaixaPrecoDTO("Abaixo de 100", contarPorFaixa(null, 100.0)),
                new FaixaPrecoDTO("100 a 300", contarPorFaixa(100.0, 300.0)),
                new FaixaPrecoDTO("300 a 700", contarPorFaixa(300.0, 700.0)),
                new FaixaPrecoDTO("Acima de 700", contarPorFaixa(700.0, null))
        );
    }

    private long contarPorFaixa(Double min, Double max) throws Exception {
        var rangeBuilder = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder().field("preco");
        if (min != null) rangeBuilder.gte(JsonData.of(min));
        if (max != null) rangeBuilder.lte(JsonData.of(max));

        CountResponse response = esClient.count(c -> c
                .index(INDEX_NAME)
                .query(q -> q.range(rangeBuilder.build())));
        return response.count();
    }
}
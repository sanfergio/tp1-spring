package com.guilda.registro.elastic.repository;

import com.guilda.registro.elastic.model.Produto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProdutoRepository extends ElasticsearchRepository<Produto, String> {

}
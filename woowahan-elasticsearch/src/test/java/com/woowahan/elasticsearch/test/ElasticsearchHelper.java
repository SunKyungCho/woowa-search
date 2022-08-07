package com.woowahan.elasticsearch.test;

import com.woowahan.elasticsearch.shop.index.ShopInfo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.io.Charsets;

import java.io.IOException;
import java.nio.charset.Charset;

public class ElasticsearchHelper {
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    public ElasticsearchHelper(RestHighLevelClient client) {
        this.client = client;
        objectMapper = new ObjectMapper();
    }

    public void createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        String mappings = StreamUtils.copyToString(new ClassPathResource("elasticsearch/mappings.json").getInputStream(), Charset.defaultCharset());
        request.mapping(mappings, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    public void insert(String index, String id, ShopInfo shopInfo) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.source(objectMapper.writeValueAsString(shopInfo), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    public ShopInfo searchById(String index, String id) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(id));
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit hit = response.getHits().getAt(0);
        return objectMapper.readValue(hit.getSourceAsString(), ShopInfo.class);
    }

}

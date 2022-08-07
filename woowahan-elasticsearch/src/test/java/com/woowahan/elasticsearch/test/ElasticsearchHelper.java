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
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.io.Charsets;
import org.testcontainers.utility.Base58;

import java.io.IOException;
import java.nio.charset.Charset;

public class ElasticsearchHelper {
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    public ElasticsearchHelper(RestHighLevelClient client) {
        this.client = client;
        objectMapper = new ObjectMapper();
    }

    public String createIndex() throws IOException {
        String indexName = "test-index-" + Base58.randomString(6).toLowerCase();
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        String mappings = StreamUtils.copyToString(new ClassPathResource("elasticsearch/mappings.json").getInputStream(), Charset.defaultCharset());
        request.mapping(mappings, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
        return indexName;
    }

    public void insert(String index, ShopInfo shopInfo) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.id(shopInfo.getShopNumber());
        request.source(objectMapper.writeValueAsString(shopInfo), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    public ShopInfo searchById(String index, String id) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(id));
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        SearchHit hit = hits[0];
        return objectMapper.readValue(hit.getSourceAsString(), ShopInfo.class);
    }

    public boolean exists(String index, String id) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(id));
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        return hits.length > 0;
    }
}

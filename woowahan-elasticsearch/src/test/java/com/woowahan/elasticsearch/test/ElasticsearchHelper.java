package com.woowahan.elasticsearch.test;

import com.woowahan.elasticsearch.shop.index.ShopInfo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ElasticsearchHelper {
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    public ElasticsearchHelper(RestHighLevelClient client) {
        this.client = client;
        objectMapper = new ObjectMapper();
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

package com.woowahan.elasticsearch.shop.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

@RequiredArgsConstructor
public class ShopQueryRepository {

    private final String index;
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    public ShopInfo searchById(String id) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(id));
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        if(hits.length == 0) {
            throw new RuntimeException();
        }
        return convertShopResponse(hits[0]);
    }

    private ShopInfo convertShopResponse(SearchHit hit) {
        try {
            return objectMapper.readValue(hit.getSourceAsString(), ShopInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

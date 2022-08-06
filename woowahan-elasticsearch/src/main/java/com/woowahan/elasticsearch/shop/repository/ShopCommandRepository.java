package com.woowahan.elasticsearch.shop.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.indices.IndexCreationException;

import java.io.IOException;

public class ShopCommandRepository {

    private static final String INDEX = "baemin-shop";
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    public ShopCommandRepository(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.client = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    public void insert(ShopInfo shopInfo) {
        if (shopInfo == null) {
            throw new IllegalArgumentException("ShopInfo cannot be null");
        }

        IndexRequest request = new IndexRequest(INDEX);
        request.id(shopInfo.getShopNumber());
        try {
            request.source(objectMapper.writeValueAsString(shopInfo), XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IndexCreationException(INDEX, e);
        }
    }
}

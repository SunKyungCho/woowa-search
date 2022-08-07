package com.woowahan.elasticsearch.shop.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.indices.IndexCreationException;

import java.io.IOException;

@RequiredArgsConstructor
public class ShopCommandRepository {

    private final String index;
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;

    /**
     * 가게 정보를 생성한다.
     * @param shopInfo 가게 정보
     * @throws IOException 예외 발생시
     */
    public void insert(ShopInfo shopInfo) {
        if (shopInfo == null) {
            throw new IllegalArgumentException("ShopInfo cannot be null");
        }

        IndexRequest request = new IndexRequest(index);
        request.id(shopInfo.getShopNumber());
        try {
            request.source(objectMapper.writeValueAsString(shopInfo), XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IndexCreationException(index, e);
        }
    }

    /**
     * 가게 정보를 업데이트한다.
     *
     * @param shop 가게 정보
     */
    public void update(ShopInfo shop) {
        UpdateRequest request = new UpdateRequest(index, shop.getShopNumber());
        try {
            request.doc(objectMapper.writeValueAsString(shop), XContentType.JSON);
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new IndexCreationException(index, e);
        }
    }

    /**
     * Shop 정보를 삭제한다.
     * @param id 삭제할 shop id
     */
    public void delete(String id) {
        DeleteRequest request = new DeleteRequest(index);
        request.id(id);
        try {
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new IndexCreationException(index, e);
        }
    }
}

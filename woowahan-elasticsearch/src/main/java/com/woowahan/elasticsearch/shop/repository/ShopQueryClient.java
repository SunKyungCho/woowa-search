package com.woowahan.elasticsearch.shop.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.error.FailedElasticsearchActionException;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import com.woowahan.elasticsearch.shop.options.FilterQuery;
import com.woowahan.elasticsearch.shop.options.SortQuery;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class ShopQueryClient {

    private final String index;
    private final RestHighLevelClient client;
    private final ObjectMapper objectMapper;
    private final List<FilterQuery> filters = new ArrayList<>();
    private final List<SortQuery> sortQueries = new ArrayList<>();

    /**
     * Id로 Shop 정보를 조회한다.
     * @param id shop ID
     * @return Shop 가게 정보
     * @throws IOException 예외 발생시
     */
    public ShopInfo searchById(String id) throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(id));
        request.source(searchSourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        if(hits.length == 0) {
            return ShopInfo.EMPTY;
        }
        return convertShopResponse(hits[0]);
    }

    /**
     * 검색어로 검색한다.
     * @param shopName 검색어
     * @return 검색 결과
     * @throws IOException 예외 발생시
     */
    public List<ShopInfo> search(String shopName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("shopName", shopName));
        if(!filters.isEmpty()) {
            filters.forEach(filter -> boolQueryBuilder.filter(filter.build()));
        }
        searchSourceBuilder.query(boolQueryBuilder);

        if(!sortQueries.isEmpty()) {
            sortQueries.forEach(sortQuery -> searchSourceBuilder.sort(sortQuery.build()));
        }

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();

        if(hits.length == 0) {
            return new ArrayList<>();
        }
        return Stream.of(hits)
                .map(this::convertShopResponse)
                .collect(Collectors.toList());
    }

    public void addFilter(FilterQuery filterQuery) {
        filters.add(filterQuery);
    }

    public void addSort(SortQuery sortQuery) {
        sortQueries.add(sortQuery);
    }

    private ShopInfo convertShopResponse(SearchHit hit) {
        try {
            return objectMapper.readValue(hit.getSourceAsString(), ShopInfo.class);
        } catch (JsonProcessingException e) {
            throw new FailedElasticsearchActionException(e);
        }
    }
}

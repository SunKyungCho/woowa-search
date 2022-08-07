package com.woowahan.app.shop.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.app.shop.domain.Shop;
import com.woowahan.elasticsearch.shop.config.ShopIndexProperties;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import com.woowahan.elasticsearch.shop.options.IsOpenFilterQuery;
import com.woowahan.elasticsearch.shop.options.ScoreSortQuery;
import com.woowahan.elasticsearch.shop.repository.ShopQueryClient;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ShopElasticsearchSearchRepository implements ShopSearchRepository {

    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;
    private final ShopIndexProperties shopIndexProperties;

    @Override
    public List<Shop> search(String shopName, String sort, String filter) {
        ShopQueryClient shopQueryClient = new ShopQueryClient(shopIndexProperties.getAlias(), restHighLevelClient, objectMapper);
        if(filter != null && filter.equals("OPEN")) {
            shopQueryClient.addFilter(new IsOpenFilterQuery());
        }
        if(sort != null && sort.equals("SCORE")) {
            shopQueryClient.addSort(new ScoreSortQuery());
        }
        List<ShopInfo> response = shopQueryClient.search(shopName);
        return response.stream()
                .map(x -> objectMapper.convertValue(x, Shop.class))
                .collect(Collectors.toList());
    }

    @Override
    public Shop searchById(String id) {
        ShopQueryClient shopQueryClient = new ShopQueryClient(shopIndexProperties.getAlias(), restHighLevelClient, objectMapper);
        ShopInfo shopInfo = shopQueryClient.searchById(id);
        return objectMapper.convertValue(shopInfo, Shop.class);
    }
}

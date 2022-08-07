package com.woowahan.elasticsearch.shop.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.repository.ShopCommandClient;
import com.woowahan.elasticsearch.shop.repository.ShopQueryClient;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ShopClientConfig {

    private final ShopIndexProperties shopIndexProperties;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        String host = shopIndexProperties.getHost();
        String[] urlInfo = host.replace("//", "").split(":");
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(urlInfo[1], Integer.parseInt(urlInfo[2]), "http"))
                        .setRequestConfigCallback(requestConfigBuilder ->
                                requestConfigBuilder.setConnectTimeout(shopIndexProperties.getConnectionTimeout())
                                        .setSocketTimeout(shopIndexProperties.getSocketTimeout())
                                        .setConnectionRequestTimeout(shopIndexProperties.getConnectionRequestTimeout())
                        )
        );
    }

    @Bean
    public ShopCommandClient shopCommandRepository(RestHighLevelClient restHighLevelClient) {
        return new ShopCommandClient(shopIndexProperties.getAlias(), restHighLevelClient, new ObjectMapper());
    }

    @Bean
    public ShopQueryClient shopQueryClient(RestHighLevelClient restHighLevelClient) {
        return new ShopQueryClient(shopIndexProperties.getAlias(), restHighLevelClient, new ObjectMapper());
    }
}

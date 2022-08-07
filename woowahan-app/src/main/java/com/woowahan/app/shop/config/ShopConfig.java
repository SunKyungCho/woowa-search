package com.woowahan.app.shop.config;


import com.woowahan.app.shop.repository.ShopElasticsearchRepository;
import com.woowahan.app.shop.repository.ShopRepository;
import com.woowahan.elasticsearch.shop.repository.ShopCommandClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopConfig {

    @Bean
    public ShopRepository shopRepository(ShopCommandClient shopCommandClient) {
        return new ShopElasticsearchRepository(shopCommandClient);
    }
}

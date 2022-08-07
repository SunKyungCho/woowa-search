package com.woowahan.elasticsearch.shop.config;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "elasticsearch.baeminshop")
public class ShopIndexProperties {

    private String alias;
    private String host;
    private int connectionTimeout;
    private int socketTimeout;
    private int connectionRequestTimeout;

}

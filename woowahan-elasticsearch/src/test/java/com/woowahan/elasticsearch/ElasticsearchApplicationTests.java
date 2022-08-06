package com.woowahan.elasticsearch;

import com.woowahan.elasticsearch.shop.config.ShopConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ShopConfig.class)
class ElasticsearchApplicationTests {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println();
    }

}

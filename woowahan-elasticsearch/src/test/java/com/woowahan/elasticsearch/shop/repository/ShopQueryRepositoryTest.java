package com.woowahan.elasticsearch.shop.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.index.Location;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import com.woowahan.elasticsearch.test.ElasticsearchHelper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Testcontainers
class ShopQueryRepositoryTest {

    @Container
    ElasticsearchContainer container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.8.1");

    private RestHighLevelClient restHighLevelClient;
    private ElasticsearchHelper elasticsearchHelper;

    @BeforeEach
    void setUp() {
        restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(container.getHost(), container.getMappedPort(9200), "http"))
        );
        elasticsearchHelper = new ElasticsearchHelper(restHighLevelClient);
    }

    @Test
    @DisplayName("가게 Id로 가게 정보를 조회한다.")
    void insert_shop_info_test() throws IOException, InterruptedException {
        //given
        ShopInfo shopInfo = new ShopInfo(
                "1234",
                "송파구 방이동",
                true,
                new Location(37.515877, 127.1171972),
                9999,
                "짜장면집"
        );
        elasticsearchHelper.insert("baemin-shop", "1234", shopInfo);
        Thread.sleep(1000);

        //when
        ShopQueryRepository shopQueryRepository = new ShopQueryRepository("baemin-shop", restHighLevelClient, new ObjectMapper());
        ShopInfo expected = shopQueryRepository.searchById("1234");

        //then
        assertThat(expected).isNotNull();
        assertThat(expected.getShopName()).isEqualTo("짜장면집");
        assertThat(expected.getAddress()).isEqualTo("송파구 방이동");
        assertThat(expected.isOpen()).isTrue();
        assertThat(expected.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(expected.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(expected.getScore()).isEqualTo(9999);
    }
}
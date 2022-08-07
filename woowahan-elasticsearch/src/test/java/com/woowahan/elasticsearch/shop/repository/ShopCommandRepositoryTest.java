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

@DisplayName("ShopCommandRepositoryTest")
@Testcontainers
class ShopCommandRepositoryTest {

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
    @DisplayName("Shop 정보를 저장한다.")
    void insert_shop_info_test() throws IOException, InterruptedException {

        //given
        elasticsearchHelper.createIndex("baemin-shop");
        ShopCommandRepository shopCommandRepository = new ShopCommandRepository(restHighLevelClient, new ObjectMapper());
        ShopInfo shopInfo = new ShopInfo(
                "1232",
                "송파구 방이동",
                true,
                new Location(37.515877, 127.1171972),
                9999,
                "짜장면집"
        );
        //when
        shopCommandRepository.insert(shopInfo);
        Thread.sleep(1000);

        //then
        ShopInfo expected = elasticsearchHelper.searchById("baemin-shop", "1232");
        assertThat(expected).isNotNull();
        assertThat(expected.getShopName()).isEqualTo("짜장면집");
        assertThat(expected.getAddress()).isEqualTo("송파구 방이동");
        assertThat(expected.isOpen()).isTrue();
        assertThat(expected.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(expected.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(expected.getScore()).isEqualTo(9999);
    }


    @Test
    @DisplayName("Shop 정보를 수정 할 수 있어야 한다")
    void update_shop_info_test() throws IOException, InterruptedException {

        //given
        elasticsearchHelper.createIndex("baemin-shop");
        elasticsearchHelper.insert("baemin-shop", "100", new ShopInfo(
                "1232",
                "송파구 방이동",
                true,
                new Location(37.515877, 127.1171972),
                9999,
                "짜장면집"
        ));

        //when
        ShopCommandRepository shopCommandRepository = new ShopCommandRepository(restHighLevelClient, new ObjectMapper());
        shopCommandRepository.update(new ShopInfo(
                "100",
                "송파구 방이동",
                true,
                new Location(37.515877, 127.1171972),
                100,
                "짬뽕집"
        ));
        Thread.sleep(1000);

        //then
        ShopInfo shopInfo1 = elasticsearchHelper.searchById("baemin-shop", "100");
        assertThat(shopInfo1).isNotNull();
        assertThat(shopInfo1.getShopName()).isEqualTo("짬뽕집");
        assertThat(shopInfo1.getAddress()).isEqualTo("송파구 방이동");
        assertThat(shopInfo1.isOpen()).isTrue();
        assertThat(shopInfo1.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(shopInfo1.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(shopInfo1.getScore()).isEqualTo(100);
    }

}

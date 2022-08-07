package com.woowahan.elasticsearch.shop.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowahan.elasticsearch.shop.index.Location;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import com.woowahan.elasticsearch.shop.options.IsOpenFilterQuery;
import com.woowahan.elasticsearch.shop.options.ScoreSortQuery;
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
import java.util.List;

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
    void search_by_id_shop_info_test() throws IOException, InterruptedException {
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
        assertThat(expected.getIsOpen()).isTrue();
        assertThat(expected.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(expected.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(expected.getScore()).isEqualTo(9999);
    }

    @Test
    @DisplayName("가게명으로 가게 정보를 검색한다.")
    void search_shop_info_test() throws IOException, InterruptedException {
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
        List<ShopInfo> shopInfos = shopQueryRepository.search("짜장면집");

        //then
        assertThat(shopInfos).isNotNull();
        assertThat(shopInfos.size()).isEqualTo(1);
        ShopInfo expected = shopInfos.get(0);
        assertThat(expected).isNotNull();
        assertThat(expected.getShopName()).isEqualTo("짜장면집");
        assertThat(expected.getAddress()).isEqualTo("송파구 방이동");
        assertThat(expected.getIsOpen()).isTrue();
        assertThat(expected.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(expected.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(expected.getScore()).isEqualTo(9999);
    }

    @Test
    @DisplayName("오픈된 가게만 가게 정보를 검색 할 수 있다")
    void filter_open_shop_info_test() throws IOException, InterruptedException {
        //given
        elasticsearchHelper.createIndex("baemin-shop");
        elasticsearchHelper.insert("baemin-shop", "1234", new ShopInfo(
                "1234",
                "송파구 방이동",
                true,
                new Location(37.515877, 127.1171972),
                10,
                "짜장면집"
        ));
        elasticsearchHelper.insert("baemin-shop", "5678", new ShopInfo(
                "5678",
                "종로구 삼청동",
                false,
                new Location(37.577943, 126.985117),
                9,
                "짜장면집"
        ));
        Thread.sleep(1000);

        //when
        ShopQueryRepository shopQueryRepository = new ShopQueryRepository("baemin-shop", restHighLevelClient, new ObjectMapper());
        shopQueryRepository.addFilter(new IsOpenFilterQuery());
        List<ShopInfo> shopInfos = shopQueryRepository.search("짜장면집");

        //then
        assertThat(shopInfos).isNotNull();
        assertThat(shopInfos.size()).isEqualTo(1);
        ShopInfo expected = shopInfos.get(0);
        assertThat(expected).isNotNull();
        assertThat(expected.getShopName()).isEqualTo("짜장면집");
        assertThat(expected.getAddress()).isEqualTo("송파구 방이동");
        assertThat(expected.getIsOpen()).isTrue();
        assertThat(expected.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(expected.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(expected.getScore()).isEqualTo(10);
    }



    @Test
    @DisplayName("score 점수와 가게이름의 길이를 합친 점수로 랭킹 한다")
    void sort_score_shop_info_test() throws IOException, InterruptedException {
        //given
        elasticsearchHelper.createIndex("baemin-shop");
        elasticsearchHelper.insert("baemin-shop", "1234", new ShopInfo(
                "1234",
                "송파구 방이동",
                true,
                new Location(37.515877, 127.1171972),
                9,
                "짜장면집 맛집"
        ));
        elasticsearchHelper.insert("baemin-shop", "5678", new ShopInfo(
                "5678",
                "종로구 삼청동",
                true,
                new Location(37.577943, 126.985117),
                10,
                "짜장면집"
        ));
        Thread.sleep(1000);

        //when
        ShopQueryRepository shopQueryRepository = new ShopQueryRepository("baemin-shop", restHighLevelClient, new ObjectMapper());
        shopQueryRepository.addSort(new ScoreSortQuery());
        List<ShopInfo> shopInfos = shopQueryRepository.search("짜장면집");

        //then
        assertThat(shopInfos).isNotNull();
        assertThat(shopInfos.size()).isEqualTo(2);
        ShopInfo expected = shopInfos.get(0);
        assertThat(expected).isNotNull();
        assertThat(expected.getShopName()).isEqualTo("짜장면집 맛집");
        assertThat(expected.getAddress()).isEqualTo("송파구 방이동");
        assertThat(expected.getIsOpen()).isTrue();
        assertThat(expected.getLocation().getLat()).isEqualTo(37.515877);
        assertThat(expected.getLocation().getLon()).isEqualTo(127.1171972);
        assertThat(expected.getScore()).isEqualTo(9);
    }
}
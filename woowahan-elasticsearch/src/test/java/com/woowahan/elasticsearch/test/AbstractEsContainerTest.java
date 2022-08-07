package com.woowahan.elasticsearch.test;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

public abstract class AbstractEsContainerTest {


    protected static ElasticsearchContainer container;

    static {
         container = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.8.1");
         container.start();
    }
}

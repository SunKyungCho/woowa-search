package com.woowahan.elasticsearch.shop.options;

import org.elasticsearch.index.query.QueryBuilder;

public interface FilterQuery {

    QueryBuilder build();

}
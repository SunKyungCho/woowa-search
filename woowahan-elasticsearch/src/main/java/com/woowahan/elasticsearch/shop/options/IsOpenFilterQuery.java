package com.woowahan.elasticsearch.shop.options;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class IsOpenFilterQuery implements FilterQuery {
    @Override
    public QueryBuilder build() {
        return QueryBuilders.termQuery("isOpen", true);
    }
}

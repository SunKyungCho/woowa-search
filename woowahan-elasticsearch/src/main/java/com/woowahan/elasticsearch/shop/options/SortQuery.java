package com.woowahan.elasticsearch.shop.options;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;

public interface SortQuery {


    SortBuilder<?> build();
}

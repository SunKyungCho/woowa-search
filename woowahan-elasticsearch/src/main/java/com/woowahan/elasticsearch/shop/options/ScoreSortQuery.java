package com.woowahan.elasticsearch.shop.options;

import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.*;

public class ScoreSortQuery implements SortQuery {

    @Override
    public SortBuilder<?> build() {
        Script script = new Script("doc['score'].value + doc['shopName.keyword'].value.length()");
        return SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER)
                .order(SortOrder.DESC);
    }
}

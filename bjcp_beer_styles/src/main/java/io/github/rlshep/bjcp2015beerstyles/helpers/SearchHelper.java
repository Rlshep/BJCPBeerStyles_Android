package io.github.rlshep.bjcp2015beerstyles.helpers;

import java.util.Arrays;
import java.util.LinkedHashSet;

import io.github.rlshep.bjcp2015beerstyles.BjcpActivity;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;

public class SearchHelper {

    public String[] getSearchSuggestions(BjcpActivity activity) {
        BjcpDataHelper bjcpDataHelper = BjcpDataHelper.getInstance(activity);
        LinkedHashSet<String> searchKeywords = new LinkedHashSet<String>();

        searchKeywords.addAll(bjcpDataHelper.getAllCategoryNames());
        searchKeywords.addAll(bjcpDataHelper.getAllSynonyms());
        searchKeywords.addAll(bjcpDataHelper.getAllTags());

        String[] stringArray = Arrays.copyOf(searchKeywords.toArray(), searchKeywords.toArray().length, String[].class);

        return stringArray;
    }

}

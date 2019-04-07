package io.github.rlshep.bjcp2015beerstyles.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.BjcpActivity;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;

public class SearchHelper {

    public String[] getSearchSuggestions(BjcpActivity activity) {
        BjcpDataHelper bjcpDataHelper = BjcpDataHelper.getInstance(activity);
        List<String> searchKeywords = new ArrayList<>();

        searchKeywords.addAll(bjcpDataHelper.getAllCategoryNames());
        searchKeywords.addAll(bjcpDataHelper.getAllSynonyms());

        String[] stringArray = Arrays.copyOf(searchKeywords.toArray(), searchKeywords.toArray().length, String[].class);

        return stringArray;
    }

}

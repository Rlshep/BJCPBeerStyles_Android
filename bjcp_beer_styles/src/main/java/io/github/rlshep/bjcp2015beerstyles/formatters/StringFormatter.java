package io.github.rlshep.bjcp2015beerstyles.formatters;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringFormatter {
    public static final Map<Character, Character> specialCharacters = Collections.unmodifiableMap(
            new HashMap<Character, Character>() {{
                put('a', 'á');
                put('a', 'ä');
                put('e', 'è');
                put('e', 'é');
                put('o', 'ö');
                put('o', 'ó');
                put('u', 'ú');
                put('u', 'ü');
            }});

    public static String getHighlightedText(String text, String toHighlight) {
        String formatted;

        if (!StringUtils.isEmpty(toHighlight)) {
            formatted = getFormattedText(text, toHighlight);
        } else {
            formatted = text;
        }

        return formatted;
    }

    private static String getFormattedText(String text, String query) {
        StringBuilder formatted = new StringBuilder();
        String subString = "";
        int queryLength = query.length();
        int i = 0;

        while (i < text.length()) {
            if ((i + queryLength) < text.length()) {
                subString = text.substring(i, i + queryLength);
            }

            if (((i + queryLength) < text.length()) && query.equalsIgnoreCase(subString)) {
                formatted.append("<font color='red'>");
                formatted.append(subString);
                formatted.append("</font>");
                i += (queryLength - 1);
            } else {
                formatted.append(text.charAt(i));
            }
            i++;
        }

        return formatted.toString();
    }

    public static String addDoubleSingleQuotes(String searchedText) {
        return searchedText.replaceAll("'", "''");
    }

    public static String removeDoubleSingleQuotes(String searchedText) {
        return searchedText.replaceAll("''", "'");
    }

    public static List<String> convertSpecialCharacters(String keyword) {
        List<String> keywords = new ArrayList<>();

        for (Character c : keyword.toCharArray()) {
            //TODO: FINISH
        }

        return keywords;
    }
}

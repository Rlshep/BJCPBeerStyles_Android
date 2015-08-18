package io.github.rlshep.bjcp2015beerstyles.formatters;

import org.apache.commons.lang.StringUtils;

public class StringFormatter {
    private final static int MAX_TITLE_SIZE = 30;

    public static String getHighlightedText(String text, String toHighlight) {
        String formatted;

        if (!StringUtils.isEmpty(toHighlight)) {
            formatted = getFormattedText(text, toHighlight);
        }
        else {
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
            }
            else {
                formatted.append(text.charAt(i));
            }
            i++;
        }

        return formatted.toString();
    }

    //TODO: Fix title based upon screen size.
    // This isn't perfect but will do for now.
//    public static Spanned getFormattedTitle(String text) {
//        Spanned title;
//        getHtmlTagSize(text);
//
//        if (MAX_TITLE_SIZE < (text.length() - getHtmlTagSize(text))) {
//            title = Html.fromHtml("<small>" + text + "</small>");
//        }
//        else {
//            title = Html.fromHtml(text);
//        }
//
//        return title;
//    }
//
//    private static int getHtmlTagSize(String text) {
//        String allTags = "";
//        Pattern p = Pattern.compile("<(\\S+)>");
//        Matcher m = p.matcher(text);
//        int allTagsLength = 0;
//
//        // if we find a match, get the group
//        while (m.find()) {
//            allTags += m.group(1);
//            allTagsLength += (allTags.length() * 2); //Add beginning and end tag text
//            allTagsLength += 5; //Brackets of both tags < > </
//        }
//
//        return allTagsLength;
//    }
}

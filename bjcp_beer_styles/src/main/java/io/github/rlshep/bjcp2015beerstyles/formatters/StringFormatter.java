package io.github.rlshep.bjcp2015beerstyles.formatters;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
    private final static int MAX_TITLE_SIZE = 30;

    public static String getHighlightedText(String text, String toHighlight) {
        String formatted;

        if (!StringUtils.isEmpty(toHighlight)) {
            //TODO: Replaces case with searchText.
            formatted = text.replaceAll("(?i)" + toHighlight, "<font color='red'>" + toHighlight + "</font>");
        }
        else {
            formatted = text;
        }

        return formatted;
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

    private static int getHtmlTagSize(String text) {
        String allTags = "";
        Pattern p = Pattern.compile("<(\\S+)>");
        Matcher m = p.matcher(text);
        int allTagsLength = 0;

        // if we find a match, get the group
        while (m.find()) {
            allTags += m.group(1);
            allTagsLength += (allTags.length() * 2); //Add beginning and end tag text
            allTagsLength += 5; //Brackets of both tags < > </
        }

        return allTagsLength;
    }
}

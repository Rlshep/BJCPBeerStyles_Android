package io.github.rlshep.bjcp2015beerstyles.formatters;

import android.text.Html;
import android.text.Spanned;

import org.apache.commons.lang.StringUtils;

public class StringFormatter {
    private static int MAX_TITLE_SIZE = 25;

    public static String getHighlightedText(String text, String toHighlight) {
        String formatted = "";

        if (!StringUtils.isEmpty(toHighlight)) {
            //TODO: Replaces case with searchText.
            formatted = text.replaceAll("(?i)" + toHighlight, "<font color='red'>" + toHighlight + "</font>");
        } else {
            formatted = text;
        }

        return formatted;
    }

    // This isn't perfect but will do for now.
    public static Spanned getFormattedTitle(String text) {
        Spanned title;

        if (MAX_TITLE_SIZE < text.length()) {
            title = Html.fromHtml("<small>" + text + "</small>");
        } else {
            title = Html.fromHtml(text);
        }

        return title;
    }
}

package io.github.rlshep.bjcp2015beerstyles.formatters;

import org.junit.Test;

public class StringFormatterTest {
    @Test
    public void addDoubleSingleQuotes_returns_single_quote() {
        assert(!"Fuller's".equals(StringFormatter.addDoubleSingleQuotes("Fuller's")));
        assert("Fuller''s".equals(StringFormatter.addDoubleSingleQuotes("Fuller's")));
    }


    @Test
    public void removeDoubleSingleQuotes_returns_single_quote() {
        assert("Fuller's".equals(StringFormatter.removeDoubleSingleQuotes("Fuller's")));
        assert(!"Fuller''s".equals(StringFormatter.removeDoubleSingleQuotes("Fuller's")));
    }

    @Test
    public void  convertSpecialCharacters_returns_o() {
        String actual = StringFormatter.convertSpecialCharacters("Kolsch");
        assert(actual.contains("Kölsch"));
        assert(actual.contains("Kólsch"));
    }
}

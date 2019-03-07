package io.github.rlshep.bjcp2015beerstyles.formatters;

import org.junit.Test;

import java.util.List;

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
        List<String> actual = StringFormatter.convertSpecialCharacters("Kolsch");
        assert(actual.contains("Kölsch"));
        assert(actual.contains("Kólsch"));
    }

    @Test
    public void  convertSpecialCharacters_returns_u() {
        List<String> actual = StringFormatter.convertSpecialCharacters("Fuchschen");
        assert(actual.contains("Füchschen"));
        assert(actual.contains("Fúchschen"));
    }

    @Test
    public void  convertSpecialCharacters_returns_a() {
        List<String> actual = StringFormatter.convertSpecialCharacters("Brau");
        assert(actual.contains("Bräu"));
        assert(actual.contains("Bráu"));
    }

    @Test
    public void  convertSpecialCharacters_returns_e() {
        List<String> actual = StringFormatter.convertSpecialCharacters("Biere");
        assert(actual.contains("Bière"));
        assert(actual.contains("Biére"));
    }
}

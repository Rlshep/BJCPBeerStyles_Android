package io.github.rlshep.bjcp2015beerstyles.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {

    @Test
    public void getTruncatedCategoryCodeReturnsNoChangeIfNoDash() {
        Category category = new Category();
        category.setCategoryCode("21C");

        assertEquals("21C", category.getTruncatedCategoryCode());
    }

    @Test
    public void getTruncatedCategoryCodeReturnsNothingAfterDash() {
        Category category = new Category();
        category.setCategoryCode("21C-black");

        assertEquals("21C", category.getTruncatedCategoryCode());
    }
}

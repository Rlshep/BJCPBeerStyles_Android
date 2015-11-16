package io.github.rlshep.bjcp2015beerstyles.domain;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public final static String LANG_ENGLISH = "English";
    public final static double CURRENT_REVISION = 2015;

    private long id;
    private long parentId;
    private String category;
    private String name;
    private double revision;
    private String language;
    private int orderNumber;
    private boolean bookmarked;
    private VitalStatistics vitalStatistics;
    private List<Section> sections = new ArrayList<Section>();
    private List<Category> childCategories = new ArrayList<Category>();

    public Category() {
        this.language = LANG_ENGLISH;
        this.revision = CURRENT_REVISION;
    }

    public Category(String category) {
        this.category = category;
        this.language = LANG_ENGLISH;
        this.revision = CURRENT_REVISION;
    }

    public Category(long id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRevision() {
        return revision;
    }

    public void setRevision(double revision) {
        this.revision = revision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public VitalStatistics getVitalStatistics() {
        return vitalStatistics;
    }

    public void setVitalStatistics(VitalStatistics vitalStatistics) {
        this.vitalStatistics = vitalStatistics;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }
}

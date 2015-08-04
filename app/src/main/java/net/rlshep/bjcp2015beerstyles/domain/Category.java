package net.rlshep.bjcp2015beerstyles.domain;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final static String LANG_ENGLISH = "English";
    private final static double CURRENT_REVISION = 2015;

    private long _id;
    private String _category;
    private String _name;
    private double _revision;
    private String _language;
    private List<SubCategory> _subCategories = new ArrayList<SubCategory>();
    private List<Section> _sections = new ArrayList<Section>();
    private int _orderNumber;

    public Category() {
        this._language = LANG_ENGLISH;
        this._revision = CURRENT_REVISION;
    }

    public Category(String category) {
        this._category = category;
        this._language = LANG_ENGLISH;
        this._revision = CURRENT_REVISION;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public double get_revision() {
        return _revision;
    }

    public void set_revision(double _revision) {
        this._revision = _revision;
    }

    public String get_language() {
        return _language;
    }

    public void set_language(String _language) {
        this._language = _language;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public List<SubCategory> get_subCategories() {
        return _subCategories;
    }

    public void set_subCategories(List<SubCategory> _subCategories) {
        this._subCategories = _subCategories;
    }

    public List<Section> get_sections() {
        return _sections;
    }

    public void set_sections(List<Section> _sections) {
        this._sections = _sections;
    }

    public int get_orderNumber() {
        return _orderNumber;
    }

    public void set_orderNumber(int _orderNumber) {
        this._orderNumber = _orderNumber;
    }
}

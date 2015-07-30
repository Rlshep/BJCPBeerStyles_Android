package net.rlshep.bjcp2015beerstyles.domain;

import java.util.ArrayList;
import java.util.List;

public class SubCategory {

    private long _id;
    private long _categoryId;
    private String _subCategory;
    private String _name;
    private boolean _tapped;
    private List<Section> _sections = new ArrayList<Section>();;
    private VitalStatistics _vitalStatistics;

    public SubCategory() {
        this._tapped = false;
    }

    public SubCategory(String _subCategory) {
        this._subCategory = _subCategory;
        this._tapped = false;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_subCategory() {
        return _subCategory;
    }

    public void set_subCategory(String _subCategory) {
        this._subCategory = _subCategory;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public boolean is_tapped() {
        return _tapped;
    }

    public void set_tapped(boolean _tapped) {
        this._tapped = _tapped;
    }

    public List<Section> get_sections() {
        return _sections;
    }

    public void set_sections(List<Section> _sections) {
        this._sections = _sections;
    }

    public VitalStatistics get_vitalStatistics() {
        return _vitalStatistics;
    }

    public void set_vitalStatistics(VitalStatistics _vitalStatistics) {
        this._vitalStatistics = _vitalStatistics;
    }

    public long get_categoryId() {
        return _categoryId;
    }

    public void set_categoryId(long _categoryId) {
        this._categoryId = _categoryId;
    }
}

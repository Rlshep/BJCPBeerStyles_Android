package net.rlshep.bjcp2015beerstyles.domain;

public class Section {
    private long _id;
    private long _categoryId;
    private long _subCategoryId;
    private String _header;
    private String _body;
    private long _orderNumber;

    public Section() {
    }

    public Section(String _header, long _orderNumber) {
        this._header = _header;
        this._orderNumber = _orderNumber;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_header() {
        return _header;
    }

    public void set_header(String _header) {
        this._header = _header;
    }

    public String get_body() {
        return _body;
    }

    public void set_body(String _body) {
        this._body = _body;
    }

    public long get_orderNumber() {
        return _orderNumber;
    }

    public void set_orderNumber(long _orderNumber) {
        this._orderNumber = _orderNumber;
    }

    public long get_subCategoryId() {
        return _subCategoryId;
    }

    public void set_subCategoryId(long _subCategoryId) {
        this._subCategoryId = _subCategoryId;
    }

    public long get_categoryId() {
        return _categoryId;
    }

    public void set_categoryId(long _categoryId) {
        this._categoryId = _categoryId;
    }

}

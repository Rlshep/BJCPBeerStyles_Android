package net.rlshep.bjcp2015beerstyles.domain;

public class Products {
    private long _id;
    private String _productName;

    public Products() {
    }

    public Products(String _productName) {
        this._productName = _productName;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_productName() {
        return _productName;
    }

    public void set_productName(String _productName) {
        this._productName = _productName;
    }
}

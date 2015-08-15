package io.github.rlshep.bjcp2015beerstyles.domain;

public class SearchResult {
    private long _id;
    private String _query;
    private long _resultId;
    private String _tableName;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_query() {
        return _query;
    }

    public void set_query(String _query) {
        this._query = _query;
    }

    public long get_resultId() {
        return _resultId;
    }

    public void set_resultId(long _resultId) {
        this._resultId = _resultId;
    }

    public String get_TableName() {
        return _tableName;
    }

    public void set_TableName(String tableName) {
        this._tableName = tableName;
    }
}

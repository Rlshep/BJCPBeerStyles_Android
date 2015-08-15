DROP TABLE fts_search;
CREATE VIRTUAL TABLE fts_search USING fts4(result_id, table_name, language, revision, body);
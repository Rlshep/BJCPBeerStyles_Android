DELETE
FROM fts_search;

-- CATEGORY NAME
INSERT INTO fts_search (
	result_id
	,table_name
	,LANGUAGE
	,revision
	,body
	)
SELECT C._ID
	,'CATEGORY'
	,C.LANGUAGE
	,C.revision
	,C.NAME
FROM CATEGORY C
WHERE C.LANGUAGE = 'English'
	AND C.revision = 2015;

-- SUBCATEGORY NAME
INSERT INTO fts_search (result_id
	,table_name
	,LANGUAGE
	,revision
	,body
	)
SELECT sc._id
	,'SUB_CATEGORY'
	,c.LANGUAGE
	,c.revision
	,sc.NAME
FROM SUB_CATEGORY sc
JOIN CATEGORY c ON c._id = sc.category_id
WHERE C.LANGUAGE = 'English'
	AND C.revision = 2015;

-- CATEGORY SECTIONS
INSERT INTO fts_search (result_id
	,table_name
	,LANGUAGE
	,revision
	,body
	)
SELECT c._id
	,'CATEGORY'
	,c.LANGUAGE
	,c.revision
	,s.body
FROM CATEGORY C
JOIN SECTION S ON S.CATEGORY_ID = C._ID
WHERE C.LANGUAGE = 'English'
	AND C.revision = 2015;

-- SUBCATEGORY SECTIONS
INSERT INTO fts_search (result_id
	,table_name
	,LANGUAGE
	,revision
	,body
	)
SELECT SC._id
	,'SUB_CATEGORY'
	,c.LANGUAGE
	,c.revision
	,s.body
FROM CATEGORY C
JOIN SUB_CATEGORY SC ON SC.CATEGORY_ID = C._ID
JOIN SECTION S ON S.SUB_CATEGORY_ID = SC._ID
WHERE C.LANGUAGE = 'English'
	AND C.revision = 2015;

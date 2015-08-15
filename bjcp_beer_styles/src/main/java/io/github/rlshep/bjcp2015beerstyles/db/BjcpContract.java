package io.github.rlshep.bjcp2015beerstyles.db;

public class BjcpContract {
    public static final String TABLE_CATEGORY = "CATEGORY";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAT = "category";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_REVISION = "revision";
    public static final String COLUMN_LANG = "language";

    public static final String TABLE_SUB_CATEGORY = "SUB_CATEGORY";
    public static final String COLUMN_CAT_ID = "category_id";
    public static final String COLUMN_SUB_CAT = "sub_category";
    public static final String COLUMN_TAPPED = "tapped";

    public static final String TABLE_SECTION = "SECTION";
    public static final String COLUMN_SUB_CAT_ID = "sub_category_id";
    public static final String COLUMN_HEADER = "header";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_ORDER = "order_number";

    public static final String TABLE_VITALS = "VITAL_STATISTICS";
    public static final String COLUMN_OG_START = "og_start";
    public static final String COLUMN_OG_END = "og_end";
    public static final String COLUMN_FG_START = "fg_start";
    public static final String COLUMN_FG_END = "fg_end";
    public static final String COLUMN_IBU_START = "ibu_start";
    public static final String COLUMN_IBU_END = "ibu_end";
    public static final String COLUMN_SRM_START = "srm_start";
    public static final String COLUMN_SRM_END = "srm_end";
    public static final String COLUMN_ABV_START = "abv_start";
    public static final String COLUMN_ABV_END = "abv_end";

    public static final String TABLE_META = "android_metadata";
    public static final String COLUMN_LOCALE = "locale";

    public static final String TABLE_SEARCH_RESULTS = "SEARCH_RESULTS";
    public static final String COLUMN_RESULT_ID = "result_id";
    public static final String COLUMN_TABLE_NAME = "table_name";
    public static final String COLUMN_QUERY = "query";

    public static final String XML_ID = "id";
    public static final String XML_SUBCATEGORY = "subcategory";
    public static final String XML_STATS = "stats";
    public static final String XML_OG = "og";
    public static final String XML_FG = "fg";
    public static final String XML_IBU = "ibu";
    public static final String XML_SRM = "srm";
    public static final String XML_ABV = "abv";
    public static final String XML_LOW = "low";
    public static final String XML_HIGH = "high";
    public static final String XML_EXCEPTIONS = "exceptions";
}

package io.github.rlshep.bjcp2015beerstyles.db;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.commons.lang.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.domain.Category;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.SubCategory;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

public class LoadDataFromXML {
    private static final String XML_FILE_NAME = "styleguide-2015-min.xml";
    private static final String[] ALLOWED_SECTIONS = {"notes", "impression", "aroma", "appearance", "flavor", "mouthfeel", "comments", "history",
            "ingredients", "comparison", "examples", "tags", "entryinstructions", "exceptions"};
    private final List<String> allowedSections = Arrays.asList(ALLOWED_SECTIONS);
    private final static HashMap<String, String> VALUES_TO_CONVERT = new HashMap<String, String>();
    static {
        VALUES_TO_CONVERT.put("entryinstructions", "Entry Instructions");
        VALUES_TO_CONVERT.put("<ul>", "");
        VALUES_TO_CONVERT.put("</ul>", "");
        VALUES_TO_CONVERT.put("<li>", "<br>");
        VALUES_TO_CONVERT.put("<li>", "<br>");
    }

    public List<Category> loadXmlFromFile(Context context) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open(XML_FILE_NAME);
        xpp.setInput(is, null);

        return loadCategoriesFromXml(xpp);
    }

    private List<Category> loadCategoriesFromXml(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<Category> categories = new ArrayList<Category>();
        int orderNumber = 0;
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && BjcpContract.COLUMN_CAT.equals(xpp.getName())) {
                categories.add(createCategory(xpp, orderNumber));
                orderNumber++;
            }

            eventType = xpp.next();
        }

        return categories;
    }

    private Category createCategory(XmlPullParser xpp, int orderNumber) throws XmlPullParserException, IOException {
        Category category = new Category(xpp.getAttributeValue(null, BjcpContract.XML_ID));
        List<Section> sections = new ArrayList<Section>();
        List<SubCategory> subCategories = new ArrayList<SubCategory>();
        int sectionOrder = 0;
        int subCatOrder = 0;

       while (isNotTheEnd(xpp,BjcpContract.COLUMN_CAT)) {
            if (isStartTag(xpp, BjcpContract.COLUMN_NAME)) {
                category.set_name(getNextText(xpp));
            } else if (isStartTag(xpp, BjcpContract.XML_SUBCATEGORY)) {
                subCategories.add(createSubCategory(xpp, subCatOrder));
                subCatOrder++;
            } else if (isSection(xpp)) {
                sections.add(createSection(xpp, sectionOrder));
                sectionOrder++;
            }
        }

        category.set_sections(sections);
        category.set_subCategories(subCategories);
        category.set_orderNumber(orderNumber);

        return category;
    }

    private SubCategory createSubCategory(XmlPullParser xpp, int orderNumber) throws XmlPullParserException, IOException {
        SubCategory subCategory = new SubCategory(xpp.getAttributeValue(null, BjcpContract.XML_ID), orderNumber);
        List<Section> sections = new ArrayList<Section>();
        int sectionOrder = 1;

        while (isNotTheEnd(xpp,BjcpContract.XML_SUBCATEGORY)){
           if (isStartTag(xpp, BjcpContract.COLUMN_NAME)) {
               subCategory.set_name(getNextText(xpp));
           } else if (isStartTag(xpp, BjcpContract.XML_STATS)) {
               VitalStatistics vitalStatistics = createVitalStatistics(xpp);

               // If statistics is an exception then add to sections.
               if (null == vitalStatistics) {
                   sections.add(createSection(xpp, sectionOrder));
                   sectionOrder++;
               }

               subCategory.set_vitalStatistics(vitalStatistics);
           } else if (isSection(xpp)) {
               sections.add(createSection(xpp, sectionOrder));
               sectionOrder++;
            }
        }

        subCategory.set_sections(sections);

        return subCategory;
    }

    private boolean isSection(XmlPullParser xpp) throws XmlPullParserException, IOException {
        return xpp.getEventType() == XmlPullParser.START_TAG && allowedSections.contains(xpp.getName());
    }

    private Section createSection(XmlPullParser xpp, int orderNumber) throws XmlPullParserException, IOException {
        String name = xpp.getName();
        String bodyText = "";
        Section section = new Section(convertValue(name),  orderNumber);

        while (isNotTheEnd(xpp, name)){
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                bodyText += convertValue(" <" + xpp.getName() + "> ");
            } else if (xpp.getEventType() == XmlPullParser.END_TAG) {
                bodyText += convertValue(" </" + xpp.getName() + "> ");
            } else {
                bodyText += xpp.getText();
            }
        }

        bodyText = bodyText.replaceAll("[\n\r]", "").trim();
        section.set_body(bodyText);

        return section;
    }

    private String convertValue(String value) {
        String converted = value.trim();

        if (VALUES_TO_CONVERT.containsKey(converted)) {
            converted = VALUES_TO_CONVERT.get(converted);
        } else {
            converted = StringUtils.capitalize(value);
        }

        return converted;
    }

    private VitalStatistics createVitalStatistics(XmlPullParser xpp) throws XmlPullParserException, IOException {
        VitalStatistics vitalStatistics = new VitalStatistics();

        while (isNotTheEnd(xpp, BjcpContract.XML_STATS)){
            if (isStartTag(xpp, BjcpContract.XML_EXCEPTIONS)) {
                return null;    //TODO: I feel dirty
            }

            if (isStartTag(xpp, BjcpContract.XML_OG)) {
                vitalStatistics.set_ogStart(getNextByName(xpp, BjcpContract.XML_LOW));
                vitalStatistics.set_ogEnd(getNextByName(xpp, BjcpContract.XML_HIGH));
            } else if (isStartTag(xpp, BjcpContract.XML_FG)) {
                vitalStatistics.set_fgStart(getNextByName(xpp, BjcpContract.XML_LOW));
                vitalStatistics.set_fgEnd(getNextByName(xpp, BjcpContract.XML_HIGH));
            } else if (isStartTag(xpp, BjcpContract.XML_IBU)) {
                vitalStatistics.set_ibuStart(getNextByName(xpp, BjcpContract.XML_LOW));
                vitalStatistics.set_ibuEnd(getNextByName(xpp, BjcpContract.XML_HIGH));
            } else if (isStartTag(xpp, BjcpContract.XML_SRM)) {
                vitalStatistics.set_srmStart(getNextByName(xpp, BjcpContract.XML_LOW));
                vitalStatistics.set_srmEnd(getNextByName(xpp, BjcpContract.XML_HIGH));
            } else if (isStartTag(xpp, BjcpContract.XML_ABV)) {
                vitalStatistics.set_abvStart(getNextByName(xpp, BjcpContract.XML_LOW));
                vitalStatistics.set_abvEnd(getNextByName(xpp, BjcpContract.XML_HIGH));
            }
        }

        return vitalStatistics;
    }

    private boolean isStartTag(XmlPullParser xpp, String name) throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.START_TAG && name.equals(xpp.getName());
    }

    private String getNextText(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String text = "";
        int eventType = xpp.next();

        if (eventType != XmlPullParser.END_DOCUMENT
            && eventType == XmlPullParser.TEXT) {
            text = xpp.getText();
        }

        return text;
    }
    
    private String getNextByName(XmlPullParser xpp, String name) throws IOException, XmlPullParserException {
        String text = "";

        while (isNotTheEnd(xpp,name)) {
            if (isStartTag(xpp, name)) {
                text = getNextText(xpp);
            }
        }

        return text;
    }
    
    private boolean isNotTheEnd(XmlPullParser xpp, String name) throws IOException, XmlPullParserException {
        int eventType = xpp.next();

        return eventType != XmlPullParser.END_DOCUMENT && !(eventType == XmlPullParser.END_TAG && name.equals(xpp.getName()));
    }
}

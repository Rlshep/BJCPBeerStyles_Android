package net.rlshep.bjcp2015beerstyles.db;

import android.content.Context;
import android.content.res.AssetManager;

import net.rlshep.bjcp2015beerstyles.domain.Category;
import net.rlshep.bjcp2015beerstyles.domain.Section;
import net.rlshep.bjcp2015beerstyles.domain.SubCategory;
import net.rlshep.bjcp2015beerstyles.domain.VitalStatistics;

import org.apache.commons.lang.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadDataFromXML {
    private static final String[] ALLOWED_SECTIONS = {"notes", "impression", "aroma", "appearance", "flavor", "mouthfeel", "comments", "history",
            "ingredients", "comparison", "examples", "tags", "entryinstructions", "exceptions"};
    private final List<String> allowedSections = Arrays.asList(ALLOWED_SECTIONS);

    public List<Category> loadXmlFromFile(Context context) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        AssetManager assetManager = context.getAssets();
        InputStream is = assetManager.open("styleguide-2015-min.xml");
        xpp.setInput(is, null);

        return loadCategoriesFromXml(xpp);
    }

    private List<Category> loadCategoriesFromXml(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<Category> categories = new ArrayList<Category>();
        int orderNumber = 0;
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && "category".equals(xpp.getName())) {
                categories.add(createCategory(xpp, orderNumber));
            }

            eventType = xpp.next();
        }

        return categories;
    }

    private Category createCategory(XmlPullParser xpp, int orderNumber) throws XmlPullParserException, IOException {
        Category category = new Category(xpp.getAttributeValue(null, "id"));
        List<Section> sections = new ArrayList<Section>();
        List<SubCategory> subCategories = new ArrayList<SubCategory>();
        int sectionOrder = 0;

       while (isNotTheEnd(xpp,"category")) {
            if (isStartTag(xpp, "name")) {
                category.set_name(getNextText(xpp));
            } else if (isStartTag(xpp, "subcategory")) {
                subCategories.add(createSubCategory(xpp));
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

    private SubCategory createSubCategory(XmlPullParser xpp) throws XmlPullParserException, IOException {
        SubCategory subCategory = new SubCategory(xpp.getAttributeValue(null, "id"));
        List<Section> sections = new ArrayList<Section>();
        int orderNumber = 1;

        while (isNotTheEnd(xpp,"subcategory")){
           if (isStartTag(xpp, "name")) {
               subCategory.set_name(getNextText(xpp));
           } else if (isStartTag(xpp, "stats")) {
               VitalStatistics vitalStatistics = createVitalStatistics(xpp);

               // If statistics is an exception then add to sections.
               if (null == vitalStatistics) {
                   sections.add(createSection(xpp, orderNumber));
                   orderNumber++;
               }

               subCategory.set_vitalStatistics(vitalStatistics);
           } else if (isSection(xpp)) {
               sections.add(createSection(xpp, orderNumber));
               orderNumber++;
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
        String bodyText = new String();
        Section section = new Section(StringUtils.capitalize(name),  orderNumber);

        while (isNotTheEnd(xpp, name)){
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                bodyText += "<" + xpp.getName() + ">";
            } else if (xpp.getEventType() == XmlPullParser.END_TAG) {
                bodyText += "</" + xpp.getName() + ">";
            } else {
                bodyText += xpp.getText();
            }
        }

        bodyText = bodyText.replaceAll("[\n\r]", "").trim();
        section.set_body(bodyText);

        return section;
    }

    private VitalStatistics createVitalStatistics(XmlPullParser xpp) throws XmlPullParserException, IOException {
        VitalStatistics vitalStatistics = new VitalStatistics();

        while (isNotTheEnd(xpp,"stats")){
            if (isStartTag(xpp, "exceptions")) {
                return null;    //TODO: I feel dirty
            }

            if (isStartTag(xpp, "og")) {
                vitalStatistics.set_ogStart(getNextByName(xpp, "low"));
                vitalStatistics.set_ogEnd(getNextByName(xpp, "high"));
            } else if (isStartTag(xpp, "fg")) {
                vitalStatistics.set_fgStart(getNextByName(xpp, "low"));
                vitalStatistics.set_fgEnd(getNextByName(xpp, "high"));
            } else if (isStartTag(xpp, "ibu")) {
                vitalStatistics.set_ibuStart(getNextByName(xpp, "low"));
                vitalStatistics.set_ibuEnd(getNextByName(xpp, "high"));
            } else if (isStartTag(xpp, "srm")) {
                vitalStatistics.set_srmStart(getNextByName(xpp, "low"));
                vitalStatistics.set_srmEnd(getNextByName(xpp, "high"));
            } else if (isStartTag(xpp, "abv")) {
                vitalStatistics.set_abvStart(getNextByName(xpp, "low"));
                vitalStatistics.set_abvEnd(getNextByName(xpp, "high"));
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

package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.CategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.Section;
import io.github.rlshep.bjcp2015beerstyles.domain.Tag;

public class CategoryBodyHelper {
    private CategoryBodyActivity activity;
    private String categoryId;
    private static final String DELIM = ", ";
    private VitalStatisticsHelper vitalsHelper;

    public CategoryBodyHelper(CategoryBodyActivity activity, String categoryId) {
        this.activity = activity;
        this.categoryId = categoryId;
        this.vitalsHelper = new VitalStatisticsHelper(activity, categoryId);
    }

    public String getMainText() {
        StringBuilder text = new StringBuilder();

        text.append(getSectionsBody());
        text.append(getTags());
        text.append(vitalsHelper.getVitalStatistics());

        return text.toString();
    }

    private String getSectionsBody() {
        String body = "";
        for (Section section : BjcpDataHelper.getInstance(activity).getCategorySections(categoryId)) {
            body += section.getBody();
        }

        return body;
    }

    private String getTags() {
        String body = "";
        List<Tag> tags = BjcpDataHelper.getInstance(activity).getTags(categoryId);

        for (int i=0; i<tags.size(); i++) {
            body += tags.get(i).getTag();

            if (i != (tags.size() - 1)) {
                body += DELIM;
            }
        }

        return body;
    }
}

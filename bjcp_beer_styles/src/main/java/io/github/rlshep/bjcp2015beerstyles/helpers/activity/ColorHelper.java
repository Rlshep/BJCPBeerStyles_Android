package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import org.apache.commons.lang.StringUtils;

import io.github.rlshep.bjcp2015beerstyles.CategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_SRM;

public class ColorHelper {
    protected CategoryBodyActivity activity;
    protected String categoryId;
    protected PreferencesHelper preferencesHelper;
    private SharedActivityHelper sharedHelper;

    public ColorHelper(CategoryBodyActivity activity, String categoryId) {
        this.activity = activity;
        this.categoryId = categoryId;
        this.preferencesHelper = new PreferencesHelper(activity);
        this.sharedHelper = new SharedActivityHelper(activity);
    }

    public String getColorVerbiage(VitalStatistics vitalStatistics) {
        StringBuilder colorVerbiage = new StringBuilder("");

        if (sharedHelper.isHeaderVerbiageRequired(vitalStatistics, XML_SRM)) {
            colorVerbiage.append(getColorTitle(vitalStatistics));
            colorVerbiage.append(vitalStatistics.getHeader());
        } else if (hasSrmNumeric(vitalStatistics)) {
            colorVerbiage.append(getColorTitle(vitalStatistics));
            colorVerbiage.append(getColorValue(vitalStatistics.getSrmStart()));
            colorVerbiage.append(" - ");
            colorVerbiage.append(getColorValue(vitalStatistics.getSrmEnd()));
        }

        return colorVerbiage.toString();
    }

    public boolean isColorAllowed(VitalStatistics vitalStatistic) {
        return XML_SRM.equals(vitalStatistic.getHeaderTarget()) ||
                (!preferencesHelper.isBrewersAssociation() && StringUtils.isEmpty(vitalStatistic.getHeader()));
    }

    private boolean hasSrmNumeric(VitalStatistics vitalStatistics) {
        return ((0.0 != vitalStatistics.getSrmStart()) || (0.0 != vitalStatistics.getSrmEnd()));
    }


    private StringBuilder getColorTitle(VitalStatistics vitalStatistics) {
        StringBuilder colorVerbiage = new StringBuilder();
        colorVerbiage.append(sharedHelper.getLineHeader(vitalStatistics));

        if (preferencesHelper.isEBC()) {
            colorVerbiage.append(activity.getString(R.string.ebc));
        } else {
            colorVerbiage.append(activity.getString(R.string.srm));
        }
        colorVerbiage.append(": </b>");

        return colorVerbiage;
    }

    private String getColorValue(double srmValue) {
        String color = "";

        if (preferencesHelper.isEBC()) {
            color = (new Double(MetricConverter.getEBC(srmValue))).toString();
        } else if (preferencesHelper.isSRM()) {
            color = (new Double(srmValue)).toString();
        }

        return color;
    }
}

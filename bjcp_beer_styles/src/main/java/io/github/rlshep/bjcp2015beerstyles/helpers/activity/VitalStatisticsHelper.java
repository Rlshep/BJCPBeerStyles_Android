package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.CategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistic;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_ABV;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_ALL;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_FG;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_IBU;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_OG;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_SRM;

public class VitalStatisticsHelper {

    private final static NumberFormat gravityFormatter = new DecimalFormat("#0.000");

    protected CategoryBodyActivity activity;
    protected String categoryId;
    protected PreferencesHelper preferencesHelper;

    public VitalStatisticsHelper(CategoryBodyActivity activity, String categoryId) {
        this.activity = activity;
        this.categoryId = categoryId;
        this.preferencesHelper = new PreferencesHelper(activity);
    }

    public String getMainVitalStatistics() {
        List<VitalStatistic> vitalStatistics = BjcpDataHelper.getInstance(activity).getVitalStatistic(categoryId);
        StringBuilder vitals = new StringBuilder();

        if (0 < vitalStatistics.size()) {
            vitals.append("<br/><br/><big><b>");
            vitals.append(activity.getString(R.string.stats_header));
            vitals.append("</b></big><br/>");
        }

        for (int i = 0; i < vitalStatistics.size(); i++) {
            VitalStatistic vitalStatistic = vitalStatistics.get(i);
            if (!XML_SRM.equals(vitalStatistic.getType())) {    // Colors set in BodyActivity
                vitals.append(getStatisticVerbiage(vitalStatistic));
                vitals.append("<br />");
            }
        }

        // Remove last break because of new text view for colors
        String verbiage = vitals.toString();
        if (verbiage.endsWith("<br />")) {
            int index = verbiage.lastIndexOf("<br />");
            verbiage = vitals.substring(0, index);
        }

        return verbiage;
    }

    public String getStatisticVerbiage(VitalStatistic vitalStatistic) {
        StringBuilder verbiage = new StringBuilder();

        verbiage.append(getStatisticHeader(vitalStatistic));

        if (!StringUtils.isEmpty(vitalStatistic.getNotes())) {
            verbiage.append(vitalStatistic.getNotes());
            verbiage.append(" ");
        }

        if (hasNumeric(vitalStatistic)) {
            verbiage.append(getStatisticValues(vitalStatistic));
        }

        return verbiage.toString();
    }

    protected String getStatisticHeader(VitalStatistic vitalStatistic) {
        StringBuilder verbiage = new StringBuilder("<b>");

        if (!StringUtils.isEmpty(vitalStatistic.getHeader())) {
            verbiage.append(vitalStatistic.getHeader());
            verbiage.append(" ");
        }

        if (XML_IBU.equals(vitalStatistic.getType())) {
            verbiage.append(activity.getString(R.string.ibu));
        } else if (XML_SRM.equals(vitalStatistic.getType())) {
            verbiage.append(getColorHeader());
        } else if (XML_OG.equals(vitalStatistic.getType())) {
            verbiage.append(activity.getString(R.string.og));
        } else if (XML_FG.equals(vitalStatistic.getType())) {
            verbiage.append(activity.getString(R.string.fg));
        } else if (XML_ABV.equals(vitalStatistic.getType())) {
            verbiage.append(getAlcoholHeader());
        } else if (XML_ALL.equals(vitalStatistic.getType())) {
            verbiage.append(activity.getString(R.string.all));
        }

        verbiage.append(": </b>");

        return verbiage.toString();
    }

    private String getColorHeader() {
        StringBuilder verbiage = new StringBuilder();

        if (preferencesHelper.isEBC()) {
            verbiage.append(activity.getString(R.string.ebc));
        } else {
            verbiage.append(activity.getString(R.string.srm));
        }

        return verbiage.toString();
    }


    private StringBuilder getAlcoholHeader() {
        StringBuilder header = new StringBuilder();

        if (preferencesHelper.isABV()) {
            header.append(activity.getString(R.string.ABV));
        } else {
            header.append(activity.getString(R.string.ABW));
        }

        return header;
    }

    private String getStatisticValues(VitalStatistic vitalStatistic) {
        StringBuilder verbiage = new StringBuilder();

        if (XML_IBU.equals(vitalStatistic.getType())) {
            verbiage.append(getIbuValues(vitalStatistic));
        } else if (XML_SRM.equals(vitalStatistic.getType())) {
            verbiage.append(getColorValues(vitalStatistic));
        } else if (XML_OG.equals(vitalStatistic.getType())) {
            verbiage.append(getGravityValues(vitalStatistic));
        } else if (XML_FG.equals(vitalStatistic.getType())) {
            verbiage.append(getGravityValues(vitalStatistic));
        } else if (XML_ABV.equals(vitalStatistic.getType())) {
            verbiage.append(getAlcoholValues(vitalStatistic));
        }

        return verbiage.toString();
    }

    private String getIbuValues(VitalStatistic vitalStatistic) {
        StringBuilder value = new StringBuilder();

        value.append(Integer.valueOf((int)vitalStatistic.getLow()));
        value.append(" - ");
        value.append(Integer.valueOf((int)vitalStatistic.getHigh()));

        return value.toString();
    }

    private String getColorValues(VitalStatistic vitalStatistic) {
        StringBuilder value = new StringBuilder();

        value.append(getColorValue(vitalStatistic.getLow()));
        value.append(" - ");
        value.append(getColorValue(vitalStatistic.getHigh()));

        return value.toString();
    }

    private String getColorValue(double srmValue) {
        String color = "";

        if (preferencesHelper.isEBC()) {
            color = (Double.valueOf(MetricConverter.getEBC(srmValue))).toString();
        } else if (preferencesHelper.isSRM()) {
            color = (Double.valueOf(srmValue)).toString();
        }

        return color;
    }

    private String getGravityValues(VitalStatistic vitalStatistic) {
        StringBuilder value = new StringBuilder();

        if (preferencesHelper.isPlato()) {
            value.append(MetricConverter.getPlato(vitalStatistic.getLow()));
            value.append(activity.getString(R.string.plato));
            value.append(" - ");
            value.append(MetricConverter.getPlato(vitalStatistic.getHigh()));
            value.append(activity.getString(R.string.plato));
        } else {
            value.append(gravityFormatter.format(vitalStatistic.getLow()));
            value.append(" - ");
            value.append(gravityFormatter.format(vitalStatistic.getHigh()));
        }

        return value.toString();
    }


    private String getAlcoholValues(VitalStatistic vitalStatistic) {
        StringBuilder value = new StringBuilder();

        value.append(getAlcoholValue(vitalStatistic.getLow()));
        value.append("%");
        value.append(" - ");
        value.append(getAlcoholValue(vitalStatistic.getHigh()));
        value.append("%");

        return value.toString();
    }

    private String getAlcoholValue(double alcohol) {
        String converted;

        if (preferencesHelper.isABV()) {
            converted = (Double.valueOf(alcohol)).toString();
        } else {
            converted = MetricConverter.getABW(alcohol);
        }

        return converted;
    }

    private boolean hasNumeric(VitalStatistic vitalStatistics) {
        return ((0.0 != vitalStatistics.getLow()) || (0.0 != vitalStatistics.getHigh()));
    }
}

package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.CategoryBodyActivity;
import io.github.rlshep.bjcp2015beerstyles.R;
import io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract;
import io.github.rlshep.bjcp2015beerstyles.converters.MetricConverter;
import io.github.rlshep.bjcp2015beerstyles.db.BjcpDataHelper;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_FG;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_IBU;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpContract.XML_OG;

public class VitalStatisticsHelper {
    
    protected CategoryBodyActivity activity;
    protected String categoryId;
    protected PreferencesHelper preferencesHelper;
    private NumberFormat gravityFormatter = new DecimalFormat("#0.000");
    private SharedActivityHelper sharedHelper;


    public VitalStatisticsHelper(CategoryBodyActivity activity, String categoryId) {
        this.activity = activity;
        this.categoryId = categoryId;
        this.preferencesHelper = new PreferencesHelper(activity);
        this.sharedHelper = new SharedActivityHelper(activity);
    }

    public String getVitalStatistics() {
        List<VitalStatistics> vitalStatisticses = BjcpDataHelper.getInstance(activity).getVitalStatistics(categoryId);
        StringBuilder vitals = new StringBuilder();

        if (0 < vitalStatisticses.size()) {
            vitals.append("<br/><br/><big><b>");
            vitals.append(activity.getString(R.string.stats_header));
            vitals.append("</b></big>");
        }

        for (VitalStatistics vitalStatistics : vitalStatisticses) {
            vitals.append(addIBUs(vitalStatistics));
            vitals.append(addOg(vitalStatistics));
            vitals.append(addFg(vitalStatistics));
            vitals.append(addAbv(vitalStatistics));
        }

        return vitals.toString();
    }

    private StringBuilder addIBUs(VitalStatistics vitalStatistics) {
        StringBuilder vitals = new StringBuilder();

        if (sharedHelper.isHeaderVerbiageRequired(vitalStatistics, XML_IBU)) {
            vitals.append("<br><b>");
            vitals.append(getIbuHeader());
            vitals.append(vitalStatistics.getHeader());
        } else if (0 < vitalStatistics.getIbuStart() || 0 < vitalStatistics.getIbuStart()) {
            vitals.append("<br><b>");
            vitals.append(vitalStatistics.getHeader());
            vitals.append(" ");
            vitals.append(getIbuHeader());
            vitals.append(vitalStatistics.getIbuStart());
            vitals.append(" - ");
            vitals.append(vitalStatistics.getIbuEnd());
        }

        return vitals;
    }

    private StringBuilder getIbuHeader() {
        StringBuilder header = new StringBuilder();

        header.append(activity.getString(R.string.ibu));
        header.append(":</b> ");

        return  header;
    }

    private StringBuilder addOg(VitalStatistics vitalStatistics) {
        StringBuilder vitals = new StringBuilder();

        if (sharedHelper.isHeaderVerbiageRequired(vitalStatistics, XML_OG)) {
            vitals.append("<br><b>");
            vitals.append(activity.getString(R.string.og));
            vitals.append(":</b> ");
            vitals.append(vitalStatistics.getHeader());
        } else if (0 < vitalStatistics.getOgStart()) {
            vitals.append(getOgVerbiage(vitalStatistics));
        }

        return vitals;
    }

    private String getOgVerbiage(VitalStatistics vitalStatistics) {
        StringBuilder ogVerbiage = new StringBuilder();
        ogVerbiage.append("<br><b>");
        ogVerbiage.append(vitalStatistics.getHeader());
        ogVerbiage.append(" ");
        ogVerbiage.append(activity.getString(R.string.og));
        ogVerbiage.append(":</b> ");

        if (preferencesHelper.isPlato()) {
            ogVerbiage.append(MetricConverter.getPlato(vitalStatistics.getOgStart()));
            ogVerbiage.append(activity.getString(R.string.plato));
            ogVerbiage.append(" - ");
            ogVerbiage.append(MetricConverter.getPlato(vitalStatistics.getOgEnd()));
            ogVerbiage.append(activity.getString(R.string.plato));
        } else {
            ogVerbiage.append(gravityFormatter.format(vitalStatistics.getOgStart()));
            ogVerbiage.append(" - ");
            ogVerbiage.append(gravityFormatter.format(vitalStatistics.getOgEnd()));
        }

        return ogVerbiage.toString();
    }

    private StringBuilder addFg(VitalStatistics vitalStatistics) {
        StringBuilder vitals = new StringBuilder();

        if (sharedHelper.isHeaderVerbiageRequired(vitalStatistics, XML_FG)) {
            vitals.append("<br><b>");
            vitals.append(activity.getString(R.string.fg));
            vitals.append(":</b> ");
            vitals.append(vitalStatistics.getHeader());
        } else if (0 < vitalStatistics.getFgStart()) {
            vitals.append(getFgVerbiage(vitalStatistics));
        }

        return vitals;
    }

    private String getFgVerbiage(VitalStatistics vitalStatistics) {
        StringBuilder fgVerbiage = new StringBuilder();

        fgVerbiage.append("<br><b>");
        fgVerbiage.append(vitalStatistics.getHeader());
        fgVerbiage.append(" ");
        fgVerbiage.append(activity.getString(R.string.fg));
        fgVerbiage.append(":</b> ");

        if (preferencesHelper.isPlato()) {
            fgVerbiage.append(MetricConverter.getPlato(vitalStatistics.getFgStart()));
            fgVerbiage.append(activity.getString(R.string.plato));
            fgVerbiage.append(" - ");
            fgVerbiage.append(MetricConverter.getPlato(vitalStatistics.getFgEnd()));
            fgVerbiage.append(activity.getString(R.string.plato));
        } else {
            fgVerbiage.append(gravityFormatter.format(vitalStatistics.getFgStart()));
            fgVerbiage.append(" - ");
            fgVerbiage.append(gravityFormatter.format(vitalStatistics.getFgEnd()));
        }

        return fgVerbiage.toString();
    }

    private StringBuilder addAbv(VitalStatistics vitalStatistics) {
        StringBuilder vitals = new StringBuilder();

        if (sharedHelper.isHeaderVerbiageRequired(vitalStatistics, BjcpContract.XML_ABV)) {
            vitals.append("<br>");
            vitals.append(getAlcoholHeader(vitalStatistics));
            vitals.append(vitalStatistics.getHeader());
        } else if (0 < vitalStatistics.getAbvStart() || 0 < vitalStatistics.getAbvEnd()) {
            vitals.append("<br>");
            vitals.append(getAlcoholHeader(vitalStatistics));
            vitals.append(getAlcoholValue(vitalStatistics.getAbvStart()));
            vitals.append(" - ");
            vitals.append(getAlcoholValue(vitalStatistics.getAbvEnd()));
        }

        return vitals;
    }

    private StringBuilder getAlcoholHeader(VitalStatistics vitalStatistics) {
        StringBuilder header = sharedHelper.getLineHeader(vitalStatistics);

        if (preferencesHelper.isABV()) {
            header.append(activity.getString(R.string.ABV));
        } else {
            header.append(activity.getString(R.string.ABW));
        }
        header.append(":</b> ");

        return header;
    }

    private String getAlcoholValue(double alcohol) {
        String converted = "";

        if (preferencesHelper.isABV()) {
            converted = (new Double(alcohol)).toString();
        } else {
            converted = MetricConverter.getABW(alcohol);
        }

        return converted;
    }
}

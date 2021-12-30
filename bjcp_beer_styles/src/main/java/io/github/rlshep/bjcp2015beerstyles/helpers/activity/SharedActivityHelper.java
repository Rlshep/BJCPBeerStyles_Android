package io.github.rlshep.bjcp2015beerstyles.helpers.activity;

import org.apache.commons.lang.StringUtils;

import io.github.rlshep.bjcp2015beerstyles.BjcpActivity;
import io.github.rlshep.bjcp2015beerstyles.domain.VitalStatistics;
import io.github.rlshep.bjcp2015beerstyles.helpers.PreferencesHelper;

public class SharedActivityHelper
{   private PreferencesHelper preferencesHelper;

    public SharedActivityHelper(BjcpActivity activity) {
        this.preferencesHelper = new PreferencesHelper(activity);
    }

    public StringBuilder getLineHeader(VitalStatistics vitalStatistics) {
        StringBuilder header = new StringBuilder();
        header.append("<b>");
        header.append(preferencesHelper.isBrewersAssociation() ? "" : vitalStatistics.getHeader());    //TODO: FIND BETTER SOLUTION
        header.append(" ");

        return header;
    }

    public boolean isHeaderVerbiageRequired(VitalStatistics vitalStatistics, String target) {
        return !StringUtils.isEmpty(vitalStatistics.getHeader())
                && target.equals(vitalStatistics.getHeaderTarget())
                && preferencesHelper.isBrewersAssociation();
    }
}

package io.github.rlshep.bjcp2015beerstyles.converters;

import android.content.SharedPreferences;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants;

public class MetricConverter {
    private static final double SRM = 1.97;
    private static final double EBC_A = 616.868;
    private static final double EBC_B = 1111.14;
    private static final double EBC_C = 630.272;
    private static final double EBC_D = 135.997;
    private static final String[] IMPERIAL_COUNTRIES = {"US", "MM", "LR"}; //Countries not using metric US, Burma, Liberia
    private static final List<String> imperialCountries = Arrays.asList(IMPERIAL_COUNTRIES);

    //EBC = SRM × 1.97
    public static int getEBC(String inSRM) {
        int srm = 0;

        try {
            srm = (int) Math.round(new Integer(inSRM) * SRM);
        } catch (Exception e) {
            System.out.println(e);
        }

        return srm;
    }

    // (-1 * 616.868) + (1111.14 * SG) – (630.272 * SG^2) + (135.997 * SG^3)
    public static String getPlato(String inGravity) {
        DecimalFormat df = new DecimalFormat("#.#");
        double plato = 0.0;

        try {
            Double gravity = new Double(inGravity);
            plato = ((EBC_A * -1) + (EBC_B * gravity) - (EBC_C * Math.pow(gravity, 2)) + (EBC_D * Math.pow(gravity, 3)));
        } catch (Exception e) {
            System.out.println(e);
        }

        return df.format(plato);
    }

    public static boolean isCountryMetric(String country) {
        boolean metric = true;

        if (StringUtils.isNotEmpty(country)) {
            metric = !imperialCountries.contains(country.toUpperCase());
        }

        return metric;
    }

    public static boolean isMetric(SharedPreferences sharedPref) {
        boolean metric = false;
        String unitsPref = sharedPref.getString(BjcpConstants.UNIT, null); // getting String

        if (!StringUtils.isEmpty(unitsPref) && BjcpConstants.METRIC.equals(unitsPref)) {
            metric = true;
        }

        return metric;
    }
}

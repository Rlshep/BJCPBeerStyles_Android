package io.github.rlshep.bjcp2015beerstyles.converters;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class MetricConverter {
    private static final String[] IMPERIAL_COUNTRIES = {"US", "MM", "LR"}; //Countries not using metric US, Burma, Liberia
    private static final List<String> imperialCountries = Arrays.asList(IMPERIAL_COUNTRIES);
    private static final double SRM = 1.97;
    private static final double EBC_A = 616.868;
    private static final double EBC_B = 1111.14;
    private static final double EBC_C = 630.272;
    private static final double EBC_D = 135.997;

    //EBC = SRM × 1.97
    public static double getEBC(double inSRM) {
        return (int) Math.round(inSRM * SRM);
    }

    //SRM = EBC / 1.97
    public static double getSRM(double inEBC) {
        return (int) Math.round(inEBC / SRM);
    }

    // (-1 * 616.868) + (1111.14 * SG) – (630.272 * SG^2) + (135.997 * SG^3)
    public static String getPlato(double gravity) {
        DecimalFormat df = new DecimalFormat("#.#");

        double plato = (EBC_A * -1) + (EBC_B * gravity) - (EBC_C * Math.pow(gravity, 2)) + (EBC_D * Math.pow(gravity, 3));

        // Fix for -0
        if (0 > plato && -0.05 < plato) {
            plato = 0.0;
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
}

package io.github.rlshep.bjcp2015beerstyles.converters;

import org.junit.Test;

public class MetricConverterTest {

    @Test
    public void getEBCFromSRM() {
        assert(2 == MetricConverter.getEBC("1"));
        assert(4 == MetricConverter.getEBC("2"));
        assert(20 == MetricConverter.getEBC("10"));
        assert(79 == MetricConverter.getEBC("40"));
        assert(-2 == MetricConverter.getEBC("-1"));
        assert(0 == MetricConverter.getEBC("wtf"));

    }
    //TODO: Disable until merge filter branch
//    @Test
//    public void getPlatoFromGravity() {
//        assert(-13.5 == MetricConverter.getPlato("0.950"));
//        assert(0.0 == MetricConverter.getPlato("1.000"));
//        assert(0.5 == MetricConverter.getPlato("1.002"));
//        assert(12.4 == MetricConverter.getPlato("1.050"));
//        assert(23.8 == MetricConverter.getPlato("1.100"));
//        assert(39.9 == MetricConverter.getPlato("1.179"));
//        assert(43.9 == MetricConverter.getPlato("1.200"));
//        assert(-616.9 == MetricConverter.getPlato("0.000"));
//        assert(-2782.8 == MetricConverter.getPlato("-1.100"));
//        assert(0.0 == MetricConverter.getPlato("wtf"));
//    }

    @Test
    public void isCountryMetric() {
        assert(!MetricConverter.isCountryMetric("US"));
        assert(MetricConverter.isCountryMetric("UK"));
        assert(MetricConverter.isCountryMetric("wtf"));
        assert(MetricConverter.isCountryMetric(""));
        assert(MetricConverter.isCountryMetric(null));
    }
}

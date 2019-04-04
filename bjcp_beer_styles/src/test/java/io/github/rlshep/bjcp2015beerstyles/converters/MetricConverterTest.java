package io.github.rlshep.bjcp2015beerstyles.converters;

import org.junit.Test;

public class MetricConverterTest {

    @Test
    public void getEBCFromSRM() {
        assert(2 == MetricConverter.getEBC(1));
        assert(4 == MetricConverter.getEBC(2));
        assert(20 == MetricConverter.getEBC(10));
        assert(79 == MetricConverter.getEBC(40));
        assert(-2 == MetricConverter.getEBC(-1));
        assert(0 == MetricConverter.getEBC(0));
        assert(11 == MetricConverter.getEBC(5.5));
    }

    @Test
    public void getSRMFromEBC() {
        assert(1 == MetricConverter.getSRM(2));
        assert(2 == MetricConverter.getSRM(4));
        assert(10 == MetricConverter.getSRM(20));
        assert(40 == MetricConverter.getSRM(79));
        assert(-1 == MetricConverter.getSRM(-2));
        assert(0 == MetricConverter.getSRM(0));
    }

    @Test
    public void getPlatoFromGravity() {
        assert("-13.5".equals(MetricConverter.getPlato(0.950)));
        assert("0".equals(MetricConverter.getPlato(1.000)));
        assert("0.5".equals(MetricConverter.getPlato(1.002)));
        assert("12.4".equals(MetricConverter.getPlato(1.050)));
        assert("23.8".equals(MetricConverter.getPlato(1.100)));
        assert("39.9".equals(MetricConverter.getPlato(1.179)));
        assert("43.9".equals(MetricConverter.getPlato(1.200)));
        assert("-616.9".equals(MetricConverter.getPlato(0.000)));
        assert("-2782.8".equals(MetricConverter.getPlato(-1.100)));
    }

    @Test
    public void isCountryMetric() {
        assert(!MetricConverter.isCountryMetric("US"));
        assert(MetricConverter.isCountryMetric("UK"));
        assert(MetricConverter.isCountryMetric("wtf"));
        assert(MetricConverter.isCountryMetric(""));
        assert(MetricConverter.isCountryMetric(null));
    }
}

package io.github.rlshep.bjcp2015beerstyles.formatters;

import org.junit.Test;

public class MetricFormatterTest {

    @Test
    public void getEBCFromSRM() {
        assert(2 == MetricFormatter.getEBC("1"));
        assert(4 == MetricFormatter.getEBC("2"));
        assert(20 == MetricFormatter.getEBC("10"));
        assert(79 == MetricFormatter.getEBC("40"));
        assert(-2 == MetricFormatter.getEBC("-1"));
        assert(0 == MetricFormatter.getEBC("wtf"));

    }

    @Test
    public void getPlatoFromGravity() {
        assert(-13.5 == MetricFormatter.getPlato("0.950"));
        assert(0.0 == MetricFormatter.getPlato("1.000"));
        assert(0.5 == MetricFormatter.getPlato("1.002"));
        assert(12.4 == MetricFormatter.getPlato("1.050"));
        assert(23.8 == MetricFormatter.getPlato("1.100"));
        assert(39.9 == MetricFormatter.getPlato("1.179"));
        assert(43.9 == MetricFormatter.getPlato("1.200"));
        assert(-616.9 == MetricFormatter.getPlato("0.000"));
        assert(-2782.8 == MetricFormatter.getPlato("-1.100"));
        assert(0.0 == MetricFormatter.getPlato("wtf"));
    }

}


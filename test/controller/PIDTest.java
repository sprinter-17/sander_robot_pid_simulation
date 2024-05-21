package controller;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.OptionalDouble;

import static org.assertj.core.api.Assertions.assertThat;

class PIDTest {
    private static final Offset<Double> TOL = Offset.offset(0.0001);
    private final PID pid = new PID();

    @Test
    void testProportional() {
        pid.setConfig(new Config(1, 0, 0, OptionalDouble.empty()));
        assertThat(pid.getOutput(50.00)).isCloseTo(50.00, TOL);
        pid.setConfig(new Config(2, 0, 0, OptionalDouble.empty()));
        assertThat(pid.getOutput(50.00)).isCloseTo(100.00, TOL);
    }

    @Test
    void testIntegral() {
        pid.setConfig(new Config(0, 0, 1, OptionalDouble.empty()));
        assertThat(pid.getOutput(50.00)).isCloseTo(50.00, TOL);
        assertThat(pid.getOutput(50.00)).isCloseTo(100.00, TOL);
        assertThat(pid.getOutput(50.00)).isCloseTo(150.00, TOL);
    }

    @Test
    void testIntegralCap() {
        pid.setConfig(new Config(0, 0, 2, OptionalDouble.of(120)));
        assertThat(pid.getOutput(50.00)).isCloseTo(100.00, TOL);
        assertThat(pid.getOutput(50.00)).isCloseTo(200.00, TOL);
        assertThat(pid.getOutput(50.00)).isCloseTo(240.00, TOL);
    }

    @Test
    void testDerivative() {
        pid.setConfig(new Config(0, 1, 0, OptionalDouble.empty()));
        pid.getOutput(50.0);
        assertThat(pid.getOutput(60.00)).isCloseTo(10, TOL);
        assertThat(pid.getOutput(60.00)).isCloseTo(0, TOL);
        assertThat(pid.getOutput(50.00)).isCloseTo(-10, TOL);
    }

}
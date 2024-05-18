package model;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControlTest {
    private static final Offset<Double> TOL = Offset.offset(0.001);

    @Test
    public void testRotations() {
        assertThat(Control.ROTATION.apply(0)).isCloseTo(1.0, TOL);
        assertThat(Control.ROTATION.apply(Math.PI / 2)).isCloseTo(1.0, TOL);
    }

    @Test
    public void testForwardBackward() {
        assertThat(Control.VERTICAL_MOVEMENT.apply(0)).isCloseTo(0.0, TOL);
        assertThat(Control.VERTICAL_MOVEMENT.apply(Math.PI / 2)).isCloseTo(1.0, TOL);
        assertThat(Control.VERTICAL_MOVEMENT.apply(Math.PI)).isCloseTo(0.0, TOL);
        assertThat(Control.VERTICAL_MOVEMENT.apply(-Math.PI / 2)).isCloseTo(-1.0, TOL);
    }

    @Test
    public void testLeftRight() {
        assertThat(Control.HORIZONTAL_MOVEMENT.apply(0)).isCloseTo(1.0, TOL);
        assertThat(Control.HORIZONTAL_MOVEMENT.apply(Math.PI / 2)).isCloseTo(0.0, TOL);
        assertThat(Control.HORIZONTAL_MOVEMENT.apply(Math.PI)).isCloseTo(-1.0, TOL);
        assertThat(Control.HORIZONTAL_MOVEMENT.apply(-Math.PI / 2)).isCloseTo(0.0, TOL);
    }
}
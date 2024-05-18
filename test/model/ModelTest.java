package model;


import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import java.util.Random;

class ModelTest {
    private static final Offset<Double> TOL = Offset.offset(0.0001);
    private final Random random = mock(Random.class);
    private final Model model = new Model(random, 100.0);

    @Test
    public void testInit() {
        assertThat(model.getWorldSize()).isCloseTo(100.0, TOL);
        assertThat(model.getRobotLocation().position().x()).isCloseTo(50.0, TOL);
    }
}
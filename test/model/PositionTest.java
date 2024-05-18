package model;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PositionTest {
    private static final Offset<Double> TOL = Offset.offset(0.01);

    @Test
    public void testOrigin() {
        assertThat(Position.ORIGIN.x()).isCloseTo(0.0, TOL);
        assertThat(Position.ORIGIN.y()).isCloseTo(0.0, TOL);
    }

    @Test
    public void testAdd() {
        Position position = new Position(10.0, 20.0);
        Position result = position.add(30, -5);
        Position expected = new Position(40, 15);
        assertThat(result.x()).isCloseTo(expected.x(), TOL);
        assertThat(result.y()).isCloseTo(expected.y(), TOL);
    }
//
//    @Test
//    public void testLimit() {
//        Position position = new Position(50, 50);
//        assertThat(position.limit(40, 60)).isEqualTo(new Position(50, 50));
//        assertThat(position.limit(55, 60)).isEqualTo(new Position(55, 55));
//        assertThat(position.limit(40, 45)).isEqualTo(new Position(45, 45));
//    }
}
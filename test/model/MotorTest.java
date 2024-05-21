package model;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MotorTest {
    private static final Offset<Double> TOL = Offset.offset(0.001);
    private static final Parameters PARAMS = new Parameters();

    private Motor normalisedMotor(double angle) {
        Motor motor = new Motor(angle);
        while (motor.getVelocity() < 1.0)
            motor.setVelocity(1.0, PARAMS);
        return motor;
    }

    @Test
    public void testAngle() {
        assertThat(new Motor(17).getAngle()).isCloseTo(17, TOL);
    }

    @Test
    public void testPositiveAcceleration() {
        Motor motor = new Motor(0);
        double target = 0.745;
        for (double expected = 0; expected < PARAMS.getMaxVelocity(); expected += PARAMS.getAcceleration()) {
            assertThat(motor.getVelocity()).isCloseTo(Math.min(target, expected), TOL);
            motor.setVelocity(target, PARAMS);
        }
    }

    @Test
    public void testNegativeAcceleration() {
        Motor motor = new Motor(0);
        double target = -2.15;
        for (double expected = 0; expected > -PARAMS.getMaxVelocity(); expected -= PARAMS.getAcceleration()) {
            assertThat(motor.getVelocity()).isCloseTo(Math.max(target, expected), TOL);
            motor.setVelocity(target, PARAMS);
        }
    }

    @Test
    public void testMaxPositiveVelocity() {
        Motor motor = new Motor(0);
        double target = PARAMS.getMaxVelocity() + 1.0;
        for (double expected = 0; expected < target; expected += PARAMS.getAcceleration()) {
            assertThat(motor.getVelocity()).isCloseTo(Math.min(PARAMS.getMaxVelocity(), expected), TOL);
            motor.setVelocity(target, PARAMS);
        }
    }

    @Test
    public void testMaxNegativeVelocity() {
        Motor motor = new Motor(0);
        double target = -PARAMS.getMaxVelocity() - 1.0;
        for (double expected = 0; expected > target; expected -= PARAMS.getAcceleration()) {
            assertThat(motor.getVelocity()).isCloseTo(Math.max(-PARAMS.getMaxVelocity(), expected), TOL);
            motor.setVelocity(target, PARAMS);
        }
    }

    private Location normalisedTranslation(double angle, double rotation) {
        return normalisedMotor(angle).applyTransforms(new Location(Position.ORIGIN, rotation));
    }

    @Test
    public void testApplyTranslation() {
        assertThat(normalisedTranslation(0.0, 0.0).position().x()).isCloseTo(1.0, TOL);
        assertThat(normalisedTranslation(0.0, 0.0).position().y()).isCloseTo(0.0, TOL);
        assertThat(normalisedTranslation(90.0, 0.0).position().x()).isCloseTo(0.0, TOL);
        assertThat(normalisedTranslation(90.0, 0.0).position().y()).isCloseTo(1.0, TOL);
        assertThat(normalisedTranslation(0.0, 90.0).position().x()).isCloseTo(0.0, TOL);
        assertThat(normalisedTranslation(0.0, 90.0).position().y()).isCloseTo(1.0, TOL);
        assertThat(normalisedTranslation(90.0, 90.0).position().x()).isCloseTo(-1.0, TOL);
        assertThat(normalisedTranslation(90.0, 90.0).position().y()).isCloseTo(0.0, TOL);
    }
}
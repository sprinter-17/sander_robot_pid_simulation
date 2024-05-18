package model;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MotorTest {
    private static final Offset<Double> TOL = Offset.offset(0.001);

    private Motor normalisedMotor(double angle) {
        Motor motor = new Motor(angle);
        while (motor.getVelocity() < 1.0)
            motor.setVelocity(1.0);
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
        for (double expected = 0; expected < Motor.MAX_VELOCITY; expected += Motor.ACCELERATION) {
            assertThat(motor.getVelocity()).isCloseTo(Math.min(target, expected), TOL);
            motor.setVelocity(target);
        }
    }

    @Test
    public void testNegativeAcceleration() {
        Motor motor = new Motor(0);
        double target = -2.15;
        for (double expected = 0; expected > -Motor.MAX_VELOCITY; expected -= Motor.ACCELERATION) {
            assertThat(motor.getVelocity()).isCloseTo(Math.max(target, expected), TOL);
            motor.setVelocity(target);
        }
    }

    @Test
    public void testMaxPositiveVelocity() {
        Motor motor = new Motor(0);
        double target = Motor.MAX_VELOCITY + 1.0;
        for (double expected = 0; expected < target; expected += Motor.ACCELERATION) {
            assertThat(motor.getVelocity()).isCloseTo(Math.min(Motor.MAX_VELOCITY, expected), TOL);
            motor.setVelocity(target);
        }
    }

    @Test
    public void testMaxNegativeVelocity() {
        Motor motor = new Motor(0);
        double target = -Motor.MAX_VELOCITY - 1.0;
        for (double expected = 0; expected > target; expected -= Motor.ACCELERATION) {
            assertThat(motor.getVelocity()).isCloseTo(Math.max(-Motor.MAX_VELOCITY, expected), TOL);
            motor.setVelocity(target);
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
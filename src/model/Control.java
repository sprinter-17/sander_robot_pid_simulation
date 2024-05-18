package model;

import java.util.function.Function;

/*
 * Instructions that can be used to control the movement of the robot. Each Control can be independently associated with
 * a signed target velocity.
 */
public enum Control {
    ROTATION(_ -> 1.0),
    HORIZONTAL_MOVEMENT(Math::cos),
    VERTICAL_MOVEMENT(Math::sin);

    private final Function<Double, Double> velocityGenerator;

    Control(Function<Double, Double> velocityGenerator) {
        this.velocityGenerator = velocityGenerator;
    }

    /*
     * Calculate the contribution a motor at a given angle makes to the movement represented by this control.
     */
    public double apply(double angle) {
        return velocityGenerator.apply(angle);
    }
}

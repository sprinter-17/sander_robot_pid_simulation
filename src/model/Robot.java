package model;

import java.util.*;
import java.util.stream.IntStream;

public class Robot {
    private static final double MARGIN = 30.0;
    private final List<Motor> motors = new ArrayList<>();
    private final EnumMap<Control, Double> activeControls = new EnumMap<>(Control.class);

    public Robot() {
        IntStream.range(0, 3)
                .mapToObj(i -> new Motor(i * 120))
                .forEach(motors::add);
    }

    public void setControl(Control control, double velocity) {
        activeControls.put(control, velocity);
    }

    public void clearControl(Control control) {
        activeControls.remove(control);
    }

    public void clearAllControls() {
        activeControls.clear();
    }

    public double getMotorVelocity(int motorIndex) {
        return motors.get(motorIndex).getVelocity();
    }

    protected Location updateLocation(Location location, double worldSize) {
        // apply all motor movement
        for (Motor motor : motors) {
            double radians = Math.toRadians(motor.getAngle());
            double velocity = activeControls.entrySet().stream()
                    .mapToDouble(e -> e.getKey().apply(radians) * e.getValue())
                    .sum();
            motor.setVelocity(velocity);
            location = motor.applyTransforms(location);
        }

        // limit to edge of world
        return location.limit(MARGIN, worldSize - MARGIN);
    }
}

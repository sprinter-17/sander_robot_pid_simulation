package controller;

import model.Control;
import model.Position;
import model.Robot;
import model.Location;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Random;

/*
 * Controls the movement of the robot using a PID controller for each possible robot control.
 */
public class Controller {
    private final Robot robot;
    private final EnumMap<Control,PID> controls = new EnumMap<>(Control.class);

    public Controller(Robot robot) {
        this.robot = robot;
        Arrays.stream(Control.values()).forEach(c -> this.controls.put(c, new PID()));
    }

    public void setPID(Config config) {
        controls.values().forEach(p -> p.setConfig(config));
    }

    public void resetPID() {
        controls.values().forEach(PID::reset);
    }

    public void control(Location reading, Position target) {
        robot.clearAllControls();

        double rotationError = reading.rotationDifference(180);
        double rotationOutput = controls.get(Control.ROTATION).getOutput(rotationError);
        robot.setControl(Control.ROTATION, -rotationOutput);

        if (Math.abs(rotationError) < 10) {
            double horizontalError = target.x() - reading.position().x();
            double horizontalOutput = controls.get(Control.HORIZONTAL_MOVEMENT).getOutput(horizontalError);
            robot.setControl(Control.HORIZONTAL_MOVEMENT, -horizontalOutput);

            double verticalError = target.y() - reading.position().y();
            double verticalOutput = controls.get(Control.VERTICAL_MOVEMENT).getOutput(verticalError);
            robot.setControl(Control.VERTICAL_MOVEMENT, -verticalOutput);
        }
    }
}

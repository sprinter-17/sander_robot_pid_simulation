package model;

import controller.Controller;

import java.util.Random;

/*
 * Contains information about the robot and the world it is operating in.
 */
public class Model {
    private final Random random;
    private final Parameters params;
    private final double worldSize;
    private final Robot robot;
    private final Target target;
    private final Controller controller;

    private boolean controllerActive = false;
    private Location robotLocation;

    public Model(Random random, Parameters params, double worldSize) {
        Position centre = new Position(worldSize / 2, worldSize / 2);
        this.random = random;
        this.params = params;
        this.worldSize = worldSize;
        this.robot = new Robot(params);
        this.robotLocation = new Location(centre, 0);
        this.target = new Target(centre, worldSize / 3);
        this.controller = new Controller(robot);
    }

    public double worldSize() {
        return worldSize;
    }

    public void setControllerActive(boolean active) {
        controllerActive = active;
        robot.clearAllControls();
        controller.resetPID();
    }

    public void toggleControllerActive() {
        setControllerActive(!controllerActive);
    }

    public void update() {
        if (controllerActive) {
            Location reading = robotLocation.addError(random, params.getJitter());
            controller.control(reading, target.position());
        }
        robotLocation = robotLocation.addError(random, params.getJitter());
        robotLocation = robot.updateLocation(robotLocation, worldSize);
        target.update(params);
    }

    public Robot getRobot() {
        return robot;
    }

    public Location getRobotLocation() {
        return robotLocation;
    }

    public Target getTarget() {
        return target;
    }

    public Controller controller() {
        return controller;
    }
}

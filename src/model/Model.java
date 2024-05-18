package model;

import java.util.Random;

/*
 * Contains information about the robot and the world it is operating in.
 */
public class Model {
    private final Random random;
    private final double worldSize;
    private final Robot robot;
    private final Target target;
    private double jitter = 0.0;
    private Location robotLocation;

    public Model(Random random, double worldSize) {
        Position centre = new Position(worldSize / 2, worldSize / 2);
        this.random = random;
        this.worldSize = worldSize;
        this.robot = new Robot();
        this.robotLocation = new Location(centre, 0);
        this.target = new Target(centre, worldSize / 3, -0.005);
    }

    public void setJitter(double jitter) {
        this.jitter = jitter;
    }

    public double getWorldSize() {
        return worldSize;
    }

    public void update() {
        robotLocation = robotLocation.addError(random, jitter);
        robotLocation = robot.updateLocation(robotLocation, worldSize);
        target.update();
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
}

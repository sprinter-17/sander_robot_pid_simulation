package model;

public class Target {
    private final Position centre;
    private final double radius;

    private double angle = 0.0;

    public Target(Position centre, double radius) {
        this.centre = centre;
        this.radius = radius;
    }

    public void update(Parameters params) {
        angle += params.getTargetSpeed();
    }

    public Position position() {
        return new Position(centre.x() + Math.sin(angle) * radius, centre.y() + Math.cos(angle) * radius);
    }

}

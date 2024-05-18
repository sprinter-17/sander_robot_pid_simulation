package model;

public class Target {
    private final Position centre;
    private final double radius;
    private final double speed;

    private double angle = 0.0;

    public Target(Position centre, double radius, double speed) {
        this.centre = centre;
        this.radius = radius;
        this.speed = speed;
    }

    public void update() {
        angle += speed;
    }

    public Position position() {
        return new Position(centre.x() + Math.sin(angle) * radius, centre.y() + Math.cos(angle) * radius);
    }

}

package model;

import java.util.Random;

public record Location(Position position, double rotation) {
    public Location(Position position, double rotation) {
        this.position = position;
        this.rotation = (rotation % 360 + 360) % 360;
    }

    public double x() {
        return position().x();
    }

    public double y() {
        return position().y();
    }

    public double rotationDifference(double angle) {
        double difference = angle - rotation;
        if (difference < -180)
            difference += 360;
        return difference;
    }

    public Location addError(Random random, double scale) {
        if (scale == 0.0)
            return this;
        Position newPosition = position.add(
                random.nextDouble(scale) - random.nextDouble(scale),
                random.nextDouble(scale) - random.nextDouble(scale)
        );
        double newRotation = rotation + random.nextDouble(scale) - random.nextDouble(scale);
        return new Location(newPosition, newRotation);
    }

    public Location add(double dx, double dy, double da) {
        return new Location(position.add(dx, dy), rotation + da);
    }


    public Location limit(double min, double max) {
        Position limitedPosition = new Position(
                Math.min(max, Math.max(min, position().x())),
                Math.min(max, Math.max(min, position().y())));
        return new Location(limitedPosition, rotation);
    }

}

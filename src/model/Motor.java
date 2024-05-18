package model;

public class Motor {
    public static final double MAX_VELOCITY = 3.0;
    public static final double ACCELERATION = 0.2;

    private final double angle;
    private double velocity = 0;

    public Motor(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setVelocity(double velocity) {
        if (velocity > this.velocity) {
            this.velocity = Math.min(MAX_VELOCITY, Math.min(velocity, this.velocity + ACCELERATION));
        } else if (velocity < this.velocity) {
            this.velocity = Math.max(-MAX_VELOCITY, Math.max(velocity, this.velocity - ACCELERATION));
        }
    }

    public double getVelocity() {
        return velocity;
    }

    /*
     * Translate and rotate a given location based on the angle and velocity of this motor
     */
    public Location applyTransforms(Location location) {
        double radians = Math.toRadians(location.rotation() + angle);
        return location.add(velocity * Math.cos(radians), velocity * Math.sin(radians), -velocity);
    }
}

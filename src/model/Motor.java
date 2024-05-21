package model;

public class Motor {
    private final double angle;
    private double velocity = 0;

    public Motor(double angle) {
        this.angle = angle;
    }

    public double getAngle() {
        return angle;
    }

    public void setVelocity(double velocity, Parameters params) {
        if (velocity > this.velocity) {
            this.velocity = Math.min(params.getMaxVelocity(), Math.min(velocity, this.velocity + params.getAcceleration()));
        } else if (velocity < this.velocity) {
            this.velocity = Math.max(-params.getMaxVelocity(), Math.max(velocity, this.velocity - params.getAcceleration()));
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

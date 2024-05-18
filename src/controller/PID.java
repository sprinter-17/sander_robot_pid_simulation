package controller;

import java.util.OptionalDouble;

/*
 * A stateful (non-linear) function that generates a response for a sequence of errors, representing distance for a
 * target value. The function uses 4 configuration values, proportional, derivative, integral and an optional cap.
 * The cap is used to limit the application of the integral term to near steady state.
 */
public class PID {
    public double accumulatedError = 0.0;
    public double previousError = 0.0;
    private Config config = new Config(1.0, 0.0, 0.0, OptionalDouble.of(100.0));

    public void setConfig(Config config) {
        this.config = config;
        reset();
    }

    public void reset() {
        accumulatedError = 0.0;
        previousError = 0.0;
    }

    public double getOutput(double error) {
        accumulatedError += error;
        if (config.integralCap().isPresent() && Math.abs(accumulatedError) > config.integralCap().getAsDouble())
            accumulatedError = Math.signum(accumulatedError) * config.integralCap().getAsDouble();
        double difference = error - previousError;
        previousError = error;
        return config.proportional() * error + config.derivative() * difference + config.integral() * accumulatedError;
    }
}

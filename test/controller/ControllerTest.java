package controller;

import model.Control;
import model.Location;
import model.Position;
import model.Robot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;

import java.util.OptionalDouble;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ControllerTest {
    private final Robot robot = mock(Robot.class);
    private final Controller controller = new Controller(robot);

    @BeforeEach
    void setup() {
        controller.setPID(new Config(1.0, 0.0, 0.0, OptionalDouble.empty()));
    }

    @Test
    void testAtTarget() {
        controller.control(new Location(new Position(50, 50), 180), new Position(50, 50));
        verify(robot).setControl(AdditionalMatchers.cmpEq(Control.ROTATION), AdditionalMatchers.eq(0.0, 0.0001));
    }

}
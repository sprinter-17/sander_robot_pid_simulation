package view;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Control;
import model.Model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KeyHandler {
    private final static Map<KeyCode, ControlVelocity> KEY_MAP = Map.of(
            KeyCode.Q, new ControlVelocity(Control.ROTATION, 1.0),
            KeyCode.E, new ControlVelocity(Control.ROTATION, -1.0),
            KeyCode.W, new ControlVelocity(Control.VERTICAL_MOVEMENT, 1.5),
            KeyCode.S, new ControlVelocity(Control.VERTICAL_MOVEMENT, -1.5),
            KeyCode.A, new ControlVelocity(Control.HORIZONTAL_MOVEMENT, 1.5),
            KeyCode.D, new ControlVelocity(Control.HORIZONTAL_MOVEMENT, -1.5)
    );
    private final Model model;
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public KeyHandler(Model model) {
        this.model = model;
    }

    public void press(KeyEvent event) {
        KeyCode key = event.getCode();
        if (KEY_MAP.containsKey(key)) {
            activeKeys.add(key);
            ControlVelocity control = KEY_MAP.get(key);
            model.getRobot().setControl(control.control(), control.velocity());
        } else if (key == KeyCode.SPACE) {
            model.toggleControllerActive();
        }
    }

    public void release(KeyEvent event) {
        KeyCode key = event.getCode();
        if (KEY_MAP.containsKey(key)) {
            activeKeys.remove(key);
            model.getRobot().clearControl(KEY_MAP.get(key).control());
        }
    }

    public void performControls() {
        if (!activeKeys.isEmpty()) {
            model.getRobot().clearAllControls();
            activeKeys.forEach(key -> model.getRobot().setControl(KEY_MAP.get(key).control(), KEY_MAP.get(key).velocity()));
        }
    }

    private record ControlVelocity(Control control, Double velocity) {
    }

}

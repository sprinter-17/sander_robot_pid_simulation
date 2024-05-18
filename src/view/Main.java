package view;

import controller.Config;
import controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import model.Control;
import model.Model;
import model.Robot;
import model.Location;

import java.util.*;

public class Main extends Application {
    private final static Map<KeyCode, ControlVelocity> KEY_MAP = Map.of(
            KeyCode.Q, new ControlVelocity(Control.ROTATION, 1.0),
            KeyCode.E, new ControlVelocity(Control.ROTATION, -1.0),
            KeyCode.W, new ControlVelocity(Control.VERTICAL_MOVEMENT, 1.5),
            KeyCode.S, new ControlVelocity(Control.VERTICAL_MOVEMENT, -1.5),
            KeyCode.A, new ControlVelocity(Control.HORIZONTAL_MOVEMENT, 1.5),
            KeyCode.D, new ControlVelocity(Control.HORIZONTAL_MOVEMENT, -1.5)
    );
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final Random random = new Random();
    private final Model model = new Model(random, 800);
    private final Controller controller = new Controller(model.getRobot());
    private final WorldCanvas canvas = new WorldCanvas(model);
    private final Slider jitterControl = new Slider(0.0, 10.0, 0.0);
    private final Slider sensorErrorControl = new Slider(0, .1, 0);
    private final ToggleButton controllerButton = new ToggleButton("Controller");
    private final Slider proportionalControl = new Slider(0, .1, .05);
    private final Slider derivativeControl = new Slider(0, 1, 0);
    private final Slider integralControl = new Slider(0, .1, 0);
    private final TextField integralCap = new TextField("100");
    private final Button exitButton = new Button("Exit");
    private final Timeline timer = new Timeline();
    private final VBox tools = new VBox();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PID");
        setupTools();
        BorderPane root = setupBorderPane();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        setPID_Config();
        setupTimer();
    }

    private void setupTools() {
        exitButton.setOnAction(_ -> Platform.exit());
        controllerButton.setSelected(false);
        controllerButton.setOnAction(_ -> setControllerAction());
        jitterControl.valueProperty().addListener((_, _, val) -> model.setJitter(val.doubleValue()));
        proportionalControl.valueProperty().addListener((_, _, _) -> setPID_Config());
        derivativeControl.valueProperty().addListener((_, _, _) -> setPID_Config());
        integralControl.valueProperty().addListener((_, _, _) -> setPID_Config());
        integralCap.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        integralCap.textProperty().addListener((_, _, _) -> setPID_Config());
        tools.setSpacing(5);
        tools.setPadding(new Insets(10));
        tools.setBorder(Border.stroke(Color.BLACK));
        tools.setPrefWidth(150);
        tools.getChildren().addAll(
                new Label("Jitter"), jitterControl,
                new Label("Sensor Error"), sensorErrorControl,
                new Separator(), controllerButton,
                new Label("Proportional"), proportionalControl,
                new Label("Derivative"), derivativeControl,
                new Label("Integral"), integralControl,
                new Label("Cap"), integralCap,
                new Separator(), exitButton);
    }

    private BorderPane setupBorderPane() {
        BorderPane root = new BorderPane();
        root.setOnKeyPressed(this::onKeyPress);
        root.setOnKeyReleased(this::onKeyRelease);
        root.setRight(tools);
        root.setCenter(canvas);
        return root;
    }

    private void setupTimer() {
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.getKeyFrames().add(new KeyFrame(Duration.millis(20), _ -> tick()));
        timer.play();
    }

    private void setControllerAction() {
        if (!controllerButton.isSelected()) {
            model.getRobot().clearAllControls();
        }
        controller.resetPID();
    }

    private void onKeyPress(KeyEvent event) {
        KeyCode key = event.getCode();
        if (KEY_MAP.containsKey(key)) {
            activeKeys.add(key);
            ControlVelocity control = KEY_MAP.get(key);
            model.getRobot().setControl(control.control(), control.velocity());
        }
    }

    private void onKeyRelease(KeyEvent event) {
        KeyCode key = event.getCode();
        if (KEY_MAP.containsKey(key)) {
            activeKeys.remove(key);
            model.getRobot().clearControl(KEY_MAP.get(key).control());
        }
    }

    private void setPID_Config() {
        double proportional = proportionalControl.getValue();
        double derivative = derivativeControl.getValue();
        double integral = integralControl.getValue();
        OptionalDouble cap = integralCap.getText().isEmpty() ? OptionalDouble.empty() : OptionalDouble.of(Double.parseDouble(integralCap.getText()));
        controller.setPID(new Config(proportional, derivative, integral, cap));
    }

    private void tick() {
        Robot robot = model.getRobot();
        robot.clearAllControls();
        if (!activeKeys.isEmpty()) {
            activeKeys.forEach(key -> model.getRobot().setControl(KEY_MAP.get(key).control(), KEY_MAP.get(key).velocity()));
        } else if (controllerButton.isSelected()) {
            Location reading = model.getRobotLocation().addError(random, jitterControl.getValue());
            controller.control(reading, model.getTarget().position());
        }
        model.update();
        canvas.draw();
    }

    private record ControlVelocity(Control control, Double velocity) {
    }
}

package view;

import controller.Config;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.DoubleStringConverter;
import model.Model;
import model.Parameters;

import java.util.OptionalDouble;

public class ToolBox extends VBox {
    private final Model model;
    private final Parameters params;

    private final Slider jitterControl = new Slider(0.0, 10.0, 0.0);
    private final Slider sensorErrorControl = new Slider(0, .1, 0);
    private final Slider maxVelocityControl;
    private final Slider accelerationControl;
    private final ToggleButton controllerButton = new ToggleButton("Controller");
    private final Slider proportionalControl = new Slider(0, .1, .05);
    private final Slider derivativeControl = new Slider(0, 1, 0);
    private final Slider integralControl = new Slider(0, .1, 0);
    private final TextField integralCap = new TextField("100");
    private final Slider targetSpeedControl;
    private final Button exitButton = new Button("Exit");


    public ToolBox(Model model, Parameters params) {
        super(5);
        this.model = model;
        this.params = params;
        maxVelocityControl = new Slider(1.0, 5.0, params.getMaxVelocity());
        accelerationControl = new Slider(0.01, 2.0, params.getAcceleration());
        targetSpeedControl = new Slider(-0.02, 0.02, params.getTargetSpeed());
    }

    public void setup() {
        setPadding(new Insets(10));
        setBorder(Border.stroke(Color.BLACK));
        setPrefWidth(150);

        getChildren().addAll(
                new Label("Jitter"), jitterControl,
                new Label("Sensor Error"), sensorErrorControl,
                new Label("Max Motor Velocity"), maxVelocityControl,
                new Label("Motor Acceleration"), accelerationControl,
                new Separator(), controllerButton,
                new Label("Proportional"), proportionalControl,
                new Label("Derivative"), derivativeControl,
                new Label("Integral"), integralControl,
                new Label("Cap"), integralCap,
                new Label("Target Velocity"), targetSpeedControl,
                new Separator(), exitButton);

        exitButton.setOnAction(_ -> Platform.exit());
        controllerButton.setSelected(false);
        controllerButton.setOnAction(_ -> setControllerAction());
        jitterControl.valueProperty().addListener((_, _, val) -> params.setJitter(val.doubleValue()));
        maxVelocityControl.valueProperty().addListener((_, _, val) -> params.setMaxVelocity(val.doubleValue()));
        accelerationControl.valueProperty().addListener((_, _, val) -> params.setAcceleration(val.doubleValue()));
        proportionalControl.valueProperty().addListener((_, _, _) -> setPID_Config());
        derivativeControl.valueProperty().addListener((_, _, _) -> setPID_Config());
        integralControl.valueProperty().addListener((_, _, _) -> setPID_Config());
        integralCap.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        integralCap.textProperty().addListener((_, _, _) -> setPID_Config());
        targetSpeedControl.valueProperty().addListener((_, _, val) -> params.setTargetSpeed(val.doubleValue()));

        setPID_Config();
    }

    public Parameters parameters() {
        return params;
    }

    private void setControllerAction() {
        model.setControllerActive(controllerButton.isSelected());
    }

    private void setPID_Config() {
        double proportional = proportionalControl.getValue();
        double derivative = derivativeControl.getValue();
        double integral = integralControl.getValue();
        OptionalDouble cap = integralCap.getText().isEmpty() ? OptionalDouble.empty() : OptionalDouble.of(Double.parseDouble(integralCap.getText()));
        model.controller().setPID(new Config(proportional, derivative, integral, cap));
    }
}

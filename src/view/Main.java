package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Model;

import java.util.Random;

public class Main extends Application {
    public static final double WORLD_SIZE = 800;

    private final Random random = new Random();
    private final model.Parameters params = new model.Parameters();
    private final Model model = new Model(random, params, WORLD_SIZE);
    private final ToolBox tools = new ToolBox(model, params);
    private final KeyHandler keyHandler = new KeyHandler(model);
    private final WorldCanvas canvas = new WorldCanvas(model, model.worldSize());
    private final Timeline timer = new Timeline();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sander Robot PID");
        tools.setup();
        BorderPane root = setupBorderPane();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        setupTimer();
    }

    private BorderPane setupBorderPane() {
        BorderPane root = new BorderPane();
        root.setOnKeyPressed(keyHandler::press);
        root.setOnKeyReleased(keyHandler::release);
        root.setRight(tools);
        root.setCenter(canvas);
        return root;
    }

    private void setupTimer() {
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.getKeyFrames().add(new KeyFrame(Duration.millis(20), _ -> tick()));
        timer.play();
    }

    private void tick() {
        keyHandler.performControls();
        model.update();
        canvas.draw();
    }
}

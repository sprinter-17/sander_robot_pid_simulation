package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import model.Model;
import model.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class WorldCanvas extends Canvas {
    private static final double ROBOT_SIZE = 50;
    private static final double AXLE_LENGTH = ROBOT_SIZE * .6;
    private static final double WHEEL_LENGTH = ROBOT_SIZE * 1.1;
    private static final double WHEEL_WIDTH = ROBOT_SIZE * .4;
    private final Model model;
    private final Map<Integer, Double> wheelPositions = new HashMap<>();

    public WorldCanvas(Model model, double worldSize) {
        super(worldSize, worldSize);
        this.model = model;
        IntStream.range(0, 3).forEach(wheel -> wheelPositions.put(wheel, 0.0));
        draw();
    }

    public void draw() {
        GraphicsContext gr = getGraphicsContext2D();
        drawBackground(gr);
        drawRobot(gr);
        drawTarget(gr);
    }

    private void drawBackground(GraphicsContext gr) {
        gr.setFill(Color.LIGHTGRAY);
        gr.fillRect(0, 0, getHeight(), getWidth());
    }

    public void drawRobot(GraphicsContext gr) {
        Affine old = gr.getTransform();
        gr.translate(model.getRobotLocation().x(), model.getRobotLocation().y());
        gr.rotate(model.getRobotLocation().rotation());
        drawBody(gr);
        drawHead(gr);
        drawWheels(gr);
        gr.setTransform(old);
    }

    private static void drawBody(GraphicsContext gr) {
        gr.setFill(Color.DARKOLIVEGREEN);
        gr.fillOval(- ROBOT_SIZE / 2, - ROBOT_SIZE / 2, ROBOT_SIZE, ROBOT_SIZE);
    }

    private static void drawHead(GraphicsContext gr) {
        gr.setStroke(Color.DARKBLUE);
        gr.setLineWidth(ROBOT_SIZE * .2);
        gr.strokeLine(0, AXLE_LENGTH + WHEEL_WIDTH * 1.45, 0, AXLE_LENGTH * 1.5 + WHEEL_WIDTH);
    }

    private void drawWheels(GraphicsContext gr) {
        gr.rotate(60.0);
        IntStream.range(0, 3).forEach(wheel -> drawWheel(gr, wheel));
    }

    private void drawWheel(GraphicsContext gr, int wheel) {
        gr.rotate(120);
        drawAxle(gr);
        drawTyre(gr);
        drawTreads(gr, wheel);
    }

    private static void drawAxle(GraphicsContext gr) {
        gr.setStroke(Color.GREY);
        gr.setLineWidth(ROBOT_SIZE * .2);
        gr.strokeLine(0, -ROBOT_SIZE / 2, 0, -AXLE_LENGTH);
    }

    private void drawTyre(GraphicsContext gr) {
        gr.setFill(Color.BLACK);
        gr.fillRoundRect(-WHEEL_LENGTH / 2, -AXLE_LENGTH - WHEEL_WIDTH, WHEEL_LENGTH, WHEEL_WIDTH, 2, 2);
    }

    private void drawTreads(GraphicsContext gr, int wheel) {
        gr.setStroke(Color.WHEAT);
        gr.setLineWidth(0.8);
        for (double angle = 0; angle < Math.PI; angle += .4) {
            double proportion = Math.cos(angle + wheelPositions.get(wheel)) / 2;
            double xPos = WHEEL_LENGTH * proportion;
            gr.strokeLine(xPos, -AXLE_LENGTH - 4, xPos, -AXLE_LENGTH - WHEEL_WIDTH + 4);
            double velocity = model.getRobot().getMotorVelocity(wheel);
            wheelPositions.put(wheel, (wheelPositions.get(wheel) + velocity / 150) % .4);
        }
    }

    private void drawTarget(GraphicsContext gr) {
        Position target = model.getTarget().position();
        gr.setFill(Color.RED);
        gr.fillOval(target.x() - 5, target.y() - 5, 10, 10);
    }
}

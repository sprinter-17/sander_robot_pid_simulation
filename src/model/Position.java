package model;

public record Position(double x, double y) {
    public static final Position ORIGIN = new Position(0.0, 0.0);

    public Position add(double dx, double dy) {
        return new Position(x + dx, y + dy);
    }
}

package models;

import java.awt.*;

public final class Cell {
    private final int x;
    private final int y;
    private Type type;

    public Cell(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }

    public enum Type {
        WALL(Double.POSITIVE_INFINITY, new Color(5, 91, 0, 255)),
        PATH(1, new Color(162, 97, 12, 255)),
        START(1, new Color(0, 0, 255, 255)),
        STEP(1, new Color(0, 78, 200, 255)),
        FINISH(1, new Color(255, 0, 0, 255)),
        SOLUTION(1, new Color(0, 255, 0, 255));

        private final double weight;
        private final Color color;

        Type(double weight, Color color) {
            this.weight = weight;
            this.color = color;
        }

        public double getWeight() {
            return weight;
        }

        public Color getColor() {
            return color;
        }
    }
}

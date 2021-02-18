package models.cells;

import java.awt.*;

public final class NatureCell extends Cell {
    private Type type;

    public NatureCell(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        DIRT(1, new Color(162, 97, 12, 255)),
        GRAVEL(2, new Color(146, 146, 146, 255)),
        SAND(3, new Color(255, 192, 0, 255)),
        WATER(5, new Color(0, 146, 255, 255)),
        WALL(Double.POSITIVE_INFINITY, new Color(5, 91, 0, 255));

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

        @Override
        public String toString() {
            var cost = getWeight() == Double.POSITIVE_INFINITY ? "∞" : String.valueOf(getWeight());
            return name().charAt(0) + name().substring(1).toLowerCase() + " (cost = " + cost + ")";
        }
    }
}

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
        SAND(2, new Color(255, 192, 0, 255)),
        WATER(5, new Color(0, 146, 255, 255)),
        BUSH(Double.POSITIVE_INFINITY, new Color(5, 91, 0, 255)),
        STONE(Double.POSITIVE_INFINITY, new Color(146, 146, 146, 255));

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
            return capitalizeFirstLetter(name());
        }

        private static String capitalizeFirstLetter(String word) {
            return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        }
    }
}

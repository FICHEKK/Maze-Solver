package models;

import java.awt.*;

/**
 * Cell format (from most to least significant bit): <br>
 * <br>
 * Bit 24: North neighbour flag. <br>
 * Bit 25: East neighbour flag. <br>
 * Bit 26: South neighbour flag. <br>
 * Bit 27: West neighbour flag. <br>
 * Bit 28: Is visited flag. <br>
 * <br>
 * 0000 0000 0000 | 0000 0000 0000 | 0 | 0 | 0 | 0 | 0 | 000 <br>
 * unused         | unused         | N | E | S | W | V | unused <br>
 */
public final class Cell {
    public static final int NORTH_NEIGHBOUR = 0b1000_0000;
    public static final int EAST_NEIGHBOUR = 0b0100_0000;
    public static final int SOUTH_NEIGHBOUR = 0b0010_0000;
    public static final int WEST_NEIGHBOUR = 0b0001_0000;

    private static final int IS_VISITED_MASK = 0b0000_1000;

    private final int x;
    private final int y;
    private Type type = Type.WALL;
    private int bits;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
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

    public boolean isVisited() {
        return (bits & IS_VISITED_MASK) != 0;
    }

    public void setVisitedFlag() {
        bits |= IS_VISITED_MASK;
    }

    public void resetVisitedFlag() {
        bits &= ~IS_VISITED_MASK;
    }

    public boolean hasNeighbour(int neighbour) {
        return (bits & neighbour) != 0;
    }

    public void setNeighbourFlag(int neighbour) {
        bits |= neighbour;
    }

    public void resetNeighbourFlag(int neighbour) {
        bits &= ~neighbour;
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

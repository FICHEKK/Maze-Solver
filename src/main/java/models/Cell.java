package models;

/**
 * Cell format (from most to least significant bit): <br>
 * <br>
 * Bits 0 to 12: X coordinate of the cell. <br>
 * Bits 12 to 24: Y coordinate of the cell. <br>
 * Bit 24: North neighbour flag. <br>
 * Bit 25: East neighbour flag. <br>
 * Bit 26: South neighbour flag. <br>
 * Bit 27: West neighbour flag. <br>
 * Bit 28: Is visited flag. <br>
 * Bits 29 to 32: Cell type. <br>
 * <br>
 * 0000 0000 0000 | 0000 0000 0000 | 0 | 0 | 0 | 0 | 0 | 000 <br>
 * X              | Y              | N | E | S | W | V | T <br>
 */
public final class Cell {
    public static final int NORTH_NEIGHBOUR = 0b1000_0000;
    public static final int EAST_NEIGHBOUR = 0b0100_0000;
    public static final int SOUTH_NEIGHBOUR = 0b0010_0000;
    public static final int WEST_NEIGHBOUR = 0b0001_0000;

    public static final int WALL = 0b0000_0000;
    public static final int PATH = 0b0000_0001;
    public static final int START = 0b0000_0010;
    public static final int STEP = 0b0000_0011;
    public static final int FINISH = 0b0000_0100;
    public static final int SOLUTION = 0b0000_0101;

    private static final int TYPE_MASK = 0b0000_0111;
    private static final int IS_VISITED_MASK = 0b0000_1000;

    private static final int X_COORDINATE_MASK = 0xFF_F0_00_00;
    private static final int X_COORDINATE_OFFSET = 20;

    private static final int Y_COORDINATE_MASK = 0x00_0F_FF_00;
    private static final int Y_COORDINATE_OFFSET = 8;

    private static final int MAX_COORDINATE = 0x0F_FF;

    private int bits;

    public int getType() {
        return bits & TYPE_MASK;
    }

    public void setType(int type) {
        bits = (bits & ~TYPE_MASK) | type;
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

    public int getX() {
        return (bits & X_COORDINATE_MASK) >> X_COORDINATE_OFFSET;
    }

    public int getY() {
        return (bits & Y_COORDINATE_MASK) >> Y_COORDINATE_OFFSET;
    }

    public void setX(int x) {
        if (x > MAX_COORDINATE)
            throw new IllegalArgumentException("X coordinate cannot be greater than " + MAX_COORDINATE + ". Was " + x + ".");

        bits = (bits & ~X_COORDINATE_MASK) | (x << X_COORDINATE_OFFSET);
    }

    public void setY(int y) {
        if (y > MAX_COORDINATE)
            throw new IllegalArgumentException("Y coordinate cannot be greater than " + MAX_COORDINATE + ". Was " + y + ".");

        bits = (bits & ~Y_COORDINATE_MASK) | (y << Y_COORDINATE_OFFSET);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return getX() == cell.getX() && getY() == cell.getY();
    }

    @Override
    public int hashCode() {
        return getX() * 31 + getY();
    }
}

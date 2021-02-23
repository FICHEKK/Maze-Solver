package models.cells;

import java.awt.*;

public class WaypointCell extends Cell {
    private Type type;

    public WaypointCell(int x, int y, Type type) {
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
        START(new Color(0, 65, 193, 255)),
        FINISH(new Color(164, 0, 181, 255));

        private final Color color;

        Type(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}

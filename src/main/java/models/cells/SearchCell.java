package models.cells;

import java.awt.*;

public final class SearchCell extends Cell {
    private Type type;

    public SearchCell(int x, int y, Type type) {
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
        UNUSED(new Color(0, 0, 0, 0)),
        START(new Color(0, 65, 193, 255)),
        STEP(new Color(0, 103, 253, 255)),
        FINISH(new Color(164, 0, 181, 255)),
        SOLUTION(new Color(0, 255, 0, 255));

        private final Color color;

        Type(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}

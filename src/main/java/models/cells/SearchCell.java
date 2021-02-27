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

    @Override
    public Color getColor() {
        return type.getColor();
    }

    public enum Type {
        UNUSED(new Color(0, 0, 0, 0)),
        STEP(new Color(0, 0, 0, 63)),
        SOLUTION(new Color(0, 255, 0, 95));

        private final Color color;

        Type(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}

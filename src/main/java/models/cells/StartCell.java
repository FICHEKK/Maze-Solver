package models.cells;

import java.awt.*;

public class StartCell extends Cell {
    private static final Color COLOR = new Color(0, 65, 193, 255);

    public StartCell(int x, int y) {
        super(x, y);
    }

    @Override
    public Color getColor() {
        return COLOR;
    }
}

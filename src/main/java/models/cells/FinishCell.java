package models.cells;

import java.awt.*;

public class FinishCell extends Cell {
    private static final Color COLOR = new Color(164, 0, 181, 255);

    public FinishCell(int x, int y) {
        super(x, y);
    }

    @Override
    public Color getColor() {
        return COLOR;
    }
}

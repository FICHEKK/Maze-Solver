package ui.selection;

import models.cells.TerrainCell;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

public class TerrainCellSelectionEditor extends BasicComboBoxEditor {
    private final TerrainTypePalette palette;
    private TerrainCell.Type setType;

    public TerrainCellSelectionEditor(SelectionModel<TerrainCell.Type> model) {
        this.palette = new TerrainTypePalette(model);
    }

    @Override
    public Component getEditorComponent() {
        return palette;
    }

    @Override
    public void setItem(Object anObject) {
        this.setType = (TerrainCell.Type) anObject;
        palette.repaint();
    }

    @Override
    public Object getItem() {
        return setType;
    }

    private static class TerrainTypePalette extends JComponent {
        private static final int CELL_GAP = 5;
        private static final String EMPTY_PALETTE_TEXT = "No cells are indestructible.";

        private final SelectionModel<TerrainCell.Type> model;

        private TerrainTypePalette(SelectionModel<TerrainCell.Type> model) {
            this.model = model;
        }

        @Override
        protected void paintComponent(Graphics g) {
            final var cellCount = model.getSelectedCount();

            if (cellCount == 0) {
                paintEmptyPaletteText(g);
            }
            else {
                paintPalette(g, cellCount);
            }
        }

        private void paintEmptyPaletteText(Graphics g) {
            final var x = (getWidth() - g.getFontMetrics().stringWidth(EMPTY_PALETTE_TEXT)) / 2;
            final var y = getHeight() / 2 + g.getFontMetrics().getHeight() / 3;

            g.drawString(EMPTY_PALETTE_TEXT, x, y);
        }

        private void paintPalette(Graphics g, int cellCount) {
            final var cellDimension = getCellDimension();
            final var paletteWidth = cellDimension * cellCount + CELL_GAP * (cellCount - 1);

            var x = (getWidth() - paletteWidth) / 2;
            final var y = (getHeight() - cellDimension) / 2;

            for (var type : model.getSelectedItems()) {
                g.setColor(type.getColor());
                g.fillRect(x, y, cellDimension, cellDimension);

                x += cellDimension + CELL_GAP;
            }
        }

        private int getCellDimension() {
            final var typeCount = TerrainCell.Type.values().length;
            final var drawableAreaWidth = getWidth() - (typeCount + 1) * CELL_GAP;
            return drawableAreaWidth / typeCount;
        }
    }
}

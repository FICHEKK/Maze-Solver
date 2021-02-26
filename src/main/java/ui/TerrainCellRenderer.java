package ui;

import models.cells.TerrainCell;

import javax.swing.*;
import java.awt.*;

public class TerrainCellRenderer extends JComponent implements ListCellRenderer<TerrainCell.Type> {
    private static final Color SELECTED_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    private static final Color TEXT_COLOR = Color.BLACK;

    private static final int TEXT_HORIZONTAL_PADDING = 10;
    private static final int TEXT_VERTICAL_PADDING = 5;

    private static final int TERRAIN_CELL_DIMENSION = 15;
    private static final int TERRAIN_CELL_HORIZONTAL_PADDING = 5;

    private TerrainCell.Type terrainCellType;
    private boolean isSelected;
    private final boolean showCost;

    public TerrainCellRenderer(boolean showCost) {
        this.showCost = showCost;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TerrainCell.Type> list, TerrainCell.Type value, int index, boolean isSelected, boolean cellHasFocus) {
        this.isSelected = isSelected;
        setTerrainCellType(value);
        return this;
    }

    public void setTerrainCellType(TerrainCell.Type terrainCellType) {
        this.terrainCellType = terrainCellType;
    }

    @Override
    public Dimension getPreferredSize() {
        final var fontMetrics = getFontMetrics(getFont());
        final var width = fontMetrics.stringWidth(getText()) + TERRAIN_CELL_DIMENSION + (TEXT_HORIZONTAL_PADDING + TERRAIN_CELL_HORIZONTAL_PADDING) * 2;
        final var height = fontMetrics.getHeight() + TEXT_VERTICAL_PADDING * 2;
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected) {
            paintSelectedBackground(g);
        }

        paintText(g);
        paintTerrainCell(g);
    }

    private void paintSelectedBackground(Graphics g) {
        g.setColor(SELECTED_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintText(Graphics g) {
        final var width = getWidth() - TERRAIN_CELL_DIMENSION - TERRAIN_CELL_HORIZONTAL_PADDING * 2;

        final var textWidth = g.getFontMetrics().stringWidth(getText());
        final var x = (width - textWidth) / 2;

        final var textHeight = g.getFontMetrics().getHeight();
        final var y = getHeight() / 2 + textHeight / 3;

        g.setColor(TEXT_COLOR);
        g.drawString(getText(), x, y);
    }

    private void paintTerrainCell(Graphics g) {
        final var x = getWidth() - TERRAIN_CELL_DIMENSION - TERRAIN_CELL_HORIZONTAL_PADDING;
        final var y = (getHeight() - TERRAIN_CELL_DIMENSION) / 2;

        g.setColor(terrainCellType.getColor());
        g.fillRect(x, y, TERRAIN_CELL_DIMENSION, TERRAIN_CELL_DIMENSION);
    }

    private String getText() {
        var cost = terrainCellType.getWeight() == Double.POSITIVE_INFINITY ? "∞" : String.valueOf(terrainCellType.getWeight());
        return terrainCellType.toString() + (showCost ? " (cost = " + cost + ")" : "");
    }
}

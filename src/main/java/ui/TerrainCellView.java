package ui;

import models.cells.TerrainCell;

import javax.swing.*;
import java.awt.*;

public class TerrainCellView extends JComponent {
    private static final Color HOVERED_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    private static final Color TICK_MARK_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR = Color.BLACK;

    private static final int TICK_MARK_DIMENSION = 8;
    private static final int TICK_MARK_HORIZONTAL_PADDING = 5;

    private static final int TEXT_HORIZONTAL_PADDING = 10;
    private static final int TEXT_VERTICAL_PADDING = 5;

    private static final int TERRAIN_CELL_DIMENSION = 15;
    private static final int TERRAIN_CELL_HORIZONTAL_PADDING = 5;

    private TerrainCell.Type terrainCellType;
    private boolean isSelected;
    private boolean isHovered;
    private final boolean showCost;

    public TerrainCellView(boolean showCost) {
        this.showCost = showCost;
    }

    public void setTerrainCellType(TerrainCell.Type terrainCellType) {
        this.terrainCellType = terrainCellType;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
        if (isHovered) {
            paintHoveredBackground(g);
        }

        if (isSelected) {
            paintSelectedTickMark(g);
        }

        paintText(g);
        paintTerrainCell(g);
    }

    private void paintHoveredBackground(Graphics g) {
        g.setColor(HOVERED_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintSelectedTickMark(Graphics g) {
        final var y = (getHeight() - TICK_MARK_DIMENSION) / 2;

        g.setColor(TICK_MARK_COLOR);
        g.fillOval(TICK_MARK_HORIZONTAL_PADDING, y, TICK_MARK_DIMENSION, TICK_MARK_DIMENSION);
    }

    private void paintText(Graphics g) {
        final var tickWidth = TICK_MARK_DIMENSION + TICK_MARK_HORIZONTAL_PADDING * 2;
        final var cellWidth = TERRAIN_CELL_DIMENSION + TERRAIN_CELL_HORIZONTAL_PADDING * 2;
        final var width = getWidth() -  tickWidth - cellWidth;

        final var textWidth = g.getFontMetrics().stringWidth(getText());
        final var x = (width - textWidth) / 2 + tickWidth;

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

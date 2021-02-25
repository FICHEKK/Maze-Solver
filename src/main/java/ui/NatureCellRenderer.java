package ui;

import models.cells.NatureCell;

import javax.swing.*;
import java.awt.*;

public class NatureCellRenderer extends JComponent implements ListCellRenderer<NatureCell.Type> {
    private static final Color SELECTED_BACKGROUND_COLOR = Color.LIGHT_GRAY;
    private static final Color TEXT_COLOR = Color.BLACK;

    private static final int TEXT_HORIZONTAL_PADDING = 10;
    private static final int TEXT_VERTICAL_PADDING = 5;

    private static final int NATURE_CELL_DIMENSION = 15;
    private static final int NATURE_CELL_HORIZONTAL_PADDING = 5;

    private NatureCell.Type natureCellType;
    private boolean isSelected;
    private final boolean showCost;

    public NatureCellRenderer(boolean showCost) {
        this.showCost = showCost;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends NatureCell.Type> list, NatureCell.Type value, int index, boolean isSelected, boolean cellHasFocus) {
        this.isSelected = isSelected;
        setNatureCellType(value);
        return this;
    }

    public void setNatureCellType(NatureCell.Type natureCellType) {
        this.natureCellType = natureCellType;
    }

    @Override
    public Dimension getPreferredSize() {
        final var fontMetrics = getFontMetrics(getFont());
        final var width = fontMetrics.stringWidth(getText()) + NATURE_CELL_DIMENSION + (TEXT_HORIZONTAL_PADDING + NATURE_CELL_HORIZONTAL_PADDING) * 2;
        final var height = fontMetrics.getHeight() + TEXT_VERTICAL_PADDING * 2;
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected) {
            paintSelectedBackground(g);
        }

        paintText(g);
        paintNatureCell(g);
    }

    private void paintSelectedBackground(Graphics g) {
        g.setColor(SELECTED_BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintText(Graphics g) {
        final var width = getWidth() - NATURE_CELL_DIMENSION - NATURE_CELL_HORIZONTAL_PADDING * 2;

        final var textWidth = g.getFontMetrics().stringWidth(getText());
        final var x = (width - textWidth) / 2;

        final var textHeight = g.getFontMetrics().getHeight();
        final var y = getHeight() / 2 + textHeight / 3;

        g.setColor(TEXT_COLOR);
        g.drawString(getText(), x, y);
    }

    private void paintNatureCell(Graphics g) {
        final var x = getWidth() - NATURE_CELL_DIMENSION - NATURE_CELL_HORIZONTAL_PADDING;
        final var y = (getHeight() - NATURE_CELL_DIMENSION) / 2;

        g.setColor(natureCellType.getColor());
        g.fillRect(x, y, NATURE_CELL_DIMENSION, NATURE_CELL_DIMENSION);
    }

    private String getText() {
        var cost = natureCellType.getWeight() == Double.POSITIVE_INFINITY ? "∞" : String.valueOf(natureCellType.getWeight());
        return natureCellType.toString() + (showCost ? " (cost = " + cost + ")" : "");
    }
}

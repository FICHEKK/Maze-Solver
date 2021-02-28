package ui.selection;

import models.cells.TerrainCell;
import ui.TerrainCellView;

import javax.swing.*;
import java.awt.*;

public class TerrainCellSelectionRenderer extends TerrainCellView implements ListCellRenderer<TerrainCell.Type> {
    private final SelectionModel<TerrainCell.Type> model;

    public TerrainCellSelectionRenderer(boolean showCost, SelectionModel<TerrainCell.Type> model) {
        super(showCost);
        this.model = model;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TerrainCell.Type> list, TerrainCell.Type value, int index, boolean isSelected, boolean cellHasFocus) {
        setHovered(isSelected);
        setSelected(model.isSelected(value));
        setTerrainCellType(value);
        return this;
    }
}

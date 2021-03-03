package edits;

import models.MazeHolder;
import models.cells.TerrainCell;

import java.util.Objects;
import java.util.Set;

public class BrushStrokeEdit implements Edit {
    private final Set<TerrainCell> cellsBeforeStroke;
    private final TerrainCell.Type brushType;

    public BrushStrokeEdit(Set<TerrainCell> cellsBeforeStroke, TerrainCell.Type brushType) {
        this.cellsBeforeStroke = Objects.requireNonNull(cellsBeforeStroke);
        this.brushType = Objects.requireNonNull(brushType);
    }

    @Override
    public void executeRedo() {
        final var maze = MazeHolder.getInstance().getMaze();

        for (var cell : cellsBeforeStroke) {
            maze.setTerrainCell(cell.getX(), cell.getY(), brushType);
        }
    }

    @Override
    public void executeUndo() {
        final var maze = MazeHolder.getInstance().getMaze();

        for (var cell : cellsBeforeStroke) {
            maze.setTerrainCell(cell.getX(), cell.getY(), cell.getType());
        }
    }
}

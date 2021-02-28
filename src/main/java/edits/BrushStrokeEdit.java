package edits;

import models.MazeHolder;
import models.cells.TerrainCell;

import java.util.Objects;
import java.util.Set;

public class BrushStrokeEdit implements Edit {
    private final MazeHolder mazeHolder;
    private final TerrainCell.Type brushType;
    private final Set<TerrainCell> cellsBeforeStroke;

    public BrushStrokeEdit(MazeHolder mazeHolder, TerrainCell.Type brushType, Set<TerrainCell> cellsBeforeStroke) {
        this.mazeHolder = Objects.requireNonNull(mazeHolder);
        this.brushType = Objects.requireNonNull(brushType);
        this.cellsBeforeStroke = Objects.requireNonNull(cellsBeforeStroke);
    }

    @Override
    public void executeRedo() {
        final var maze = mazeHolder.getMaze();

        for (var cell : cellsBeforeStroke) {
            maze.setTerrainCell(cell.getX(), cell.getY(), brushType);
        }
    }

    @Override
    public void executeUndo() {
        final var maze = mazeHolder.getMaze();

        for (var cell : cellsBeforeStroke) {
            maze.setTerrainCell(cell.getX(), cell.getY(), cell.getType());
        }
    }
}

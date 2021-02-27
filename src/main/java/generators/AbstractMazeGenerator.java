package generators;

import models.Maze;
import models.cells.FinishCell;
import models.cells.StartCell;
import models.cells.TerrainCell;

import java.util.Random;

public abstract class AbstractMazeGenerator implements MazeGenerator {
    protected static final Random RANDOM = new Random();
    protected static final TerrainCell.Type PATH_TYPE = TerrainCell.Type.DIRT;
    protected static final TerrainCell.Type WALL_TYPE = TerrainCell.Type.BUSH;

    protected static final int GAP_SIZE = 2;
    protected static final int FIRST_ROW = 1;
    protected static final int FIRST_COLUMN = 1;
    protected static final int SECOND_ROW = FIRST_ROW + GAP_SIZE;
    protected static final int SECOND_COLUMN = FIRST_COLUMN + GAP_SIZE;

    protected TerrainCell[][] terrainCells;
    protected int width;
    protected int height;

    @Override
    public Maze generate(int widthWithoutWalls, int heightWithoutWalls) {
        createMazeMadeFullyOutOfWalls(widthWithoutWalls, heightWithoutWalls);
        carveOutMazePath();
        return new Maze(terrainCells, new StartCell(FIRST_ROW, FIRST_COLUMN), new FinishCell(width - GAP_SIZE, height - GAP_SIZE));
    }

    private void createMazeMadeFullyOutOfWalls(int widthWithoutWalls, int heightWithoutWalls) {
        this.width = widthWithoutWalls * 2 + 1;
        this.height = heightWithoutWalls * 2 + 1;
        this.terrainCells = new TerrainCell[height][width];

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                terrainCells[row][column] = new TerrainCell(column, row, WALL_TYPE);
            }
        }
    }

    protected abstract void carveOutMazePath();

    @Override
    public String toString() {
        return "Abstract Maze Generator";
    }
}

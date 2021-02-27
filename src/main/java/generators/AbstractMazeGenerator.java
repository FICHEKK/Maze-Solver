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

    protected TerrainCell[][] terrainCells;
    protected int width;
    protected int height;

    @Override
    public Maze generate(int widthWithoutWalls, int heightWithoutWalls) {
        createMazeMadeFullyOutOfWalls(widthWithoutWalls, heightWithoutWalls);
        carveOutMazePath();
        return new Maze(terrainCells, new StartCell(1, 1), new FinishCell(width - 2, height - 2));
    }

    private void createMazeMadeFullyOutOfWalls(int widthWithoutWalls, int heightWithoutWalls) {
        this.width = widthWithoutWalls * 2 + 1;
        this.height = heightWithoutWalls * 2 + 1;
        this.terrainCells = new TerrainCell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainCells[y][x] = new TerrainCell(x, y, WALL_TYPE);
            }
        }
    }

    protected abstract void carveOutMazePath();

    @Override
    public String toString() {
        return "Abstract Maze Generator";
    }
}

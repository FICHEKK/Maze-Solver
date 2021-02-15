package models;

import java.util.Objects;

public class Maze {
    private final Cell[][] grid;
    private final int width;
    private final int height;

    public Maze(Cell[][] grid, int width, int height) {
        this.grid = Objects.requireNonNull(grid);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        return grid[y][x];
    }
}

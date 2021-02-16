package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Maze {
    private final Cell[][] grid;
    private final int width;
    private final int height;

    private Cell start;
    private Cell finish;

    private final List<MazeListener> listeners = new ArrayList<>();

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

    public Cell getStart() {
        return start;
    }

    public Cell getFinish() {
        return finish;
    }

    public List<Cell> getNeighbours(Cell cell) {
        var neighbours = new ArrayList<Cell>(4);

        var x = cell.getX();
        var y = cell.getY();

        if (y >= 1)
            neighbours.add(grid[y - 1][x]);

        if (x <= width - 2)
            neighbours.add(grid[y][x + 1]);

        if (y <= height - 2)
            neighbours.add(grid[y + 1][x]);

        if (x >= 1)
            neighbours.add(grid[y][x - 1]);

        return neighbours;
    }

    public void setCell(int x, int y, int cellType) {
        var cellBeingModified = grid[y][x];

        if (cellType == Cell.START) {
            if (cellBeingModified.equals(finish))
                return;

            if (start != null)
                start.setType(Cell.PATH);

            start = cellBeingModified;
        }
        else if (cellType == Cell.FINISH) {
            if (cellBeingModified.equals(start))
                return;

            if (finish != null)
                finish.setType(Cell.PATH);

            finish = cellBeingModified;
        }

        cellBeingModified.setType(cellType);
        listeners.forEach(listener -> listener.onCellChange(x, y));
    }

    public void addListener(MazeListener listener) {
        listeners.add(listener);
    }
}

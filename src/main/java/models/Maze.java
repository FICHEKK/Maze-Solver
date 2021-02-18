package models;

import models.cells.Cell;
import models.cells.NatureCell;
import models.cells.SearchCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Maze {
    private final NatureCell[][] natureCells;
    private final SearchCell[][] searchCells;
    private final int width;
    private final int height;

    private SearchCell start;
    private SearchCell finish;

    private final List<MazeListener> listeners = new ArrayList<>();

    public Maze(NatureCell[][] natureCells, SearchCell[][] searchCells, int width, int height, SearchCell start, SearchCell finish) {
        this.natureCells = Objects.requireNonNull(natureCells);
        this.searchCells = Objects.requireNonNull(searchCells);
        this.width = width;
        this.height = height;
        this.start = Objects.requireNonNull(start);
        this.finish = Objects.requireNonNull(finish);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public NatureCell getNatureCell(int x, int y) {
        return natureCells[y][x];
    }

    public SearchCell getSearchCell(int x, int y) {
        return searchCells[y][x];
    }

    public NatureCell getStart() {
        return getNatureCell(start.getX(), start.getY());
    }

    public NatureCell getFinish() {
        return getNatureCell(finish.getX(), finish.getY());
    }

    public List<NatureCell> getNeighbours(NatureCell cell) {
        var neighbours = new ArrayList<NatureCell>(4);

        var x = cell.getX();
        var y = cell.getY();

        if (y >= 1)
            neighbours.add(natureCells[y - 1][x]);

        if (x <= width - 2)
            neighbours.add(natureCells[y][x + 1]);

        if (y <= height - 2)
            neighbours.add(natureCells[y + 1][x]);

        if (x >= 1)
            neighbours.add(natureCells[y][x - 1]);

        return neighbours;
    }

    public double getManhattanDistanceToFinish(Cell cell) {
        return Math.abs(cell.getX() - finish.getX()) + Math.abs(cell.getY() - finish.getY());
    }

    public void setNatureCell(int x, int y, NatureCell.Type type) {
        natureCells[y][x].setType(type);
        listeners.forEach(MazeListener::onMazeChanged);
    }

    public void setSearchCell(int x, int y, SearchCell.Type type) {
        var cellBeingModified = searchCells[y][x];
        if (cellBeingModified.equals(start) || cellBeingModified.equals(finish)) return;

        if (type == SearchCell.Type.START) {
            start.setType(SearchCell.Type.UNUSED);
            start = cellBeingModified;
        }
        else if (type == SearchCell.Type.FINISH) {
            finish.setType(SearchCell.Type.UNUSED);
            finish = cellBeingModified;
        }

        cellBeingModified.setType(type);
        listeners.forEach(MazeListener::onMazeChanged);
    }

    public void clearSearchLayer() {
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                searchCells[y][x].setType(SearchCell.Type.UNUSED);
            }
        }

        start.setType(SearchCell.Type.START);
        finish.setType(SearchCell.Type.FINISH);

        listeners.forEach(MazeListener::onMazeChanged);
    }

    public void addListener(MazeListener listener) {
        listeners.add(listener);
    }
}

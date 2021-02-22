package models;

import models.cells.Cell;
import models.cells.NatureCell;
import models.cells.SearchCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Maze {
    private static final int[] STRAIGHT_OFFSET_X = new int[] {0, 1, 0, -1, 0};
    private static final int[] STRAIGHT_OFFSET_Y = new int[] {1, 0, -1, 0, 1};
    private static final int[] DIAGONAL_OFFSET_X = new int[] {1, 1, -1, -1};
    private static final int[] DIAGONAL_OFFSET_Y = new int[] {1, -1, -1, 1};

    private static final double SQUARE_ROOT_OF_2 = Math.sqrt(2);

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
        final var neighbours = new ArrayList<NatureCell>();

        for (var i = 0; i < 4; i++) {
            var neighbour = getNeighbour(cell, STRAIGHT_OFFSET_X[i], STRAIGHT_OFFSET_Y[i]);
            if (neighbour == null) continue;
            neighbours.add(neighbour);
        }

        for (var i = 0; i < 4; i++) {
            var neighbour = getNeighbour(cell, DIAGONAL_OFFSET_X[i], DIAGONAL_OFFSET_Y[i]);
            if (neighbour == null) continue;

            var adjacentCell1 = natureCells[cell.getY() + STRAIGHT_OFFSET_Y[i]][cell.getX() + STRAIGHT_OFFSET_X[i]];
            if (adjacentCell1.getType() == NatureCell.Type.WALL) continue;

            var adjacentCell2 = natureCells[cell.getY() + STRAIGHT_OFFSET_Y[i + 1]][cell.getX() + STRAIGHT_OFFSET_X[i + 1]];
            if (adjacentCell2.getType() == NatureCell.Type.WALL) continue;

            neighbours.add(neighbour);
        }

        return neighbours;
    }

    private NatureCell getNeighbour(NatureCell anchor, int offsetX, int offsetY) {
        var x = anchor.getX() + offsetX;
        var y = anchor.getY() + offsetY;
        if (x < 0 || x >= width || y < 0 || y >= height) return null;

        var neighbour = natureCells[y][x];
        return neighbour.getType() != NatureCell.Type.WALL ? neighbour : null;
    }

    public double getDiagonalManhattanDistanceToFinish(Cell cell) {
        var dx = Math.abs(cell.getX() - finish.getX());
        var dy = Math.abs(cell.getY() - finish.getY());
        return Math.min(dx, dy) * SQUARE_ROOT_OF_2 + Math.abs(dx - dy);
    }

    public void setNatureCell(int x, int y, NatureCell.Type type) {
        var cellBeingModified = natureCells[y][x];
        if (cellBeingModified.getType() == type) return;

        cellBeingModified.setType(type);
        listeners.forEach(listener -> listener.onSingleCellChanged(x, y));
    }

    public void setSearchCell(int x, int y, SearchCell.Type type) {
        var cellBeingModified = searchCells[y][x];
        if (cellBeingModified.getType() == type || cellBeingModified.equals(start) || cellBeingModified.equals(finish)) return;

        if (type == SearchCell.Type.START) {
            start.setType(SearchCell.Type.UNUSED);
            listeners.forEach(listener -> listener.onSingleCellChanged(start.getX(), start.getY()));
            start = cellBeingModified;
        }
        else if (type == SearchCell.Type.FINISH) {
            finish.setType(SearchCell.Type.UNUSED);
            listeners.forEach(listener -> listener.onSingleCellChanged(finish.getX(), finish.getY()));
            finish = cellBeingModified;
        }

        cellBeingModified.setType(type);
        listeners.forEach(listener -> listener.onSingleCellChanged(cellBeingModified.getX(), cellBeingModified.getY()));
    }

    public void clearSearchLayer() {
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                searchCells[y][x].setType(SearchCell.Type.UNUSED);
            }
        }

        start.setType(SearchCell.Type.START);
        finish.setType(SearchCell.Type.FINISH);

        listeners.forEach(MazeListener::onMultipleCellsChanged);
    }

    public void addListener(MazeListener listener) {
        listeners.add(listener);
    }
}

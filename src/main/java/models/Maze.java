package models;

import models.cells.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Maze {
    private static final int[] STRAIGHT_OFFSET_X = new int[] {0, 1, 0, -1, 0};
    private static final int[] STRAIGHT_OFFSET_Y = new int[] {1, 0, -1, 0, 1};
    private static final int[] DIAGONAL_OFFSET_X = new int[] {1, 1, -1, -1};
    private static final int[] DIAGONAL_OFFSET_Y = new int[] {1, -1, -1, 1};

    private static final double SQUARE_ROOT_OF_2 = Math.sqrt(2);

    private final TerrainCell[][] terrainCells;
    private final SearchCell[][] searchCells;
    private final int width;
    private final int height;

    private StartCell start;
    private FinishCell finish;

    private final List<MazeListener> listeners = new ArrayList<>();

    public Maze(TerrainCell[][] terrainCells, StartCell start, FinishCell finish) {
        this.terrainCells = Objects.requireNonNull(terrainCells);
        this.start = Objects.requireNonNull(start);
        this.finish = Objects.requireNonNull(finish);

        this.width = terrainCells[0].length;
        this.height = terrainCells.length;
        this.searchCells = new SearchCell[height][width];

        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                if (terrainCells[y][x] == null)
                    throw new NullPointerException("Terrain cell cannot be null.");

                searchCells[y][x] = new SearchCell(x, y, SearchCell.Type.UNUSED);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TerrainCell getTerrainCell(int x, int y) {
        return terrainCells[y][x];
    }

    public TerrainCell getTerrainCellAtStart() {
        return terrainCells[start.getY()][start.getX()];
    }

    public TerrainCell getTerrainCellAtFinish() {
        return terrainCells[finish.getY()][finish.getX()];
    }

    public SearchCell getSearchCell(int x, int y) {
        return searchCells[y][x];
    }

    public StartCell getStart() {
        return start;
    }

    public FinishCell getFinish() {
        return finish;
    }

    public List<TerrainCell> getFourAdjacentNeighbours(TerrainCell cell) {
        return getFourNeighbours(cell, STRAIGHT_OFFSET_X, STRAIGHT_OFFSET_Y);
    }

    public List<TerrainCell> getFourDiagonalNeighbours(TerrainCell cell) {
        return getFourNeighbours(cell, DIAGONAL_OFFSET_X, DIAGONAL_OFFSET_Y);
    }

    private List<TerrainCell> getFourNeighbours(TerrainCell cell, int[] offsetX, int[] offsetY) {
        final var neighbours = new ArrayList<TerrainCell>();

        for (var i = 0; i < 4; i++) {
            var neighbour = getNeighbour(cell, offsetX[i], offsetY[i]);
            if (neighbour == null) continue;
            neighbours.add(neighbour);
        }

        return neighbours;
    }

    public List<TerrainCell> getEightNeighboursConsideringTraversal(TerrainCell cell) {
        final var neighbours = new ArrayList<TerrainCell>();

        for (var i = 0; i < 4; i++) {
            var neighbour = getNeighbourConsideringTraversal(cell, STRAIGHT_OFFSET_X[i], STRAIGHT_OFFSET_Y[i]);
            if (neighbour == null) continue;
            neighbours.add(neighbour);
        }

        for (var i = 0; i < 4; i++) {
            var neighbour = getNeighbourConsideringTraversal(cell, DIAGONAL_OFFSET_X[i], DIAGONAL_OFFSET_Y[i]);
            if (neighbour == null) continue;

            var adjacentCell1 = terrainCells[cell.getY() + STRAIGHT_OFFSET_Y[i]][cell.getX() + STRAIGHT_OFFSET_X[i]];
            if (!adjacentCell1.isTraversable()) continue;

            var adjacentCell2 = terrainCells[cell.getY() + STRAIGHT_OFFSET_Y[i + 1]][cell.getX() + STRAIGHT_OFFSET_X[i + 1]];
            if (!adjacentCell2.isTraversable()) continue;

            neighbours.add(neighbour);
        }

        return neighbours;
    }

    private TerrainCell getNeighbourConsideringTraversal(TerrainCell anchor, int offsetX, int offsetY) {
        final var neighbour = getNeighbour(anchor, offsetX, offsetY);
        if (neighbour == null) return null;
        return neighbour.isTraversable() ? neighbour : null;
    }

    private TerrainCell getNeighbour(TerrainCell anchor, int offsetX, int offsetY) {
        final var x = anchor.getX() + offsetX;
        final var y = anchor.getY() + offsetY;
        return x >= 0 && x < width && y >= 0 && y < height ? terrainCells[y][x] : null;
    }

    public double getDiagonalManhattanDistanceToFinish(Cell cell) {
        var dx = Math.abs(cell.getX() - finish.getX());
        var dy = Math.abs(cell.getY() - finish.getY());
        return Math.min(dx, dy) * SQUARE_ROOT_OF_2 + Math.abs(dx - dy);
    }

    public void setTerrainCell(int x, int y, TerrainCell.Type type) {
        var cellBeingModified = terrainCells[y][x];
        if (cellBeingModified.getType() == type) return;

        cellBeingModified.setType(type);
        listeners.forEach(listener -> listener.onSingleCellChanged(x, y));
    }

    public void setSearchCell(int x, int y, SearchCell.Type type) {
        var cellBeingModified = searchCells[y][x];
        if (cellBeingModified.getType() == type) return;

        cellBeingModified.setType(type);
        listeners.forEach(listener -> listener.onSingleCellChanged(cellBeingModified.getX(), cellBeingModified.getY()));
    }

    public void setStart(int x, int y) {
        final var oldStart = start;
        this.start = new StartCell(x, y);
        listeners.forEach(listener -> listener.onSingleCellChanged(oldStart.getX(), oldStart.getY()));
        listeners.forEach(listener -> listener.onSingleCellChanged(start.getX(), start.getY()));
    }

    public void setFinish(int x, int y) {
        final var oldFinish = finish;
        this.finish = new FinishCell(x, y);
        listeners.forEach(listener -> listener.onSingleCellChanged(oldFinish.getX(), oldFinish.getY()));
        listeners.forEach(listener -> listener.onSingleCellChanged(finish.getX(), finish.getY()));
    }

    public void clearSearchLayer() {
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                searchCells[y][x].setType(SearchCell.Type.UNUSED);
            }
        }

        listeners.forEach(MazeListener::onMultipleCellsChanged);
    }

    public void addListener(MazeListener listener) {
        listeners.add(listener);
    }
}

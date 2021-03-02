package tools;

import edits.BrushStrokeEdit;
import edits.EditManager;
import models.MazeHolder;
import models.cells.TerrainCell;
import search.BreadthFirstSearch;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.*;

public class BucketTool implements Tool {
    private final MazeHolder mazeHolder;
    private final Supplier<TerrainCell.Type> fillTypeSupplier;

    private Set<TerrainCell> modifiedCells;

    public BucketTool(MazeHolder mazeHolder, Supplier<TerrainCell.Type> fillTypeSupplier) {
        this.mazeHolder = mazeHolder;
        this.fillTypeSupplier = fillTypeSupplier;
    }

    @Override
    public void mouseClicked(int cellX, int cellY, MouseEvent mouseEvent) {
        if (!SwingUtilities.isLeftMouseButton(mouseEvent)) return;

        modifiedCells = new HashSet<>();
        fill(cellX, cellY, fillTypeSupplier.get());
        publishEdit();
    }

    private void publishEdit() {
        if (modifiedCells.isEmpty()) return;
        EditManager.getInstance().push(new BrushStrokeEdit(mazeHolder, fillTypeSupplier.get(), modifiedCells));
    }

    private void fill(int cellX, int cellY, TerrainCell.Type fillType) {
        final var maze = mazeHolder.getMaze();
        if (maze == null) return;
        if (cellX < 0 || cellX >= maze.getWidth() || cellY < 0 || cellY >= maze.getHeight()) return;

        final var mouseCell = maze.getTerrainCell(cellX, cellY);
        final var clickedType = mouseCell.getType();
        if (clickedType == fillType) return;

        Supplier<TerrainCell> initial = () -> mouseCell;
        ToDoubleBiFunction<TerrainCell, TerrainCell> weight = (c1, c2) -> 1;
        Predicate<TerrainCell> goal = cell -> false;

        Consumer<TerrainCell> consumer = cell -> {
            modifiedCells.add(new TerrainCell(cell.getX(), cell.getY(), cell.getType()));
            maze.setTerrainCell(cell.getX(), cell.getY(), fillType);
        };

        Function<TerrainCell, List<TerrainCell>> successors = cell -> {
            final var neighbours = maze.getFourAdjacentNeighbours(cell);
            neighbours.removeIf(neighbour -> neighbour.getType() != clickedType);
            return neighbours;
        };

        new BreadthFirstSearch<TerrainCell>().search(initial, consumer, successors, weight, goal);
    }

    @Override
    public String toString() {
        return "Bucket";
    }
}

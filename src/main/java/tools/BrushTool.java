package tools;

import edits.BrushStrokeEdit;
import edits.EditManager;
import models.MazeHolder;
import models.cells.TerrainCell;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class BrushTool implements Tool {
    private static final Random RANDOM = new Random();

    private final Supplier<Integer> radiusSupplier;
    private final Supplier<Double> densitySupplier;
    private final Supplier<TerrainCell.Type> typeSupplier;
    private final Supplier<Set<TerrainCell.Type>> indestructibleTypesSupplier;

    private Set<TerrainCell> modifiedCells;

    public BrushTool(
            Supplier<Integer> radiusSupplier,
            Supplier<Double> densitySupplier,
            Supplier<TerrainCell.Type> typeSupplier,
            Supplier<Set<TerrainCell.Type>> indestructibleTypesSupplier
    ) {
        this.radiusSupplier = radiusSupplier;
        this.densitySupplier = densitySupplier;
        this.typeSupplier = typeSupplier;
        this.indestructibleTypesSupplier = indestructibleTypesSupplier;
    }

    @Override
    public void mousePressed(int cellX, int cellY, MouseEvent mouseEvent) {
        if (!SwingUtilities.isLeftMouseButton(mouseEvent)) return;
        modifiedCells = new HashSet<>();
    }

    @Override
    public void mouseDragged(int cellX, int cellY, MouseEvent mouseEvent) {
        if (!SwingUtilities.isLeftMouseButton(mouseEvent)) return;
        paintMazeUsingCurrentBrushSettings(cellX, cellY, typeSupplier.get());
    }

    @Override
    public void mouseReleased(int cellX, int cellY, MouseEvent mouseEvent) {
        if (!SwingUtilities.isLeftMouseButton(mouseEvent)) return;
        publishEdit();
    }

    @Override
    public void mouseClicked(int cellX, int cellY, MouseEvent mouseEvent) {
        if (!SwingUtilities.isLeftMouseButton(mouseEvent)) return;
        paintMazeUsingCurrentBrushSettings(cellX, cellY, typeSupplier.get());
        publishEdit();
    }

    private void publishEdit() {
        if (modifiedCells.isEmpty()) return;
        EditManager.getInstance().push(new BrushStrokeEdit(modifiedCells, typeSupplier.get()));
    }

    private void paintMazeUsingCurrentBrushSettings(int centerX, int centerY, TerrainCell.Type brushType) {
        final var maze = MazeHolder.getInstance().getMaze();
        final var brushRadius = radiusSupplier.get();
        final var brushDensity = densitySupplier.get();
        final var indestructibleCellTypes = indestructibleTypesSupplier.get();

        for (var y = centerY - brushRadius; y <= centerY + brushRadius; y++) {
            for (var x = centerX - brushRadius; x <= centerX + brushRadius; x++) {
                if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) continue;

                final var cell = maze.getTerrainCell(x, y);

                if (cell.getType() == brushType) continue;
                if (indestructibleCellTypes.contains(cell.getType())) continue;
                if (RANDOM.nextDouble() > brushDensity) continue;
                if (Math.hypot(centerX - x, centerY - y) > brushRadius) continue;

                modifiedCells.add(new TerrainCell(x, y, cell.getType()));
                maze.setTerrainCell(x, y, brushType);
            }
        }
    }

    @Override
    public String toString() {
        return "Brush";
    }
}

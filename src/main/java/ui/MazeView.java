package ui;

import models.Maze;
import models.MazeHolder;
import models.MazeListener;
import models.cells.Cell;
import models.cells.SearchCell;
import models.cells.TerrainCell;
import util.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MazeView extends JComponent {
    private final MazeHolder mazeHolder;
    private TerrainCell.Type outsideCellType = TerrainCell.Type.values()[0];

    public MazeView(MazeHolder mazeHolder) {
        this.mazeHolder = mazeHolder;

        mazeHolder.addListener(maze -> {
            subscribeToNewMaze(maze);
            repaint();
        });
    }

    private void subscribeToNewMaze(Maze maze) {
        maze.addListener(new MazeListener() {
            @Override
            public void onSingleCellChanged(int x, int y) {
                final var cellDimension = getCellDimension();
                if (cellDimension == 0) return;

                final var remainderWidth = (getWidth() - cellDimension * maze.getWidth()) / 2;
                final var remainderHeight = (getHeight() - cellDimension * maze.getHeight()) / 2;

                repaint(x * cellDimension + remainderWidth, y * cellDimension + remainderHeight, cellDimension, cellDimension);
            }

            @Override
            public void onMultipleCellsChanged() {
                repaint();
            }
        });
    }

    public void setOutsideCellType(TerrainCell.Type outsideCellType) {
        if (this.outsideCellType == outsideCellType) return;
        this.outsideCellType = outsideCellType;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        final var maze = mazeHolder.getMaze();
        if (maze == null) return;

        final var cellDimension = getCellDimension();

        if (cellDimension == 0) {
            paintCouldNotRenderMazeMessage(g);
            return;
        }

        final var remainderWidth = (getWidth() - cellDimension * maze.getWidth()) / 2;
        final var remainderHeight = (getHeight() - cellDimension * maze.getHeight()) / 2;

        final var clip = g.getClipBounds();
        final var cellStartX = Math.max(0, (clip.x - remainderWidth) / cellDimension);
        final var cellStartY = Math.max(0, (clip.y - remainderHeight) / cellDimension);

        final var cellCountWidth = (int) Math.ceil((double) clip.width / cellDimension + 1);
        final var cellCountHeight = (int) Math.ceil((double) clip.height / cellDimension + 1);

        final var cellEndX = clip.height == getHeight() ? maze.getWidth() : Math.min(maze.getWidth(), cellStartX + cellCountWidth);
        final var cellEndY = clip.width == getWidth() ? maze.getHeight() : Math.min(maze.getHeight(), cellStartY + cellCountHeight);

        paintOutsideMaze(g, remainderWidth + 1, remainderHeight + 1);
        paintMaze(g, cellDimension, remainderWidth, remainderHeight, cellStartX, cellStartY, cellEndX, cellEndY);
        paintWaypoints(g, cellDimension, remainderWidth, remainderHeight, cellStartX, cellStartY, cellEndX, cellEndY);
    }

    private void paintMaze(Graphics g, int cellDimension, int remainderWidth, int remainderHeight, int cellStartX, int cellStartY, int cellEndX, int cellEndY) {
        final var maze = mazeHolder.getMaze();

        for (var y = cellStartY; y < cellEndY; y++) {
            for (var x = cellStartX; x < cellEndX; x++) {
                final var terrainCellType = maze.getTerrainCell(x, y).getType();
                final var searchCellType = maze.getSearchCell(x, y).getType();

                final var cellX = x * cellDimension + remainderWidth;
                final var cellY = y * cellDimension + remainderHeight;

                g.setColor(terrainCellType.getColor());
                g.fillRect(cellX, cellY, cellDimension, cellDimension);

                if (searchCellType == SearchCell.Type.UNUSED) continue;

                g.setColor(searchCellType.getColor());
                g.fillRect(cellX, cellY, cellDimension, cellDimension);
            }
        }
    }

    private void paintWaypoints(Graphics g, int cellDimension, int remainderWidth, int remainderHeight, int cellStartX, int cellStartY, int cellEndX, int cellEndY) {
        final var maze = mazeHolder.getMaze();
        final var waypoints = new Cell[]{maze.getStart(), maze.getFinish()};

        for (var waypoint : waypoints) {
            final var x = waypoint.getX();
            final var y = waypoint.getY();

            if (x >= cellStartX && x < cellEndX && y >= cellStartY && y < cellEndY) {
                final var cellX = x * cellDimension + remainderWidth;
                final var cellY = y * cellDimension + remainderHeight;

                g.setColor(waypoint.getColor());
                g.fillRect(cellX, cellY, cellDimension, cellDimension);
            }
        }
    }

    private int getCellDimension() {
        final var maze = mazeHolder.getMaze();
        return Math.min(getWidth() / maze.getWidth(), getHeight() / maze.getHeight());
    }

    private void paintCouldNotRenderMazeMessage(Graphics g) {
        final var message = List.of(
                "Maze of this size could not be rendered on your current window.",
                "Try to enlarge your current window or render it on the higher resolution screen."
        );
        final var messageWidth = GraphicsUtils.getMultilineStringWidth(g, message);
        final var messageHeight = GraphicsUtils.getMultilineStringHeight(g, message);
        GraphicsUtils.drawMultilineString(g, message, (getWidth() - messageWidth) / 2, (getHeight() - messageHeight) / 2);
    }

    private void paintOutsideMaze(Graphics g, int outsideMazeWidth, int outsideMazeHeight) {
        g.setColor(outsideCellType.getColor());
        g.fillRect(0, 0, getWidth(), outsideMazeHeight);
        g.fillRect(0, outsideMazeHeight, outsideMazeWidth, getHeight() - outsideMazeHeight * 2);
        g.fillRect(getWidth() - outsideMazeWidth, outsideMazeHeight, outsideMazeWidth, getHeight() - outsideMazeHeight * 2);
        g.fillRect(0, getHeight() - outsideMazeHeight, getWidth(), outsideMazeHeight);
    }
}
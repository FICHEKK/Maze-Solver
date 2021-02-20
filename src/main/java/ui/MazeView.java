package ui;

import models.Maze;
import models.MazeListener;
import models.cells.NatureCell;
import models.cells.SearchCell;
import util.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class MazeView extends JComponent {
    private Maze maze;
    private NatureCell.Type paintBrush = NatureCell.Type.values()[0];

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;

        this.maze.addListener(new MazeListener() {
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseEvent(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseEvent(e);
            }
        });

        repaint();
    }

    public void setPaintBrush(NatureCell.Type paintBrush) {
        this.paintBrush = paintBrush;
    }

    private void handleMouseEvent(MouseEvent event) {
        final var cellDimension = getCellDimension();
        if (cellDimension == 0) return;

        final var remainderWidth = (getWidth() - cellDimension * maze.getWidth()) / 2;
        final var remainderHeight = (getHeight() - cellDimension * maze.getHeight()) / 2;

        final var x = (event.getX() - remainderWidth) / cellDimension;
        final var y = (event.getY() - remainderHeight) / cellDimension;

        if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) return;

        if (SwingUtilities.isLeftMouseButton(event)) {
            maze.setNatureCell(x, y, paintBrush);
        }
        else if (SwingUtilities.isMiddleMouseButton(event)) {
            maze.setSearchCell(x, y, SearchCell.Type.START);
        }
        else if (SwingUtilities.isRightMouseButton(event)) {
            maze.setSearchCell(x, y, SearchCell.Type.FINISH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (maze == null) return;

        final var cellDimension = getCellDimension();

        if (cellDimension == 0) {
            paintCouldNotRenderMazeMessage(g);
            return;
        }

        final var remainderWidth = (getWidth() - cellDimension * maze.getWidth()) / 2;
        final var remainderHeight = (getHeight() - cellDimension * maze.getHeight()) / 2;

        final var clip = getClipRectangle(g);
        final var cellStartX = Math.max(0, (clip.x - remainderWidth) / cellDimension);
        final var cellStartY = Math.max(0, (clip.y - remainderHeight) / cellDimension);

        final var cellCountWidth = (int) Math.ceil((double) clip.width / cellDimension + 1);
        final var cellCountHeight = (int) Math.ceil((double) clip.height / cellDimension + 1);

        final var cellEndX = clip.height == getHeight() ? maze.getWidth() : Math.min(maze.getWidth(), cellStartX + cellCountWidth);
        final var cellEndY = clip.width == getWidth() ? maze.getHeight() : Math.min(maze.getHeight(), cellStartY + cellCountHeight);

        paintOutsideMaze(g, remainderWidth + 1, remainderHeight + 1);

        for (var y = cellStartY; y < cellEndY; y++) {
            for (var x = cellStartX; x < cellEndX; x++) {
                var searchCellType = maze.getSearchCell(x, y).getType();
                g.setColor(searchCellType == SearchCell.Type.UNUSED ? maze.getNatureCell(x, y).getType().getColor() : searchCellType.getColor());
                g.fillRect(x * cellDimension + remainderWidth, y * cellDimension + remainderHeight, cellDimension, cellDimension);
            }
        }
    }

    private int getCellDimension() {
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
        g.setColor(NatureCell.Type.WALL.getColor());
        g.fillRect(0, 0, getWidth(), outsideMazeHeight);
        g.fillRect(0, outsideMazeHeight, outsideMazeWidth, getHeight() - outsideMazeHeight * 2);
        g.fillRect(getWidth() - outsideMazeWidth, outsideMazeHeight, outsideMazeWidth, getHeight() - outsideMazeHeight * 2);
        g.fillRect(0, getHeight() - outsideMazeHeight, getWidth(), outsideMazeHeight);
    }

    private Rectangle getClipRectangle(Graphics g) {
        var rectangle = (Rectangle2D.Double) g.getClip();

        return new Rectangle(
                (int) Math.round(rectangle.x),
                (int) Math.round(rectangle.y),
                getFlooredIfApproximatelyEqualToOriginal(rectangle.width),
                getFlooredIfApproximatelyEqualToOriginal(rectangle.height)
        );
    }

    private int getFlooredIfApproximatelyEqualToOriginal(double value) {
        final var epsilon = 1E-6;
        final var flooredValue = Math.floor(value);
        return value - flooredValue < epsilon ? (int) flooredValue : (int) (flooredValue + 1.0);
    }
}
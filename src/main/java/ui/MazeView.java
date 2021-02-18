package ui;

import models.Maze;
import models.cells.NatureCell;
import models.cells.SearchCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeView extends JComponent {
    private Maze maze;
    private NatureCell.Type paintBrush = NatureCell.Type.WALL;

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.maze.addListener(this::repaint);

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

    private void handleMouseEvent(MouseEvent event) {
        int x = (int) (maze.getWidth() * (event.getX() / (double) getWidth()));
        int y = (int) (maze.getHeight() * (event.getY() / (double) getHeight()));

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

    public void setPaintBrush(NatureCell.Type paintBrush) {
        this.paintBrush = paintBrush;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (maze == null) return;

        var cellWidth = getWidth() / (double) maze.getWidth();
        var cellHeight = getHeight() / (double) maze.getHeight();

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                var searchCellType = maze.getSearchCell(x, y).getType();
                g.setColor(searchCellType == SearchCell.Type.UNUSED ? maze.getNatureCell(x, y).getType().getColor() : searchCellType.getColor());
                g.fillRect((int) (x * cellWidth), (int) (y * cellHeight), (int) Math.ceil(cellWidth), (int) Math.ceil(cellHeight));
            }
        }
    }

    public Maze getMaze() {
        return maze;
    }
}
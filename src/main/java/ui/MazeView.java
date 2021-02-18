package ui;

import models.Cell;
import models.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeView extends JComponent {
    private Maze maze;

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.maze.addListener(this::repaint);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (int) (maze.getWidth() * (e.getX() / (double) getWidth()));
                int y = (int) (maze.getHeight() * (e.getY() / (double) getHeight()));

                if (SwingUtilities.isLeftMouseButton(e)) {
                    maze.setCell(x, y, e.isShiftDown() ? Cell.Type.PATH : Cell.Type.WALL);
                }
                else if (SwingUtilities.isMiddleMouseButton(e)) {
                    maze.setCell(x, y, Cell.Type.START);
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    maze.setCell(x, y, Cell.Type.FINISH);
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = (int) (maze.getWidth() * (e.getX() / (double) getWidth()));
                int y = (int) (maze.getHeight() * (e.getY() / (double) getHeight()));

                if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) return;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    maze.setCell(x, y, e.isShiftDown() ? Cell.Type.PATH : Cell.Type.WALL);
                }
            }
        });

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (maze == null) return;

        var cellWidth = getWidth() / (double) maze.getWidth();
        var cellHeight = getHeight() / (double) maze.getHeight();

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                g.setColor(maze.getCell(x, y).getType().getColor());
                g.fillRect((int) (x * cellWidth), (int) (y * cellHeight), (int) Math.ceil(cellWidth), (int) Math.ceil(cellHeight));
            }
        }
    }

    public Maze getMaze() {
        return maze;
    }
}
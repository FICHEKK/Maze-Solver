package views;

import models.Cell;
import models.Maze;

import javax.swing.*;
import java.awt.*;

public class MazeView extends JComponent {
    private static final Color WALL_COLOR = new Color(5, 91, 0, 255);
    private static final Color PATH_COLOR = Color.WHITE;
    private static final Color START_COLOR = Color.BLUE;
    private static final Color FINISH_COLOR = Color.RED;

    private Maze maze;

    public void setMaze(Maze maze) {
        this.maze = maze;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (maze == null) return;

        var cellWidth = getWidth() / (double) maze.getWidth();
        var cellHeight = getHeight() / (double) maze.getHeight();

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                var cellType = maze.getCell(x, y).getType();

                switch (cellType) {
                    case Cell.WALL -> g.setColor(WALL_COLOR);
                    case Cell.PATH -> g.setColor(PATH_COLOR);
                    case Cell.START -> g.setColor(START_COLOR);
                    case Cell.FINISH -> g.setColor(FINISH_COLOR);
                }

                g.fillRect((int) (x * cellWidth), (int) (y * cellHeight), (int) Math.ceil(cellWidth), (int) Math.ceil(cellHeight));
            }
        }
    }
}
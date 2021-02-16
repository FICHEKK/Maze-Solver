package views;

import models.Cell;
import models.Maze;
import models.MazeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeView extends JComponent implements MazeListener {
    private static final Color WALL_COLOR = new Color(5, 91, 0, 255);
    private static final Color PATH_COLOR = new Color(162, 97, 12, 255);
    private static final Color START_COLOR = Color.BLUE;
    private static final Color STEP_COLOR = new Color(0, 78, 200, 255);
    private static final Color FINISH_COLOR = Color.RED;
    private static final Color SOLUTION_COLOR = Color.GREEN;

    private Maze maze;

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.maze.addListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = (int) (maze.getWidth() * (e.getX() / (double) getWidth()));
                int y = (int) (maze.getHeight() * (e.getY() / (double) getHeight()));
                maze.setCell(x, y, SwingUtilities.isLeftMouseButton(e) ? Cell.START :
                        SwingUtilities.isMiddleMouseButton(e) ? Cell.WALL : Cell.FINISH);
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
                var cellType = maze.getCell(x, y).getType();

                switch (cellType) {
                    case Cell.WALL -> g.setColor(WALL_COLOR);
                    case Cell.PATH -> g.setColor(PATH_COLOR);
                    case Cell.START -> g.setColor(START_COLOR);
                    case Cell.STEP -> g.setColor(STEP_COLOR);
                    case Cell.FINISH -> g.setColor(FINISH_COLOR);
                    case Cell.SOLUTION -> g.setColor(SOLUTION_COLOR);
                }

                g.fillRect((int) (x * cellWidth), (int) (y * cellHeight), (int) Math.ceil(cellWidth), (int) Math.ceil(cellHeight));
            }
        }
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void onCellChange(int x, int y) {
        repaint();
    }
}
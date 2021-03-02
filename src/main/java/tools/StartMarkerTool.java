package tools;

import models.MazeHolder;

import java.awt.event.MouseEvent;

public class StartMarkerTool implements Tool {
    private final MazeHolder mazeHolder;

    public StartMarkerTool(MazeHolder mazeHolder) {
        this.mazeHolder = mazeHolder;
    }

    @Override
    public void mouseClicked(int cellX, int cellY, MouseEvent mouseEvent) {
        setStart(cellX, cellY);
    }

    @Override
    public void mouseDragged(int cellX, int cellY, MouseEvent mouseEvent) {
        setStart(cellX, cellY);
    }

    private void setStart(int cellX, int cellY) {
        final var maze = mazeHolder.getMaze();
        if (cellX < 0 || cellX >= maze.getWidth() || cellY < 0 || cellY >= maze.getHeight()) return;
        mazeHolder.getMaze().setStart(cellX, cellY);
    }

    @Override
    public String toString() {
        return "Start Marker";
    }
}

package tools;

import models.MazeHolder;

import java.awt.event.MouseEvent;

public class FinishMarkerTool implements Tool {
    private final MazeHolder mazeHolder;

    public FinishMarkerTool(MazeHolder mazeHolder) {
        this.mazeHolder = mazeHolder;
    }

    @Override
    public void mouseClicked(int cellX, int cellY, MouseEvent mouseEvent) {
        setFinish(cellX, cellY);
    }

    @Override
    public void mouseDragged(int cellX, int cellY, MouseEvent mouseEvent) {
        setFinish(cellX, cellY);
    }

    private void setFinish(int cellX, int cellY) {
        final var maze = mazeHolder.getMaze();
        if (cellX < 0 || cellX >= maze.getWidth() || cellY < 0 || cellY >= maze.getHeight()) return;
        mazeHolder.getMaze().setFinish(cellX, cellY);
    }

    @Override
    public String toString() {
        return "Finish Marker";
    }
}

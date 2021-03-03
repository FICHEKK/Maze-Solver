package tools;

import models.MazeHolder;

import java.awt.event.MouseEvent;

public class FinishMarkerTool implements Tool {

    @Override
    public void mouseClicked(int cellX, int cellY, MouseEvent mouseEvent) {
        setFinish(cellX, cellY);
    }

    @Override
    public void mouseDragged(int cellX, int cellY, MouseEvent mouseEvent) {
        setFinish(cellX, cellY);
    }

    private void setFinish(int cellX, int cellY) {
        final var maze = MazeHolder.getInstance().getMaze();
        if (cellX < 0 || cellX >= maze.getWidth() || cellY < 0 || cellY >= maze.getHeight()) return;
        maze.setFinish(cellX, cellY);
    }

    @Override
    public String toString() {
        return "Finish Marker";
    }
}

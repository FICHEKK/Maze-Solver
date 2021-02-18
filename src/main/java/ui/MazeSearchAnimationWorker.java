package ui;

import models.Maze;
import models.cells.NatureCell;
import models.cells.SearchCell;

import javax.swing.*;
import java.util.List;

public class MazeSearchAnimationWorker extends SwingWorker<Void, Void> {
    private static final int SEARCH_STEP_DURATION_IN_MS = 10;
    private static final int PATH_STEP_DURATION_IN_MS = 10;

    private final Maze maze;
    private final List<NatureCell> visited;
    private final List<NatureCell> path;

    public MazeSearchAnimationWorker(Maze maze, List<NatureCell> visited, List<NatureCell> path) {
        this.maze = maze;
        this.visited = visited;
        this.path = path;
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        maze.clearSearchLayer();
        animateSearchSteps();

        if (path != null) {
            animatePathSteps();
        }

        return null;
    }

    private void animateSearchSteps() throws InterruptedException {
        for (var cell : visited) {
            maze.setSearchCell(cell.getX(), cell.getY(), SearchCell.Type.STEP);
            Thread.sleep(SEARCH_STEP_DURATION_IN_MS);
        }
    }

    private void animatePathSteps() throws InterruptedException {
        int index = 0;

        for (var cell : path) {
            var type = SearchCell.Type.SOLUTION;

            if (index == 0)
                type = SearchCell.Type.START;

            if (index == path.size() - 1)
                type = SearchCell.Type.FINISH;

            maze.setSearchCell(cell.getX(), cell.getY(), type);
            index++;

            Thread.sleep(PATH_STEP_DURATION_IN_MS);
        }
    }
}

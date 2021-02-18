package ui;

import models.Cell;
import models.Maze;

import javax.swing.*;
import java.util.List;
import java.util.Set;

public class MazeSearchAnimationWorker extends SwingWorker<Void, Void> {
    private static final int SEARCH_STEP_DURATION_IN_MS = 10;
    private static final int PATH_STEP_DURATION_IN_MS = 10;

    private final Maze maze;
    private final List<Cell> visitedCells;
    private final List<Cell> path;

    public MazeSearchAnimationWorker(Maze maze, List<Cell> visitedCells, List<Cell> path) {
        this.maze = maze;
        this.visitedCells = visitedCells;
        this.path = path;
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        clearPreviousSearchIfNeeded();
        animateSearchSteps();

        if (path != null) {
            animatePathSteps();
        }

        return null;
    }

    private void clearPreviousSearchIfNeeded() {
        maze.replaceAllCellsOfType(Set.of(Cell.Type.STEP, Cell.Type.SOLUTION), Cell.Type.PATH);
    }

    private void animateSearchSteps() throws InterruptedException {
        for (var visitedCell : visitedCells) {
            maze.setCell(visitedCell.getX(), visitedCell.getY(), Cell.Type.STEP);
            Thread.sleep(SEARCH_STEP_DURATION_IN_MS);
        }
    }

    private void animatePathSteps() throws InterruptedException {
        int index = 0;

        for (var cell : path) {
            var cellType = Cell.Type.SOLUTION;

            if (index == 0)
                cellType = Cell.Type.START;

            if (index == path.size() - 1)
                cellType = Cell.Type.FINISH;

            maze.setCell(cell.getX(), cell.getY(), cellType);
            index++;

            Thread.sleep(PATH_STEP_DURATION_IN_MS);
        }
    }
}

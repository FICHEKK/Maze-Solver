package ui;

import models.Maze;
import models.cells.TerrainCell;
import models.cells.SearchCell;
import search.SearchNode;
import search.SearchResult;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class MazeSearchAnimationWorker extends SwingWorker<Void, Void> {
    private static final int NO_DELAY = 0;

    private final Maze maze;
    private final SearchResult<TerrainCell> searchResult;
    private final int stepDelay;

    public MazeSearchAnimationWorker(Maze maze, SearchResult<TerrainCell> searchResult, int stepDelay) {
        this.maze = maze;
        this.searchResult = searchResult;
        this.stepDelay = stepDelay;
    }

    @Override
    protected Void doInBackground() throws InterruptedException {
        maze.clearSearchLayer();
        displaySearchResult(stepDelay);
        return null;
    }

    @Override
    protected void done() {
        if (!isCancelled()) return;

        try {
            displaySearchResult(NO_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displaySearchResult(int delayBetweenSteps) throws InterruptedException {
        displaySearchSteps(delayBetweenSteps);

        if (searchResult.getPathHead() != null) {
            displayPathSteps(delayBetweenSteps);
        }
    }

    private void displaySearchSteps(int delayBetweenSteps) throws InterruptedException {
        final var visited = searchResult.getVisited().keySet();

        for (var cell : visited) {
            maze.setSearchCell(cell.getX(), cell.getY(), SearchCell.Type.STEP);

            if (delayBetweenSteps == NO_DELAY) continue;
            Thread.sleep(delayBetweenSteps);
        }
    }

    private void displayPathSteps(int delayBetweenSteps) throws InterruptedException {
        final var path = reconstructPath(searchResult.getPathHead());

        for (var cell : path) {
            maze.setSearchCell(cell.getX(), cell.getY(), SearchCell.Type.SOLUTION);

            if (delayBetweenSteps == NO_DELAY) continue;
            Thread.sleep(delayBetweenSteps);
        }
    }

    private List<TerrainCell> reconstructPath(SearchNode<TerrainCell> pathHead) {
        var path = new LinkedList<TerrainCell>();

        for (var node = pathHead; node != null; node = node.getParent()) {
            path.addFirst(node.getState());
        }

        return path;
    }
}

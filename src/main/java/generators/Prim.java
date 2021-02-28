package generators;

import models.cells.TerrainCell;

import java.util.*;

public class Prim extends AbstractMazeGenerator {

    @Override
    protected void carveOutMazePath() {
        final var frontier = new ArrayList<TerrainCell>();
        frontier.add(terrainCells[FIRST_ROW][FIRST_COLUMN]);

        final var visited = new HashSet<TerrainCell>();

        while (!frontier.isEmpty()) {
            final var randomFrontierCell = frontier.remove(RANDOM.nextInt(frontier.size()));
            if (visited.contains(randomFrontierCell)) continue;

            visitFrontierCell(randomFrontierCell, frontier, visited);
        }
    }

    private void visitFrontierCell(TerrainCell frontierCell, List<TerrainCell> frontier, Set<TerrainCell> visited) {
        frontierCell.setType(PATH_TYPE);
        visited.add(frontierCell);

        final var unvisitedNeighbours = getUnvisitedNeighbours(frontierCell, visited);
        frontier.addAll(unvisitedNeighbours);

        final var visitedNeighbours = getVisitedNeighbours(frontierCell, visited);
        if (visitedNeighbours.isEmpty()) return;

        final var randomNeighbour = visitedNeighbours.get(RANDOM.nextInt(visitedNeighbours.size()));

        final var dx = (randomNeighbour.getX() - frontierCell.getX()) / 2;
        final var dy = (randomNeighbour.getY() - frontierCell.getY()) / 2;

        terrainCells[frontierCell.getY() + dy][frontierCell.getX() + dx].setType(PATH_TYPE);
    }

    private List<TerrainCell> getVisitedNeighbours(TerrainCell anchor, Set<TerrainCell> visited) {
        return getNeighbours(anchor, true, visited);
    }

    private List<TerrainCell> getUnvisitedNeighbours(TerrainCell anchor, Set<TerrainCell> visited) {
        return getNeighbours(anchor, false, visited);
    }

    private List<TerrainCell> getNeighbours(TerrainCell anchor, boolean shouldNeighbourBeVisited, Set<TerrainCell> visited) {
        final var neighbours = new ArrayList<TerrainCell>();
        final var x = anchor.getX();
        final var y = anchor.getY();

        if (y >= 2 && visited.contains(terrainCells[y - 2][x]) == shouldNeighbourBeVisited)
            neighbours.add(terrainCells[y - 2][x]);

        if (x < (width - 2) && visited.contains(terrainCells[y][x + 2]) == shouldNeighbourBeVisited)
            neighbours.add(terrainCells[y][x + 2]);

        if (y < (height - 2) && visited.contains(terrainCells[y + 2][x]) == shouldNeighbourBeVisited)
            neighbours.add(terrainCells[y + 2][x]);

        if (x >= 2 && visited.contains(terrainCells[y][x - 2]) == shouldNeighbourBeVisited)
            neighbours.add(terrainCells[y][x - 2]);

        return neighbours;
    }

    @Override
    public String toString() {
        return "Prim";
    }
}

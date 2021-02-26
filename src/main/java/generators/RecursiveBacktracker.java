package generators;

import models.Maze;
import models.cells.TerrainCell;
import models.cells.WaypointCell;

import java.util.*;

public final class RecursiveBacktracker implements MazeGenerator {
    private static final Random RANDOM = new Random();
    private static final TerrainCell.Type PATH_TYPE = TerrainCell.Type.DIRT;
    private static final TerrainCell.Type WALL_TYPE = TerrainCell.Type.BUSH;

    private int width;
    private int height;
    private TerrainCell[][] terrainCells;

    @Override
    public Maze generate(int widthWithoutWalls, int heightWithoutWalls) {
        createMazeMadeFullyOutOfWalls(widthWithoutWalls, heightWithoutWalls);
        carveOutMazePath();

        final var start = new WaypointCell(1, 1, WaypointCell.Type.START);
        final var finish = new WaypointCell(width - 2, height - 2, WaypointCell.Type.FINISH);

        return new Maze(terrainCells, start, finish);
    }

    private void createMazeMadeFullyOutOfWalls(int widthWithoutWalls, int heightWithoutWalls) {
        width = widthWithoutWalls * 2 + 1;
        height = heightWithoutWalls * 2 + 1;
        terrainCells = new TerrainCell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                terrainCells[y][x] = new TerrainCell(x, y, WALL_TYPE);
            }
        }
    }

    private void carveOutMazePath() {
        final var stack = initializeStack();
        final var visited = new HashSet<TerrainCell>();

        while (!stack.isEmpty()) {
            visitUppermostCell(stack.peek(), stack, visited);
        }
    }

    private Stack<TerrainCell> initializeStack() {
        final var stack = new Stack<TerrainCell>();
        stack.push(terrainCells[1][1]);
        return stack;
    }

    private void visitUppermostCell(TerrainCell cell, Stack<TerrainCell> stack, Set<TerrainCell> visited) {
        cell.setType(PATH_TYPE);
        visited.add(cell);
        moveToRandomValidDirection(cell, stack, getValidDirections(cell.getX(), cell.getY(), visited));
    }

    private void moveToRandomValidDirection(TerrainCell cell, Stack<TerrainCell> stack, List<Direction> validDirections) {
        if (validDirections.isEmpty()) {
            stack.pop();
            return;
        }

        var randomDirection = validDirections.get(RANDOM.nextInt(validDirections.size()));

        var intermediate = terrainCells[cell.getY() + randomDirection.offsetY][cell.getX() + randomDirection.offsetX];
        intermediate.setType(PATH_TYPE);

        var neighbour = terrainCells[cell.getY() + randomDirection.offsetY * 2][cell.getX() + randomDirection.offsetX * 2];
        stack.push(neighbour);
    }

    private List<Direction> getValidDirections(int x, int y, Set<TerrainCell> visited) {
        var validDirections = new ArrayList<Direction>();

        if (y >= 2 && !visited.contains(terrainCells[y - 2][x]))
            validDirections.add(Direction.NORTH);

        if (x < (width - 2) && !visited.contains(terrainCells[y][x + 2]))
            validDirections.add(Direction.EAST);

        if (y < (height - 2) && !visited.contains(terrainCells[y + 2][x]))
            validDirections.add(Direction.SOUTH);

        if (x >= 2 && !visited.contains(terrainCells[y][x - 2]))
            validDirections.add(Direction.WEST);

        return validDirections;
    }

    @Override
    public String toString() {
        return "Recursive Backtracker";
    }
}

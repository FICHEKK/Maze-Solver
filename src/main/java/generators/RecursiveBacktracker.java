package generators;

import models.Maze;
import models.cells.NatureCell;
import models.cells.WaypointCell;

import java.util.*;

public final class RecursiveBacktracker implements MazeGenerator {
    private static final Random RANDOM = new Random();
    private static final NatureCell.Type PATH_TYPE = NatureCell.Type.DIRT;
    private static final NatureCell.Type WALL_TYPE = NatureCell.Type.BUSH;

    private int width;
    private int height;
    private NatureCell[][] natureCells;

    @Override
    public Maze generate(int widthWithoutWalls, int heightWithoutWalls) {
        createMazeMadeFullyOutOfWalls(widthWithoutWalls, heightWithoutWalls);
        carveOutMazePath();

        final var start = new WaypointCell(1, 1, WaypointCell.Type.START);
        final var finish = new WaypointCell(width - 2, height - 2, WaypointCell.Type.FINISH);

        return new Maze(natureCells, start, finish);
    }

    private void createMazeMadeFullyOutOfWalls(int widthWithoutWalls, int heightWithoutWalls) {
        width = widthWithoutWalls * 2 + 1;
        height = heightWithoutWalls * 2 + 1;
        natureCells = new NatureCell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                natureCells[y][x] = new NatureCell(x, y, WALL_TYPE);
            }
        }
    }

    private void carveOutMazePath() {
        final var stack = initializeStack();
        final var visited = new HashSet<NatureCell>();

        while (!stack.isEmpty()) {
            visitUppermostCell(stack.peek(), stack, visited);
        }
    }

    private Stack<NatureCell> initializeStack() {
        final var stack = new Stack<NatureCell>();
        stack.push(natureCells[1][1]);
        return stack;
    }

    private void visitUppermostCell(NatureCell cell, Stack<NatureCell> stack, Set<NatureCell> visited) {
        cell.setType(PATH_TYPE);
        visited.add(cell);
        moveToRandomValidDirection(cell, stack, getValidDirections(cell.getX(), cell.getY(), visited));
    }

    private void moveToRandomValidDirection(NatureCell cell, Stack<NatureCell> stack, List<Direction> validDirections) {
        if (validDirections.isEmpty()) {
            stack.pop();
            return;
        }

        var randomDirection = validDirections.get(RANDOM.nextInt(validDirections.size()));

        var intermediate = natureCells[cell.getY() + randomDirection.offsetY][cell.getX() + randomDirection.offsetX];
        intermediate.setType(PATH_TYPE);

        var neighbour = natureCells[cell.getY() + randomDirection.offsetY * 2][cell.getX() + randomDirection.offsetX * 2];
        stack.push(neighbour);
    }

    private List<Direction> getValidDirections(int x, int y, Set<NatureCell> visited) {
        var validDirections = new ArrayList<Direction>();

        if (y >= 2 && !visited.contains(natureCells[y - 2][x]))
            validDirections.add(Direction.NORTH);

        if (x < (width - 2) && !visited.contains(natureCells[y][x + 2]))
            validDirections.add(Direction.EAST);

        if (y < (height - 2) && !visited.contains(natureCells[y + 2][x]))
            validDirections.add(Direction.SOUTH);

        if (x >= 2 && !visited.contains(natureCells[y][x - 2]))
            validDirections.add(Direction.WEST);

        return validDirections;
    }

    @Override
    public String toString() {
        return "Recursive Backtracker";
    }
}

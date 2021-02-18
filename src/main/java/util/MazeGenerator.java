package util;

import models.Cell;
import models.Maze;

import java.util.*;

public final class MazeGenerator {
    private static final Random RANDOM = new Random();
    private static final int DIRECTION_COUNT = Direction.values().length;

    private final int width;
    private final int height;
    private final double wallDensity;
    private final Cell[][] grid;

    public MazeGenerator(int width, int height, double wallDensity) {
        if (wallDensity < 0 || wallDensity > 1)
            throw new IllegalArgumentException("Wall density must be in range [0, 1].");

        this.width = 2 * width + 1;
        this.height = 2 * height + 1;
        this.wallDensity = wallDensity;
        this.grid = createGrid(this.width, this.height);
    }

    public Maze generate() {
        var stack = new Stack<Cell>();
        stack.push(grid[1][1]);

        var visited = new HashSet<Cell>();

        while (!stack.isEmpty()) {
            var cell = stack.peek();
            visited.add(cell);

            var validDirections = getValidDirections(cell.getX(), cell.getY(), visited);

            if (validDirections.isEmpty()) {
                stack.pop();
                continue;
            }

            var direction = validDirections.get(RANDOM.nextInt(validDirections.size()));
            var neighbour = moveToNeighbour(cell, direction);
            stack.push(neighbour);
        }

        var start = convertRandomPathCellTo(Cell.Type.START);
        var finish = convertRandomPathCellTo(Cell.Type.FINISH);

        removeRandomWalls();
        return new Maze(grid, width, height, start, finish);
    }

    private Cell convertRandomPathCellTo(Cell.Type cellType) {
        Cell cell;

        do {
            int widthWithoutWalls = (width - 1) / 2;
            int heightWithoutWalls = (height - 1) / 2;

            var randomX = 2 * RANDOM.nextInt(widthWithoutWalls) + 1;
            var randomY = 2 * RANDOM.nextInt(heightWithoutWalls) + 1;

            cell = grid[randomY][randomX];
        } while(cell.getType() != Cell.Type.PATH);

        cell.setType(cellType);
        return cell;
    }

    private List<Direction> getValidDirections(int x, int y, Set<Cell> visited) {
        List<Direction> validDirections = new ArrayList<>(DIRECTION_COUNT);

        if (y >= 2 && !visited.contains(grid[y - 2][x]))
            validDirections.add(Direction.NORTH);

        if (x < (width - 2) && !visited.contains(grid[y][x + 2]))
            validDirections.add(Direction.EAST);

        if (y < (height - 2) && !visited.contains(grid[y + 2][x]))
            validDirections.add(Direction.SOUTH);

        if (x >= 2 && !visited.contains(grid[y][x - 2]))
            validDirections.add(Direction.WEST);

        return validDirections;
    }

    private Cell moveToNeighbour(Cell current, Direction direction) {
        var x = current.getX();
        var y = current.getY();

        var intermediate = grid[y + direction.offsetY][x + direction.offsetX];
        intermediate.setType(Cell.Type.PATH);

        var neighbour = grid[y + direction.offsetY * 2][x + direction.offsetX * 2];
        neighbour.setType(Cell.Type.PATH);

        return neighbour;
    }

    private void removeRandomWalls() {
        for (var y = 1; y < height - 1; y++) {
            for (var x = 1; x < width - 1; x++) {
                if (grid[y][x].getType() != Cell.Type.WALL) continue;
                if (RANDOM.nextDouble() < wallDensity) continue;

                grid[y][x].setType(Cell.Type.PATH);
            }
        }
    }

    private static Cell[][] createGrid(int width, int height) {
        var grid = new Cell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Cell(x, y, Cell.Type.WALL);
            }
        }

        return grid;
    }

    private enum Direction {
        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0);

        public final int offsetX;
        public final int offsetY;

        Direction(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }
}

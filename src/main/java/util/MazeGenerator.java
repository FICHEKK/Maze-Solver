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

        while (!stack.isEmpty()) {
            var cell = stack.peek();
            cell.setVisitedFlag();

            var validDirections = getValidDirections(cell.getX(), cell.getY());

            if (validDirections.isEmpty()) {
                stack.pop();
                continue;
            }

            var direction = validDirections.get(RANDOM.nextInt(validDirections.size()));
            var neighbour = moveToNeighbour(cell, direction);
            stack.push(neighbour);
        }

        var start = convertRandomPathCellTo(Cell.START);
        var finish = convertRandomPathCellTo(Cell.FINISH);

        removeRandomWalls();
        return new Maze(grid, width, height, start, finish);
    }

    private Cell convertRandomPathCellTo(int cellType) {
        Cell cell;

        do {
            int widthWithoutWalls = (width - 1) / 2;
            int heightWithoutWalls = (height - 1) / 2;

            var randomX = 2 * RANDOM.nextInt(widthWithoutWalls) + 1;
            var randomY = 2 * RANDOM.nextInt(heightWithoutWalls) + 1;

            cell = grid[randomY][randomX];
        } while(cell.getType() != Cell.PATH);

        cell.setType(cellType);
        return cell;
    }

    private List<Direction> getValidDirections(int x, int y) {
        List<Direction> validDirections = new ArrayList<>(DIRECTION_COUNT);

        if (y >= 2 && !grid[y - 2][x].isVisited())
            validDirections.add(Direction.NORTH);

        if (x < (width - 2) && !grid[y][x + 2].isVisited())
            validDirections.add(Direction.EAST);

        if (y < (height - 2) && !grid[y + 2][x].isVisited())
            validDirections.add(Direction.SOUTH);

        if (x >= 2 && !grid[y][x - 2].isVisited())
            validDirections.add(Direction.WEST);

        return validDirections;
    }

    private Cell moveToNeighbour(Cell current, Direction direction) {
        current.setNeighbourFlag(direction.sourceFlag);

        var intermediate = grid[current.getY() + direction.offsetY][current.getX() + direction.offsetX];
        intermediate.setType(Cell.PATH);

        var neighbour = grid[current.getY() + direction.offsetY * 2][current.getX() + direction.offsetX * 2];
        neighbour.setNeighbourFlag(direction.destinationFlag);
        neighbour.setType(Cell.PATH);

        return neighbour;
    }

    private void removeRandomWalls() {
        for (var y = 1; y < height - 1; y++) {
            for (var x = 1; x < width - 1; x++) {
                if (grid[y][x].getType() != Cell.WALL) continue;
                if (RANDOM.nextDouble() < wallDensity) continue;

                grid[y][x].setType(Cell.PATH);
            }
        }
    }

    private static Cell[][] createGrid(int width, int height) {
        var grid = new Cell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Cell();
                grid[y][x].setX(x);
                grid[y][x].setY(y);
            }
        }

        return grid;
    }

    private enum Direction {
        NORTH(0, -1, Cell.NORTH_NEIGHBOUR, Cell.SOUTH_NEIGHBOUR),
        EAST(1, 0, Cell.EAST_NEIGHBOUR, Cell.WEST_NEIGHBOUR),
        SOUTH(0, 1, Cell.SOUTH_NEIGHBOUR, Cell.NORTH_NEIGHBOUR),
        WEST(-1, 0, Cell.WEST_NEIGHBOUR, Cell.EAST_NEIGHBOUR);

        public final int offsetX;
        public final int offsetY;
        public final int sourceFlag;
        public final int destinationFlag;

        Direction(int offsetX, int offsetY, int sourceFlag, int destinationFlag) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.sourceFlag = sourceFlag;
            this.destinationFlag = destinationFlag;
        }
    }
}

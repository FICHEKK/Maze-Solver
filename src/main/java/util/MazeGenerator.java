package util;

import models.Maze;
import models.cells.NatureCell;
import models.cells.SearchCell;

import java.util.*;

public final class MazeGenerator {
    private static final Random RANDOM = new Random();
    private static final int DIRECTION_COUNT = Direction.values().length;

    private final int width;
    private final int height;
    private final double wallDensity;
    private final NatureCell[][] natureCells;
    private final SearchCell[][] searchCells;

    public MazeGenerator(int width, int height, double wallDensity) {
        if (wallDensity < 0 || wallDensity > 1)
            throw new IllegalArgumentException("Wall density must be in range [0, 1].");

        this.width = 2 * width + 1;
        this.height = 2 * height + 1;
        this.wallDensity = wallDensity;

        this.natureCells = new NatureCell[this.height][this.width];
        this.searchCells = new SearchCell[this.height][this.width];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                natureCells[y][x] = new NatureCell(x, y, NatureCell.Type.WALL);
                searchCells[y][x] = new SearchCell(x, y, SearchCell.Type.UNUSED);
            }
        }
    }

    public Maze generate() {
        var stack = new Stack<NatureCell>();
        stack.push(natureCells[1][1]);

        var visited = new HashSet<NatureCell>();

        while (!stack.isEmpty()) {
            var cell = stack.peek();
            visited.add(cell);
            cell.setType(NatureCell.Type.DIRT);

            var validDirections = getValidDirections(cell.getX(), cell.getY(), visited);

            if (validDirections.isEmpty()) {
                stack.pop();
                continue;
            }

            var direction = validDirections.get(RANDOM.nextInt(validDirections.size()));

            var intermediate = natureCells[cell.getY() + direction.offsetY][cell.getX() + direction.offsetX];
            intermediate.setType(NatureCell.Type.DIRT);

            var neighbour = natureCells[cell.getY() + direction.offsetY * 2][cell.getX() + direction.offsetX * 2];
            stack.push(neighbour);
        }

        var start = convertRandomSearchCellTo(SearchCell.Type.START);
        var finish = convertRandomSearchCellTo(SearchCell.Type.FINISH);

        removeRandomWalls();
        return new Maze(natureCells, searchCells, width, height, start, finish);
    }

    private SearchCell convertRandomSearchCellTo(SearchCell.Type type) {
        SearchCell searchCell;

        do {
            int widthWithoutWalls = (width - 1) / 2;
            int heightWithoutWalls = (height - 1) / 2;

            var randomX = 2 * RANDOM.nextInt(widthWithoutWalls) + 1;
            var randomY = 2 * RANDOM.nextInt(heightWithoutWalls) + 1;

            searchCell = searchCells[randomY][randomX];
        } while(searchCell.getType() != SearchCell.Type.UNUSED);

        searchCell.setType(type);
        return searchCell;
    }

    private List<Direction> getValidDirections(int x, int y, Set<NatureCell> visited) {
        List<Direction> validDirections = new ArrayList<>(DIRECTION_COUNT);

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

    private void removeRandomWalls() {
        for (var y = 1; y < height - 1; y++) {
            for (var x = 1; x < width - 1; x++) {
                if (natureCells[y][x].getType() != NatureCell.Type.WALL) continue;
                if (RANDOM.nextDouble() < wallDensity) continue;

                natureCells[y][x].setType(NatureCell.Type.DIRT);
            }
        }
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

package generators;

import java.util.ArrayList;

public class BinaryTree extends AbstractMazeGenerator {

    @Override
    protected void carveOutMazePath() {
        for (var row = 1; row < height; row += 2) {
            carveOutRow(row);
        }
    }

    private void carveOutRow(int row) {
        for (var column = 1; column < width; column += 2) {
            carveOutCell(row, column);
        }
    }

    private void carveOutCell(int row, int column) {
        final var cell = terrainCells[row][column];
        cell.setType(PATH_TYPE);

        final var validDirections = new ArrayList<Direction>();

        if (row > 1) validDirections.add(Direction.NORTH);
        if (column > 1) validDirections.add(Direction.WEST);

        if (validDirections.isEmpty()) return;

        var randomDirection = validDirections.get(RANDOM.nextInt(validDirections.size()));
        terrainCells[cell.getY() + randomDirection.offsetY][cell.getX() + randomDirection.offsetX].setType(PATH_TYPE);
        terrainCells[cell.getY() + randomDirection.offsetY * 2][cell.getX() + randomDirection.offsetX * 2].setType(PATH_TYPE);
    }

    @Override
    public String toString() {
        return "Binary Tree";
    }
}

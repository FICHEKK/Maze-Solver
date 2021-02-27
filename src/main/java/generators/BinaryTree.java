package generators;

public class BinaryTree extends AbstractMazeGenerator {

    @Override
    protected void carveOutMazePath() {
        carveOutFirstRow();
        carveOutFirstColumn();
        carveOutRestOfTheRows();
    }

    private void carveOutFirstRow() {
        for (var column = FIRST_COLUMN; column < width - 1; column++) {
            terrainCells[FIRST_ROW][column].setType(PATH_TYPE);
        }
    }

    private void carveOutFirstColumn() {
        for (var row = FIRST_ROW; row < height - 1; row++) {
            terrainCells[row][FIRST_COLUMN].setType(PATH_TYPE);
        }
    }

    private void carveOutRestOfTheRows() {
        for (var row = SECOND_ROW; row < height; row += GAP_SIZE) {
            carveOutRow(row);
        }
    }

    private void carveOutRow(int row) {
        for (var column = SECOND_COLUMN; column < width; column += GAP_SIZE) {
            carveOutCell(row, column);
        }
    }

    private void carveOutCell(int row, int column) {
        final var cell = terrainCells[row][column];
        cell.setType(PATH_TYPE);

        final var shouldCarveNorth = RANDOM.nextBoolean();

        if (shouldCarveNorth) {
            terrainCells[cell.getY() - 1][cell.getX()].setType(PATH_TYPE);
        }
        else {
            terrainCells[cell.getY()][cell.getX() - 1].setType(PATH_TYPE);
        }
    }

    @Override
    public String toString() {
        return "Binary Tree";
    }
}

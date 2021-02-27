package generators;

import models.cells.TerrainCell;

import java.util.ArrayList;
import java.util.List;

public class Sidewinder extends AbstractMazeGenerator {

    @Override
    protected void carveOutMazePath() {
        carveOutFirstRow();
        carveOutRestOfTheRows();
    }

    private void carveOutFirstRow() {
        for (var column = FIRST_COLUMN; column < width - 1; column++) {
            terrainCells[FIRST_ROW][column].setType(PATH_TYPE);
        }
    }

    private void carveOutRestOfTheRows() {
        final var possibleNorthPassages = new ArrayList<TerrainCell>();
        possibleNorthPassages.add(terrainCells[SECOND_ROW][FIRST_COLUMN]);

        for (var row = SECOND_ROW; row < height; row += GAP_SIZE) {
            carveOutRow(row, possibleNorthPassages);
        }
    }

    private void carveOutRow(int row, List<TerrainCell> possibleNorthPassages) {
        for (var column = FIRST_COLUMN; column < width; column += GAP_SIZE) {
            carveOutCell(row, column, possibleNorthPassages);
        }
    }

    private void carveOutCell(int row, int column, List<TerrainCell> possibleNorthPassages) {
        final var cell = terrainCells[row][column];
        cell.setType(PATH_TYPE);
        possibleNorthPassages.add(cell);

        final var canCarveEast = (column + GAP_SIZE) < width;
        final var shouldCarveEast = RANDOM.nextBoolean();

        if (canCarveEast && shouldCarveEast) {
            terrainCells[row][column + 1].setType(PATH_TYPE);
        }
        else {
            var passage = possibleNorthPassages.get(RANDOM.nextInt(possibleNorthPassages.size()));
            terrainCells[passage.getY() - 1][passage.getX()].setType(PATH_TYPE);
            possibleNorthPassages.clear();
        }
    }

    @Override
    public String toString() {
        return "Sidewinder";
    }
}

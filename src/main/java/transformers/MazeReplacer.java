package transformers;

import models.Maze;
import models.cells.TerrainCell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MazeReplacer implements MazeTransformer {
    private static final Random RANDOM = new Random();

    private final Map<TerrainCell.Type, List<Replacement>> typeToReplacements = new HashMap<>();

    @Override
    public void transform(Maze maze) {
        for (var y = 0; y < maze.getHeight(); y++) {
            for (var x = 0; x < maze.getWidth(); x++) {
                replace(maze.getTerrainCell(x, y));
            }
        }
    }

    private void replace(TerrainCell cell) {
        var replacements = typeToReplacements.get(cell.getType());
        if (replacements == null) return;

        var sum = 0.0;
        var random = RANDOM.nextDouble();

        for (var replacement : replacements) {
            sum += replacement.probability;

            if (random <= sum) {
                cell.setType(replacement.type);
                return;
            }
        }
    }

    public void addTypeReplacements(TerrainCell.Type replaceableType, List<Replacement> replacementTypes) {
        var probabilitySum = replacementTypes.stream().mapToDouble(r -> r.probability).sum();

        if (probabilitySum > 1.0)
            throw new IllegalArgumentException("Replacements' probability sum must not be greater than 1.0.");

        typeToReplacements.put(replaceableType, replacementTypes);
    }

    public static class Replacement {
        public final TerrainCell.Type type;
        public final double probability;

        public Replacement(TerrainCell.Type type, double probability) {
            this.type = type;
            this.probability = probability;
        }
    }
}

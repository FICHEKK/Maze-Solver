package generators;

import models.Maze;

public interface MazeGenerator {
    Maze generate(int widthWithoutWalls, int heightWithoutWalls);
}

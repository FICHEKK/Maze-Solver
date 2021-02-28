package edits;

import models.Maze;
import models.MazeHolder;

import java.util.Objects;

public class MazeGenerationEdit implements Edit {
    private final MazeHolder mazeHolder;
    private final Maze oldMaze;
    private final Maze newMaze;

    public MazeGenerationEdit(MazeHolder mazeHolder, Maze oldMaze, Maze newMaze) {
        this.mazeHolder = Objects.requireNonNull(mazeHolder);
        this.oldMaze = Objects.requireNonNull(oldMaze);
        this.newMaze = Objects.requireNonNull(newMaze);
    }

    @Override
    public void executeRedo() {
        mazeHolder.setMaze(newMaze);
    }

    @Override
    public void executeUndo() {
        mazeHolder.setMaze(oldMaze);
    }
}

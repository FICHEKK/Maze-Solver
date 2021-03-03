package edits;

import models.Maze;
import models.MazeHolder;

import java.util.Objects;

public class MazeGenerationEdit implements Edit {
    private final Maze oldMaze;
    private final Maze newMaze;

    public MazeGenerationEdit(Maze oldMaze, Maze newMaze) {
        this.oldMaze = Objects.requireNonNull(oldMaze);
        this.newMaze = Objects.requireNonNull(newMaze);
    }

    @Override
    public void executeRedo() {
        MazeHolder.getInstance().setMaze(newMaze);
    }

    @Override
    public void executeUndo() {
        MazeHolder.getInstance().setMaze(oldMaze);
    }
}

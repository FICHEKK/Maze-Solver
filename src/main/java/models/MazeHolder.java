package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MazeHolder {
    private Maze maze;
    private final List<MazeHolderListener> listeners = new ArrayList<>();

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        if (this.maze == maze) return;
        this.maze = Objects.requireNonNull(maze);
        listeners.forEach(listener -> listener.onMazeChanged(maze));
    }

    public void addListener(MazeHolderListener listener) {
        listeners.add(listener);
    }
}

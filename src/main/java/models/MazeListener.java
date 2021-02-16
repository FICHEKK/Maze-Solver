package models;

@FunctionalInterface
public interface MazeListener {
    void onCellChange(int x, int y);
}

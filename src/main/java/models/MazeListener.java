package models;

public interface MazeListener {
    void onSingleCellChanged(int x, int y);
    void onMultipleCellsChanged();
}

package edits;

public interface EditManagerListener {
    void onUndoStackEmpty();
    void onUndoStackNotEmpty();
    void onRedoStackEmpty();
    void onRedoStackNotEmpty();
}
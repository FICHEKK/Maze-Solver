package edits;

public interface EditManagerListener {

    default void onUndoStackEmpty() { }

    default void onUndoStackNotEmpty() { }

    default void onRedoStackEmpty() { }

    default void onRedoStackNotEmpty() { }
}
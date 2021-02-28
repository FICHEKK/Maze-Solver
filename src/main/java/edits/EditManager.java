package edits;

import java.util.*;

public class EditManager {
    private static final int UNDO_LIMIT = 100;
    private static final EditManager INSTANCE = new EditManager();

    private final Deque<Edit> undoStack = new ArrayDeque<>(UNDO_LIMIT);
    private final Deque<Edit> redoStack = new ArrayDeque<>(UNDO_LIMIT);

    private final List<EditManagerListener> listeners = new ArrayList<>();

    private EditManager() {
        // Singleton.
    }

    public static EditManager getInstance() {
        return INSTANCE;
    }

    public void undo() {
        final var edit = undoStack.pop();
        redoStack.push(edit);
        edit.executeUndo();

        if (undoStack.isEmpty()) notifyListenersUndoStackEmpty();
        if (redoStack.size() == 1) notifyListenersRedoStackNotEmpty();
    }

    public void redo() {
        final var edit = redoStack.pop();
        undoStack.push(edit);
        edit.executeRedo();

        if (redoStack.isEmpty()) notifyListenersRedoStackEmpty();
        if (undoStack.size() == 1) notifyListenersUndoStackNotEmpty();
    }

    public void push(Edit edit) {
        if (undoStack.size() == UNDO_LIMIT)
            undoStack.removeLast();

        redoStack.clear();
        undoStack.push(edit);

        notifyListenersRedoStackEmpty();
        if (undoStack.size() == 1) notifyListenersUndoStackNotEmpty();
    }

    public void addListener(EditManagerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EditManagerListener listener) {
        listeners.remove(listener);
    }

    private void notifyListenersUndoStackEmpty() {
        listeners.forEach(EditManagerListener::onUndoStackEmpty);
    }

    private void notifyListenersUndoStackNotEmpty() {
        listeners.forEach(EditManagerListener::onUndoStackNotEmpty);
    }

    private void notifyListenersRedoStackEmpty() {
        listeners.forEach(EditManagerListener::onRedoStackEmpty);
    }

    private void notifyListenersRedoStackNotEmpty() {
        listeners.forEach(EditManagerListener::onRedoStackNotEmpty);
    }
}
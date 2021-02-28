package ui.selection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ExactlyOneSelectionModel<T> implements SelectionModel<T> {
    private final Set<T> selectedItems = new LinkedHashSet<>();
    private final Set<T> unmodifiableSelectedItems = Collections.unmodifiableSet(selectedItems);

    public ExactlyOneSelectionModel(T item) {
        selectItem(item);
    }

    @Override
    public void selectItem(T item) {
        if (isSelected(item)) return;

        selectedItems.clear();
        selectedItems.add(Objects.requireNonNull(item));
    }

    @Override
    public Set<T> getSelectedItems() {
        return unmodifiableSelectedItems;
    }

    @Override
    public int getSelectedCount() {
        return 1;
    }

    @Override
    public boolean isSelected(T item) {
        return selectedItems.contains(item);
    }
}

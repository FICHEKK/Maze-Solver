package ui.selection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class MultipleSelectionModel<T> implements SelectionModel<T> {
    private final Set<T> selectedItems = new LinkedHashSet<>();
    private final Set<T> unmodifiableSelectedItems = Collections.unmodifiableSet(selectedItems);

    @Override
    public void selectItem(T item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
            return;
        }

        selectedItems.add(item);
    }

    @Override
    public Set<T> getSelectedItems() {
        return unmodifiableSelectedItems;
    }

    @Override
    public int getSelectedCount() {
        return selectedItems.size();
    }

    @Override
    public boolean isSelected(T item) {
        return selectedItems.contains(item);
    }
}

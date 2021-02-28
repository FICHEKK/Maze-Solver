package ui.selection;

import java.util.Set;

public interface SelectionModel<T> {

    void selectItem(T item);

    Set<T> getSelectedItems();

    int getSelectedCount();

    boolean isSelected(T item);
}

package ui.selection;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SelectionView<T> extends JComboBox<T> {

    public SelectionView(T[] items, SelectionModel<T> model) {
        super(items);

        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //noinspection unchecked
                model.selectItem((T) getSelectedItem());
            }
        });
    }
}

package ui.buttons;

import edits.EditManager;
import edits.EditManagerListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UndoButton extends JButton {
    private static final String TITLE = "Undo";

    public UndoButton() {
        setClickAction();
        subscribeToEditManager();
        setGUI();
    }

    private void setGUI() {
        setText(TITLE);
        setEnabled(false);
    }

    private void setClickAction() {
        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditManager.getInstance().undo();
            }
        });
    }

    private void subscribeToEditManager() {
        EditManager.getInstance().addListener(new EditManagerListener() {
            @Override
            public void onUndoStackEmpty() {
                setEnabled(false);
            }

            @Override
            public void onUndoStackNotEmpty() {
                setEnabled(true);
            }
        });
    }
}

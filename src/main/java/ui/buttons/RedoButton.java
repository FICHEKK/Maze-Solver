package ui.buttons;

import edits.EditManager;
import edits.EditManagerListener;
import util.ImageUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RedoButton extends JButton {
    private static final String TITLE = "Redo";
    private static final String ICON_PATH = "/icons/redo.png";

    public RedoButton() {
        setClickAction();
        subscribeToEditManager();
        setGUI();
    }

    private void setGUI() {
        setText(TITLE);
        setEnabled(false);
        setIcon(ImageUtils.loadIcon(ICON_PATH));
    }

    private void setClickAction() {
        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditManager.getInstance().redo();
            }
        });
    }

    private void subscribeToEditManager() {
        EditManager.getInstance().addListener(new EditManagerListener() {
            @Override
            public void onRedoStackEmpty() {
                setEnabled(false);
            }

            @Override
            public void onRedoStackNotEmpty() {
                setEnabled(true);
            }
        });
    }
}

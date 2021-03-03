package ui.buttons;

import models.MazeHolder;
import util.MazeConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class SaveMazeButton extends ConvertMazeButton {
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(0, 146, 255, 255);
    private static final String SAVE_ACTION_TEXT = "Save";

    public SaveMazeButton() {
        setClickAction();
        setGUI();
    }

    private void setClickAction() {
        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var fileChooser = createFileChooser(SAVE_ACTION_TEXT);

                int decision = fileChooser.showOpenDialog(null);
                if (decision != JFileChooser.APPROVE_OPTION) return;

                var destination = new File(fileChooser.getSelectedFile() + "." + SAVE_FILE_EXTENSION);

                try {
                    saveMaze(destination);
                    showMessage("Success", "Maze was successfully saved to " + destination.getAbsolutePath() + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    showMessage("Error", "Maze could not be saved to " + destination.getAbsolutePath() + ".", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void saveMaze(File destination) throws IOException {
        MazeConverter.serialize(MazeHolder.getInstance().getMaze(), destination);
    }

    private void setGUI() {
        setText(SAVE_ACTION_TEXT);
        setForeground(TEXT_COLOR);
        setBackground(BACKGROUND_COLOR);
    }
}

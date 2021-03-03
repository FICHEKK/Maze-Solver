package ui.buttons;

import edits.EditManager;
import edits.MazeGenerationEdit;
import models.Maze;
import models.MazeHolder;
import util.MazeConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class LoadMazeButton extends ConvertMazeButton {
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(146, 146, 146, 255);

    private static final String LOAD_ACTION_TEXT = "Load";

    public LoadMazeButton() {
        setClickAction();
        setGUI();
    }

    private void setClickAction() {
        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var fileChooser = createFileChooser(LOAD_ACTION_TEXT);

                int decision = fileChooser.showOpenDialog(null);
                if (decision != JFileChooser.APPROVE_OPTION) return;

                var source = fileChooser.getSelectedFile();

                try {
                    loadMaze(source);
                    showMessage("Success", "Maze was successfully loaded from " + source.getAbsolutePath() + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    showMessage("Error", "Maze could not be loaded from " + source.getAbsolutePath() + ".", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadMaze(File source) throws IOException {
        final var oldMaze = MazeHolder.getInstance().getMaze();
        final var newMaze = MazeConverter.deserialize(source);
        MazeHolder.getInstance().setMaze(newMaze);
        publishEdit(oldMaze, newMaze);
    }

    private void publishEdit(Maze oldMaze, Maze newMaze) {
        if (oldMaze == null) return;
        EditManager.getInstance().push(new MazeGenerationEdit(oldMaze, newMaze));
    }

    private void setGUI() {
        setText(LOAD_ACTION_TEXT);
        setForeground(TEXT_COLOR);
        setBackground(BACKGROUND_COLOR);
    }
}

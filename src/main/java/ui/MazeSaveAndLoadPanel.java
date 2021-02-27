package ui;

import models.MazeHolder;
import util.MazeConverter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class MazeSaveAndLoadPanel extends JPanel {
    private static final Color SAVE_BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color SAVE_BUTTON_BACKGROUND_COLOR = new Color(0, 146, 255, 255);
    private static final Color LOAD_BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color LOAD_BUTTON_BACKGROUND_COLOR = new Color(146, 146, 146, 255);

    private static final String SAVE_FILE_FOLDER = ".";
    private static final String SAVE_FILE_DESCRIPTION = "Maze-Solver Save File";
    private static final String SAVE_FILE_EXTENSION = "maze";

    private static final String SAVE_ACTION_TEXT = "Save";
    private static final String LOAD_ACTION_TEXT = "Load";

    private final MazeHolder mazeHolder;

    public MazeSaveAndLoadPanel(MazeHolder mazeHolder) {
        this.mazeHolder = mazeHolder;

        setLayout(new GridBagLayout());
        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;

        addSaveMazeButton(constraints);
        addLoadMazeButton(constraints);
    }

    private void addSaveMazeButton(GridBagConstraints constraints) {
        var saveMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var fileChooser = createFileChooser(SAVE_ACTION_TEXT);

                int decision = fileChooser.showOpenDialog(null);
                if (decision != JFileChooser.APPROVE_OPTION) return;

                var destination = new File(fileChooser.getSelectedFile() + "." + SAVE_FILE_EXTENSION);

                try {
                    MazeConverter.serialize(mazeHolder.getMaze(), destination);
                    showMessage("Success", "Maze was successfully saved to " + destination.getAbsolutePath() + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    showMessage("Error", "Maze could not be saved to " + destination.getAbsolutePath() + ".", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveMazeButton.setText(SAVE_ACTION_TEXT);
        saveMazeButton.setForeground(SAVE_BUTTON_TEXT_COLOR);
        saveMazeButton.setBackground(SAVE_BUTTON_BACKGROUND_COLOR);

        add(saveMazeButton, constraints);
    }

    private void addLoadMazeButton(GridBagConstraints constraints) {
        var loadMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var fileChooser = createFileChooser(LOAD_ACTION_TEXT);

                int decision = fileChooser.showOpenDialog(null);
                if (decision != JFileChooser.APPROVE_OPTION) return;

                var source = fileChooser.getSelectedFile();

                try {
                    mazeHolder.setMaze(MazeConverter.deserialize(source));
                    showMessage("Success", "Maze was successfully loaded from " + source.getAbsolutePath() + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    showMessage("Error", "Maze could not be loaded from " + source.getAbsolutePath() + ".", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadMazeButton.setText(LOAD_ACTION_TEXT);
        loadMazeButton.setForeground(LOAD_BUTTON_TEXT_COLOR);
        loadMazeButton.setBackground(LOAD_BUTTON_BACKGROUND_COLOR);

        add(loadMazeButton, constraints);
    }

    private JFileChooser createFileChooser(String actionName) {
        var fileChooser = new JFileChooser(new File(SAVE_FILE_FOLDER));
        fileChooser.setDialogTitle(actionName);
        fileChooser.setApproveButtonText(actionName);

        var filter = new FileNameExtensionFilter(SAVE_FILE_DESCRIPTION, SAVE_FILE_EXTENSION);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);

        return fileChooser;
    }

    private void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }
}

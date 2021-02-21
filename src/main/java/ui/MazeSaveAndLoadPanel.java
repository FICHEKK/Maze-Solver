package ui;

import util.MazeConverter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class MazeSaveAndLoadPanel extends JPanel {
    private static final String SAVE_FILE_FOLDER = ".";
    private static final String SAVE_FILE_DESCRIPTION = "Maze-Solver Save File";
    private static final String SAVE_FILE_EXTENSION = "maze";

    private static final String SAVE_ACTION_TEXT = "Save";
    private static final String LOAD_ACTION_TEXT = "Load";

    private final MazeView mazeView;

    public MazeSaveAndLoadPanel(MazeView mazeView) {
        this.mazeView = mazeView;

        setLayout(new GridLayout(1, 0, 10, 0));
        addSaveMazeButton();
        addLoadMazeButton();
    }

    private void addSaveMazeButton() {
        var saveMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var fileChooser = createFileChooser(SAVE_ACTION_TEXT);

                int decision = fileChooser.showOpenDialog(null);
                if (decision != JFileChooser.APPROVE_OPTION) return;

                var destination = new File(fileChooser.getSelectedFile() + "." + SAVE_FILE_EXTENSION);

                try {
                    MazeConverter.serialize(mazeView.getMaze(), destination);
                    showMessage("Success", "Maze was successfully saved to " + destination.getAbsolutePath() + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    showMessage("Error", "Maze could not be saved to " + destination.getAbsolutePath() + ".", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        saveMazeButton.setText(SAVE_ACTION_TEXT);
        add(saveMazeButton);
    }

    private void addLoadMazeButton() {
        var loadMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var fileChooser = createFileChooser(LOAD_ACTION_TEXT);

                int decision = fileChooser.showOpenDialog(null);
                if (decision != JFileChooser.APPROVE_OPTION) return;

                var source = fileChooser.getSelectedFile();

                try {
                    mazeView.setMaze(MazeConverter.deserialize(source));
                    showMessage("Success", "Maze was successfully loaded from " + source.getAbsolutePath() + ".", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException exception) {
                    showMessage("Error", "Maze could not be loaded from " + source.getAbsolutePath() + ".", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loadMazeButton.setText(LOAD_ACTION_TEXT);
        add(loadMazeButton);
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

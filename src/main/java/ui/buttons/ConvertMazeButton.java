package ui.buttons;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public abstract class ConvertMazeButton extends JButton {
    protected static final String SAVE_FILE_FOLDER = ".";
    protected static final String SAVE_FILE_DESCRIPTION = "Maze-Solver Save File";
    protected static final String SAVE_FILE_EXTENSION = "maze";

    protected JFileChooser createFileChooser(String actionName) {
        var fileChooser = new JFileChooser(new File(SAVE_FILE_FOLDER));
        fileChooser.setDialogTitle(actionName);
        fileChooser.setApproveButtonText(actionName);

        var filter = new FileNameExtensionFilter(SAVE_FILE_DESCRIPTION, SAVE_FILE_EXTENSION);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);

        return fileChooser;
    }

    protected void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }
}

package ui;

import models.MazeHolder;
import ui.buttons.LoadMazeButton;
import ui.buttons.SaveMazeButton;

import javax.swing.*;
import java.awt.*;

public class MazeSaveAndLoadPanel extends JPanel {

    public MazeSaveAndLoadPanel(MazeHolder mazeHolder) {
        setLayout(new GridBagLayout());

        final var constraints = initializeConstraints();
        add(new SaveMazeButton(mazeHolder), constraints);
        add(new LoadMazeButton(mazeHolder), constraints);
    }

    private GridBagConstraints initializeConstraints() {
        final var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;
        return constraints;
    }
}

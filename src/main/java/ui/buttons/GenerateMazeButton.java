package ui.buttons;

import edits.EditManager;
import edits.MazeGenerationEdit;
import generators.MazeGenerator;
import models.Maze;
import models.MazeHolder;
import models.cells.TerrainCell;
import transformers.MazeReplacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GenerateMazeButton extends JButton {
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(5, 91, 0, 255);

    private static final int MIN_DIMENSION = 4;
    private static final int MAX_DIMENSION = 1000;

    private final MazeHolder mazeHolder;
    private final JComboBox<MazeGenerator> mazeGeneratorPicker;
    private final JTextField widthTextField;
    private final JTextField heightTextField;
    private final JSlider wallDensitySlider;

    public GenerateMazeButton(
            MazeHolder mazeHolder,
            JComboBox<MazeGenerator> mazeGeneratorPicker,
            JTextField widthTextField,
            JTextField heightTextField,
            JSlider wallDensitySlider
    ) {
        this.mazeHolder = mazeHolder;
        this.mazeGeneratorPicker = mazeGeneratorPicker;
        this.widthTextField = widthTextField;
        this.heightTextField = heightTextField;
        this.wallDensitySlider = wallDensitySlider;

        setClickAction();
        setGUI();
        generateMaze();
    }

    private void setClickAction() {
        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze();
            }
        });
    }

    private void setGUI() {
        setText("Generate");
        setForeground(TEXT_COLOR);
        setBackground(BACKGROUND_COLOR);
    }

    private void generateMaze() {
        try {
            int width = tryParseInt(widthTextField.getText());
            int height = tryParseInt(heightTextField.getText());

            if (width < MIN_DIMENSION || width > MAX_DIMENSION)
                throw new NumberFormatException("Width must be in range [" + MIN_DIMENSION + ", " + MAX_DIMENSION + "].");

            if (height < MIN_DIMENSION || height > MAX_DIMENSION)
                throw new NumberFormatException("Height must be in range [" + MIN_DIMENSION + ", " + MAX_DIMENSION + "].");

            var maze = getSelectedMazeGenerator().generate(width, height);

            var replacer = new MazeReplacer();
            var wallDensity = (double) wallDensitySlider.getValue() / wallDensitySlider.getMaximum();
            replacer.addTypeReplacements(TerrainCell.Type.BUSH, List.of(new MazeReplacer.Replacement(TerrainCell.Type.DIRT, 1 - wallDensity)));
            replacer.transform(maze);

            publishEdit(mazeHolder.getMaze(), maze);
            mazeHolder.setMaze(maze);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    private void publishEdit(Maze oldMaze, Maze newMaze) {
        if (oldMaze == null) return;
        EditManager.getInstance().push(new MazeGenerationEdit(mazeHolder, oldMaze, newMaze));
    }

    private int tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("'" + string + "' is not a valid integer value.");
        }
    }

    private MazeGenerator getSelectedMazeGenerator() {
        return (MazeGenerator) mazeGeneratorPicker.getSelectedItem();
    }
}

package ui;

import generators.BinaryTree;
import generators.MazeGenerator;
import generators.RecursiveBacktracker;
import models.MazeHolder;
import models.cells.TerrainCell;
import transformers.MazeReplacer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MazeGenerationPanel extends JPanel {
    private static final Color GENERATE_BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color GENERATE_BUTTON_BACKGROUND_COLOR = new Color(5, 91, 0, 255);
    private static final int PADDING = 10;

    private static final int DEFAULT_MAZE_WIDTH = 50;
    private static final int DEFAULT_MAZE_HEIGHT = 50;
    private static final int MIN_DIMENSION = 4;
    private static final int MAX_DIMENSION = 1000;

    private static final int MIN_WALL_DENSITY = 0;
    private static final int DEFAULT_WALL_DENSITY = 100;
    private static final int MAX_WALL_DENSITY = 100;

    private final MazeHolder mazeHolder;

    private final JComboBox<MazeGenerator> mazeGeneratorPicker = new JComboBox<>();
    private final JTextField widthTextField = new JTextField(String.valueOf(DEFAULT_MAZE_WIDTH));
    private final JTextField heightTextField = new JTextField(String.valueOf(DEFAULT_MAZE_HEIGHT));
    private final JSlider wallDensitySlider = new JSlider(MIN_WALL_DENSITY, MAX_WALL_DENSITY, DEFAULT_WALL_DENSITY);

    public MazeGenerationPanel(MazeHolder mazeHolder) {
        this.mazeHolder = mazeHolder;

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;

        addMazeGeneratorPicker(constraints);
        addWidthField(constraints);
        addHeightField(constraints);
        addWallDensityField(constraints);
        addGenerateMazeButton(constraints);

        generateMaze();
    }

    private void addMazeGeneratorPicker(GridBagConstraints constraints) {
        mazeGeneratorPicker.addItem(new RecursiveBacktracker());
        mazeGeneratorPicker.addItem(new BinaryTree());
        addComponents(constraints, new JLabel("Generator:", JLabel.CENTER), mazeGeneratorPicker);
    }

    private void addWidthField(GridBagConstraints constraints) {
        widthTextField.setHorizontalAlignment(SwingConstants.CENTER);
        addComponents(constraints, new JLabel("Width:", JLabel.CENTER), widthTextField);
    }

    private void addHeightField(GridBagConstraints constraints) {
        heightTextField.setHorizontalAlignment(SwingConstants.CENTER);
        addComponents(constraints, new JLabel("Height:", JLabel.CENTER), heightTextField);
    }

    private void addWallDensityField(GridBagConstraints constraints) {
        var wallDensityLabel = new JLabel("Wall density (" + DEFAULT_WALL_DENSITY + "%):", JLabel.CENTER);
        wallDensityLabel.setPreferredSize(new Dimension(120, 20));

        wallDensitySlider.setPreferredSize(new Dimension(80, 20));
        wallDensitySlider.addChangeListener(e -> wallDensityLabel.setText("Wall density (" + wallDensitySlider.getValue() + "%):"));

        addComponents(constraints, wallDensityLabel, wallDensitySlider);
    }

    private void addGenerateMazeButton(GridBagConstraints constraints) {
        var generateMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze();
            }
        });
        generateMazeButton.setText("Generate");
        generateMazeButton.setForeground(GENERATE_BUTTON_TEXT_COLOR);
        generateMazeButton.setBackground(GENERATE_BUTTON_BACKGROUND_COLOR);

        constraints.gridwidth = 2;
        addComponents(constraints, generateMazeButton);
    }

    private void addComponents(GridBagConstraints constraints, JComponent... components) {
        for (var component : components) {
            add(component, constraints);
            constraints.gridx++;
        }

        constraints.gridx = 0;
        constraints.gridy++;
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
            var wallDensity = (double) wallDensitySlider.getValue() / MAX_WALL_DENSITY;
            replacer.addTypeReplacements(TerrainCell.Type.BUSH, List.of(new MazeReplacer.Replacement(TerrainCell.Type.DIRT, 1 - wallDensity)));
            replacer.transform(maze);

            mazeHolder.setMaze(maze);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
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

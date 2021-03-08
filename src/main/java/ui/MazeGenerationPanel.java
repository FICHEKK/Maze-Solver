package ui;

import generators.*;
import ui.buttons.GenerateMazeButton;
import util.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MazeGenerationPanel extends JPanel {
    private static final int PADDING = 10;

    private static final int DEFAULT_MAZE_WIDTH = 50;
    private static final int DEFAULT_MAZE_HEIGHT = 50;

    private static final int MIN_WALL_DENSITY = 0;
    private static final int DEFAULT_WALL_DENSITY = 100;
    private static final int MAX_WALL_DENSITY = 100;

    private static final int GENERATE_BUTTON_GRID_WIDTH = 2;
    private static final String GENERATE_BUTTON_ICON_PATH = "/icons/generate.png";

    private static final Dimension WALL_DENSITY_SLIDER_DIMENSION = new Dimension(80, 20);
    private static final Dimension WALL_DENSITY_LABEL_DIMENSION = new Dimension(120, 20);

    private final JComboBox<MazeGenerator> mazeGeneratorPicker = new JComboBox<>();
    private final JTextField widthTextField = new JTextField(String.valueOf(DEFAULT_MAZE_WIDTH));
    private final JTextField heightTextField = new JTextField(String.valueOf(DEFAULT_MAZE_HEIGHT));
    private final JSlider wallDensitySlider = new JSlider(MIN_WALL_DENSITY, MAX_WALL_DENSITY, DEFAULT_WALL_DENSITY);

    public MazeGenerationPanel() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        final var constraints = initializeConstraints();
        addMazeGeneratorPicker(constraints);
        addWidthField(constraints);
        addHeightField(constraints);
        addWallDensityField(constraints);
        addGenerateMazeButton(constraints);
    }

    private GridBagConstraints initializeConstraints() {
        final var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        return constraints;
    }

    private void addMazeGeneratorPicker(GridBagConstraints constraints) {
        mazeGeneratorPicker.addItem(new RecursiveBacktracker());
        mazeGeneratorPicker.addItem(new BinaryTree());
        mazeGeneratorPicker.addItem(new Sidewinder());
        mazeGeneratorPicker.addItem(new Prim());

        ((JLabel) mazeGeneratorPicker.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
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
        wallDensityLabel.setPreferredSize(WALL_DENSITY_LABEL_DIMENSION);

        wallDensitySlider.setPreferredSize(WALL_DENSITY_SLIDER_DIMENSION);
        wallDensitySlider.addChangeListener(e -> wallDensityLabel.setText("Wall density (" + wallDensitySlider.getValue() + "%):"));

        addComponents(constraints, wallDensityLabel, wallDensitySlider);
    }

    private void addGenerateMazeButton(GridBagConstraints constraints) {
        final var generateMazeButton = new GenerateMazeButton(mazeGeneratorPicker, widthTextField, heightTextField, wallDensitySlider);
        generateMazeButton.setIcon(ImageUtils.loadIcon(GENERATE_BUTTON_ICON_PATH));
        constraints.gridwidth = GENERATE_BUTTON_GRID_WIDTH;
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
}

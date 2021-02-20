package ui;

import util.MazeGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MazeGenerationPanel extends JPanel {
    private static final int PADDING = 10;

    private static final int DEFAULT_MAZE_WIDTH = 60;
    private static final int DEFAULT_MAZE_HEIGHT = 30;
    private static final int MIN_DIMENSION = 4;
    private static final int MAX_DIMENSION = 1000;

    private static final int MIN_WALL_DENSITY = 0;
    private static final int DEFAULT_WALL_DENSITY = 100;
    private static final int MAX_WALL_DENSITY = 100;

    private final MazeView mazeView;

    private final JTextField widthTextField = new JTextField(String.valueOf(DEFAULT_MAZE_WIDTH));
    private final JTextField heightTextField = new JTextField(String.valueOf(DEFAULT_MAZE_HEIGHT));
    private final JSlider wallDensitySlider = new JSlider(MIN_WALL_DENSITY, MAX_WALL_DENSITY, DEFAULT_WALL_DENSITY);

    public MazeGenerationPanel(MazeView mazeView) {
        this.mazeView = mazeView;

        setLayout(new GridLayout(1, 0, PADDING, 0));
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        addWidthField();
        addHeightField();
        addWallDensityField();
        addGenerateMazeButton();

        generateMaze();
    }

    private void addWidthField() {
        add(new JLabel("Width:", JLabel.CENTER));
        add(widthTextField);
        widthTextField.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void addHeightField() {
        add(new JLabel("Height:", JLabel.CENTER));
        add(heightTextField);
        heightTextField.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void addWallDensityField() {
        var wallDensityLabel = new JLabel("Wall density (" + DEFAULT_WALL_DENSITY + "%):", JLabel.CENTER);
        add(wallDensityLabel);
        add(wallDensitySlider);
        wallDensitySlider.addChangeListener(e -> wallDensityLabel.setText("Wall density (" + wallDensitySlider.getValue() + "%):"));
    }

    private void addGenerateMazeButton() {
        var generateMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze();
            }
        });
        generateMazeButton.setText("Generate");
        add(generateMazeButton);
    }

    private void generateMaze() {
        try {
            int width = tryParseInt(widthTextField.getText());
            int height = tryParseInt(heightTextField.getText());

            if (width < MIN_DIMENSION || width > MAX_DIMENSION)
                throw new NumberFormatException("Width must be in range [" + MIN_DIMENSION + ", " + MAX_DIMENSION + "].");

            if (height < MIN_DIMENSION || height > MAX_DIMENSION)
                throw new NumberFormatException("Height must be in range [" + MIN_DIMENSION + ", " + MAX_DIMENSION + "].");

            var wallDensity = (double) wallDensitySlider.getValue() / MAX_WALL_DENSITY;
            var mazeGenerator = new MazeGenerator(width, height, wallDensity);
            mazeView.setMaze(mazeGenerator.generate());
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
}

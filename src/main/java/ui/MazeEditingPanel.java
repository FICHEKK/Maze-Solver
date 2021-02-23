package ui;

import models.Maze;
import models.cells.NatureCell;
import models.cells.SearchCell;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class MazeEditingPanel extends JPanel {
    private static final Random RANDOM = new Random();
    private static final int PADDING = 10;

    private static final int MIN_RADIUS = 0;
    private static final int DEFAULT_RADIUS = 0;
    private static final int MAX_RADIUS = 100;

    private static final int MIN_DENSITY = 0;
    private static final int DEFAULT_DENSITY = 100;
    private static final int MAX_DENSITY = 100;

    private static final Dimension SLIDER_DIMENSION = new Dimension(80, 20);
    private static final Dimension DENSITY_LABEL_DIMENSION = new Dimension(100, 20);

    private final MazeView mazeView;
    private final JComboBox<NatureCell.Type> paintBrushPicker = new JComboBox<>(NatureCell.Type.values());
    private final JSlider paintBrushRadiusSlider = new JSlider(MIN_RADIUS, MAX_RADIUS, DEFAULT_RADIUS);
    private final JSlider paintBrushDensitySlider = new JSlider(MIN_DENSITY, MAX_DENSITY, DEFAULT_DENSITY);

    public MazeEditingPanel(MazeView mazeView) {
        this.mazeView = mazeView;

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;

        addOutsideField(constraints);
        addBrushField(constraints);
        addRadiusField(constraints);
        addDensityField(constraints);
        addMazeViewMouseListeners();
    }

    private void addOutsideField(GridBagConstraints constraints) {
        var outsidePicker = new JComboBox<>(NatureCell.Type.values());
        ((JLabel) outsidePicker.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        outsidePicker.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mazeView.setOutsideCellType((NatureCell.Type) outsidePicker.getSelectedItem());
            }
        });

        addComponents(constraints, new JLabel("Outside:", JLabel.CENTER), outsidePicker);
    }

    private void addBrushField(GridBagConstraints constraints) {
        ((JLabel) paintBrushPicker.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        addComponents(constraints, new JLabel("Brush:", JLabel.CENTER), paintBrushPicker);
    }

    private void addRadiusField(GridBagConstraints constraints) {
        var radiusLabel = new JLabel("Radius (" + DEFAULT_RADIUS + "):", JLabel.CENTER);

        paintBrushRadiusSlider.setPreferredSize(SLIDER_DIMENSION);
        paintBrushRadiusSlider.addChangeListener(e -> radiusLabel.setText("Radius (" + paintBrushRadiusSlider.getValue() + "):"));

        addComponents(constraints, radiusLabel, paintBrushRadiusSlider);
    }

    private void addDensityField(GridBagConstraints constraints) {
        var densityLabel = new JLabel("Density (" + DEFAULT_DENSITY + "%):", JLabel.CENTER);
        densityLabel.setPreferredSize(DENSITY_LABEL_DIMENSION);

        paintBrushDensitySlider.setPreferredSize(SLIDER_DIMENSION);
        paintBrushDensitySlider.addChangeListener(e -> densityLabel.setText("Density (" + paintBrushDensitySlider.getValue() + "%):"));

        addComponents(constraints, densityLabel, paintBrushDensitySlider);
    }

    private void addComponents(GridBagConstraints constraints, JComponent... components) {
        for (var component : components) {
            add(component, constraints);
            constraints.gridx++;
        }

        constraints.gridx = 0;
        constraints.gridy++;
    }

    private void addMazeViewMouseListeners() {
        mazeView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseEvent(e);
            }
        });

        mazeView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseEvent(e);
            }
        });
    }

    private void handleMouseEvent(MouseEvent event) {
        final var maze = mazeView.getMaze();
        final var cellDimension = Math.min(mazeView.getWidth() / maze.getWidth(), mazeView.getHeight() / maze.getHeight());
        if (cellDimension == 0) return;

        final var remainderWidth = (mazeView.getWidth() - cellDimension * maze.getWidth()) / 2;
        final var remainderHeight = (mazeView.getHeight() - cellDimension * maze.getHeight()) / 2;

        final var centerX = (event.getX() - remainderWidth) / cellDimension;
        final var centerY = (event.getY() - remainderHeight) / cellDimension;

        if (SwingUtilities.isLeftMouseButton(event)) {
            paintMazeUsingCurrentBrushSettings(maze, centerX, centerY);
        }
        else if (SwingUtilities.isMiddleMouseButton(event)) {
            maze.setSearchCell(centerX, centerY, SearchCell.Type.START);
        }
        else if (SwingUtilities.isRightMouseButton(event)) {
            maze.setSearchCell(centerX, centerY, SearchCell.Type.FINISH);
        }
    }

    private void paintMazeUsingCurrentBrushSettings(Maze maze, int centerX, int centerY) {
        final var paintBrushRadius = paintBrushRadiusSlider.getValue();
        final var paintBrushDensity = paintBrushDensitySlider.getValue() / 100.0;

        for (var y = centerY - paintBrushRadius; y <= centerY + paintBrushRadius; y++) {
            for (var x = centerX - paintBrushRadius; x <= centerX + paintBrushRadius; x++) {
                if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) continue;
                if (RANDOM.nextDouble() > paintBrushDensity) continue;
                if (Math.hypot(centerX - x, centerY - y) > paintBrushRadius) continue;

                maze.setNatureCell(x, y, (NatureCell.Type) paintBrushPicker.getSelectedItem());
            }
        }
    }
}

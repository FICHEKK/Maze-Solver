package ui;

import models.Maze;
import models.MazeHolder;
import models.cells.TerrainCell;
import ui.selection.*;

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
    private final MazeHolder mazeHolder;

    private final SelectionModel<TerrainCell.Type> paintBrushModel = new ExactlyOneSelectionModel<>(TerrainCell.Type.first());
    private final SelectionModel<TerrainCell.Type> indestructibleCellsModel = new MultipleSelectionModel<>();

    private final JSlider paintBrushRadiusSlider = new JSlider(MIN_RADIUS, MAX_RADIUS, DEFAULT_RADIUS);
    private final JSlider paintBrushDensitySlider = new JSlider(MIN_DENSITY, MAX_DENSITY, DEFAULT_DENSITY);

    public MazeEditingPanel(MazeView mazeView, MazeHolder mazeHolder) {
        this.mazeView = mazeView;
        this.mazeHolder = mazeHolder;

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

        addPaintBrushPicker(constraints);
        addIndestructibleCellsSelector(constraints);
        addRadiusField(constraints);
        addDensityField(constraints);
        addOutsideBrushPicker(constraints);
        addMazeViewMouseListeners();
    }

    private void addPaintBrushPicker(GridBagConstraints constraints) {
        final var paintBrushPicker = new SelectionView<>(TerrainCell.Type.values(), paintBrushModel);
        paintBrushPicker.setEditable(true);
        paintBrushPicker.setEditor(new TerrainCellSelectionEditor(paintBrushModel));
        paintBrushPicker.setRenderer(new TerrainCellSelectionRenderer(true, paintBrushModel));
        addComponents(constraints, new JLabel("Brush:", JLabel.CENTER), paintBrushPicker);
    }

    private void addIndestructibleCellsSelector(GridBagConstraints constraints) {
        final var indestructibleCellsSelector = new SelectionView<>(TerrainCell.Type.values(), indestructibleCellsModel);
        indestructibleCellsSelector.setEditable(true);
        indestructibleCellsSelector.setEditor(new TerrainCellSelectionEditor(indestructibleCellsModel));
        indestructibleCellsSelector.setRenderer(new TerrainCellSelectionRenderer(false, indestructibleCellsModel));
        addComponents(constraints, new JLabel("Indestructible:", JLabel.CENTER), indestructibleCellsSelector);
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

    private void addOutsideBrushPicker(GridBagConstraints constraints) {
        final var outsideBrushModel = new ExactlyOneSelectionModel<>(TerrainCell.Type.first());
        final var outsideBrushPicker = new SelectionView<>(TerrainCell.Type.values(), outsideBrushModel);
        outsideBrushPicker.setEditable(true);
        outsideBrushPicker.setEditor(new TerrainCellSelectionEditor(outsideBrushModel));
        outsideBrushPicker.setRenderer(new TerrainCellSelectionRenderer(false, outsideBrushModel));

        outsideBrushPicker.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mazeView.setOutsideCellType((TerrainCell.Type) outsideBrushPicker.getSelectedItem());
            }
        });

        addComponents(constraints, new JLabel("Outside:", JLabel.CENTER), outsideBrushPicker);
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
        final var maze = mazeHolder.getMaze();
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
            maze.setStart(centerX, centerY);
        }
        else if (SwingUtilities.isRightMouseButton(event)) {
            maze.setFinish(centerX, centerY);
        }
    }

    private void paintMazeUsingCurrentBrushSettings(Maze maze, int centerX, int centerY) {
        final var paintBrushRadius = paintBrushRadiusSlider.getValue();
        final var paintBrushDensity = paintBrushDensitySlider.getValue() / 100.0;
        final var paintBrushType = paintBrushModel.getSelectedItems().iterator().next();
        final var indestructibleCellTypes = indestructibleCellsModel.getSelectedItems();

        for (var y = centerY - paintBrushRadius; y <= centerY + paintBrushRadius; y++) {
            for (var x = centerX - paintBrushRadius; x <= centerX + paintBrushRadius; x++) {
                if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) continue;
                if (indestructibleCellTypes.contains(maze.getTerrainCell(x, y).getType())) continue;
                if (RANDOM.nextDouble() > paintBrushDensity) continue;
                if (Math.hypot(centerX - x, centerY - y) > paintBrushRadius) continue;

                maze.setTerrainCell(x, y, paintBrushType);
            }
        }
    }
}

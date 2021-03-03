package ui;

import models.MazeHolder;
import models.cells.TerrainCell;
import tools.*;
import ui.buttons.RedoButton;
import ui.buttons.UndoButton;
import ui.selection.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MazeEditingPanel extends JPanel {
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

    private final JComboBox<Tool> toolbox = new JComboBox<>();
    private final SelectionModel<TerrainCell.Type> brushModel = new ExactlyOneSelectionModel<>(TerrainCell.Type.first());
    private final SelectionModel<TerrainCell.Type> indestructibleCellsModel = new MultipleSelectionModel<>();

    private final JSlider brushRadiusSlider = new JSlider(MIN_RADIUS, MAX_RADIUS, DEFAULT_RADIUS);
    private final JSlider brushDensitySlider = new JSlider(MIN_DENSITY, MAX_DENSITY, DEFAULT_DENSITY);

    public MazeEditingPanel(MazeView mazeView, MazeHolder mazeHolder) {
        this.mazeView = mazeView;
        this.mazeHolder = mazeHolder;

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        final var constraints = initializeConstraints();
        addToolbox(constraints);
        addBrushPicker(constraints);
        addIndestructibleCellsSelector(constraints);
        addRadiusField(constraints);
        addDensityField(constraints);
        addOutsideBrushPicker(constraints);
        addUndoRedoButtons(constraints);

        addMazeViewMouseListeners();
    }

    private GridBagConstraints initializeConstraints() {
        final var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipady = 5;
        constraints.ipadx = 20;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.gridx = 0;
        constraints.gridy = 0;
        return constraints;
    }

    private void addToolbox(GridBagConstraints constraints) {
        toolbox.addItem(new BrushTool(
                mazeHolder,
                brushRadiusSlider::getValue,
                () -> brushDensitySlider.getValue() / 100.0,
                this::getSelectedBrushType,
                indestructibleCellsModel::getSelectedItems
        ));

        toolbox.addItem(new BucketTool(mazeHolder, this::getSelectedBrushType));
        toolbox.addItem(new StartMarkerTool(mazeHolder));
        toolbox.addItem(new FinishMarkerTool(mazeHolder));

        ((JLabel) toolbox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        addComponents(constraints, new JLabel("Toolbox:", JLabel.CENTER), toolbox);
    }

    private void addBrushPicker(GridBagConstraints constraints) {
        final var brushPicker = new SelectionView<>(TerrainCell.Type.values(), brushModel);
        brushPicker.setEditable(true);
        brushPicker.setEditor(new TerrainCellSelectionEditor(brushModel));
        brushPicker.setRenderer(new TerrainCellSelectionRenderer(true, brushModel));
        addComponents(constraints, new JLabel("Brush:", JLabel.CENTER), brushPicker);
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

        brushRadiusSlider.setPreferredSize(SLIDER_DIMENSION);
        brushRadiusSlider.addChangeListener(e -> radiusLabel.setText("Radius (" + brushRadiusSlider.getValue() + "):"));

        addComponents(constraints, radiusLabel, brushRadiusSlider);
    }

    private void addDensityField(GridBagConstraints constraints) {
        var densityLabel = new JLabel("Density (" + DEFAULT_DENSITY + "%):", JLabel.CENTER);
        densityLabel.setPreferredSize(DENSITY_LABEL_DIMENSION);

        brushDensitySlider.setPreferredSize(SLIDER_DIMENSION);
        brushDensitySlider.addChangeListener(e -> densityLabel.setText("Density (" + brushDensitySlider.getValue() + "%):"));

        addComponents(constraints, densityLabel, brushDensitySlider);
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

    private void addUndoRedoButtons(GridBagConstraints constraints) {
        addComponents(constraints, new UndoButton(), new RedoButton());
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
            public void mousePressed(MouseEvent e) {
                handleMouseEvent(e, (cellX, cellY, mouseEvent) -> getSelectedTool().mousePressed(cellX, cellY, mouseEvent));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseEvent(e, (cellX, cellY, mouseEvent) -> getSelectedTool().mouseReleased(cellX, cellY, mouseEvent));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseEvent(e, (cellX, cellY, mouseEvent) -> getSelectedTool().mouseClicked(cellX, cellY, mouseEvent));
            }
        });

        mazeView.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseEvent(e, (cellX, cellY, mouseEvent) -> getSelectedTool().mouseDragged(cellX, cellY, mouseEvent));
            }
        });
    }

    private void handleMouseEvent(MouseEvent event, TriConsumer<Integer, Integer, MouseEvent> consumer) {
        final var maze = mazeHolder.getMaze();
        final var cellDimension = Math.min(mazeView.getWidth() / maze.getWidth(), mazeView.getHeight() / maze.getHeight());
        if (cellDimension == 0) return;

        final var remainderWidth = (mazeView.getWidth() - cellDimension * maze.getWidth()) / 2;
        final var remainderHeight = (mazeView.getHeight() - cellDimension * maze.getHeight()) / 2;

        final var x = (event.getX() - remainderWidth) / cellDimension;
        final var y = (event.getY() - remainderHeight) / cellDimension;

        consumer.accept(x, y, event);
    }

    private Tool getSelectedTool() {
        return (Tool) toolbox.getSelectedItem();
    }

    private TerrainCell.Type getSelectedBrushType() {
        return brushModel.getSelectedItems().iterator().next();
    }

    @FunctionalInterface
    private interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}

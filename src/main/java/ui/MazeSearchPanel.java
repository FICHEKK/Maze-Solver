package ui;

import models.Maze;
import models.MazeHolder;
import models.cells.TerrainCell;
import search.*;
import util.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.*;

public class MazeSearchPanel extends JPanel {
    private static final int MIN_STEP_DURATION = 0;
    private static final int DEFAULT_STEP_DURATION = 10;
    private static final int MAX_STEP_DURATION = 10000;

    private static final int PADDING = 10;
    private static final String SEARCH_BUTTON_START_TEXT = "Search";
    private static final String SEARCH_BUTTON_STOP_TEXT = "Stop";
    private static final Color SEARCH_BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color SEARCH_BUTTON_START_COLOR = new Color(162, 97, 12, 255);
    private static final Color SEARCH_BUTTON_STOP_COLOR = new Color(159, 0, 0, 255);
    private static final String CLEAR_BUTTON_TEXT = "Clear";
    private static final Color CLEAR_BUTTON_BACKGROUND_COLOR = Color.WHITE;
    private static final String SEARCH_RESULT_LABEL_TEXT = "Cost: - | Visited: -";
    private static final String SEARCH_ICON_PATH = "/icons/search.png";
    private static final String CLEAR_ICON_PATH = "/icons/clear.png";

    private final JComboBox<SearchAlgorithm<TerrainCell>> searchAlgorithmPicker = new JComboBox<>();
    private final JButton searchButton = new JButton();
    private final JButton clearButton = new JButton();
    private final JTextField stepDurationField = new JTextField(String.valueOf(DEFAULT_STEP_DURATION));
    private final JLabel searchResultLabel = new JLabel();

    private MazeSearchAnimationWorker animationWorker;

    public MazeSearchPanel() {
        MazeHolder.getInstance().addListener(maze -> handleMazeChange());

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        var constraints = new GridBagConstraints();
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridx = 0;
        addSearchAlgorithmPicker(constraints);
        addSearchButton(constraints);
        addClearButton(constraints);
        addStepDurationField(constraints);
        addSearchResultLabel(constraints);
    }

    private void handleMazeChange() {
        if (isAnimationWorkerRunning()) {
            stopAnimationWorker();
        }

        searchResultLabel.setText(SEARCH_RESULT_LABEL_TEXT);
        clearButton.setEnabled(false);
    }

    private void addSearchAlgorithmPicker(GridBagConstraints constraints) {
        searchAlgorithmPicker.addItem(new AStar<>(cell -> MazeHolder.getInstance().getMaze().getDiagonalManhattanDistanceToFinish(cell)));
        searchAlgorithmPicker.addItem(new Dijkstra<>());
        searchAlgorithmPicker.addItem(new GreedyBestFirstSearch<>(cell -> MazeHolder.getInstance().getMaze().getDiagonalManhattanDistanceToFinish(cell)));
        searchAlgorithmPicker.addItem(new BreadthFirstSearch<>());
        searchAlgorithmPicker.addItem(new DepthFirstSearch<>());

        ((JLabel) searchAlgorithmPicker.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        add(searchAlgorithmPicker, constraints);
    }

    private void addSearchButton(GridBagConstraints constraints) {
        searchButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearchButtonClick();
            }
        });

        searchButton.setText(SEARCH_BUTTON_START_TEXT);
        searchButton.setForeground(SEARCH_BUTTON_TEXT_COLOR);
        searchButton.setBackground(SEARCH_BUTTON_START_COLOR);
        searchButton.setIcon(ImageUtils.loadIcon(SEARCH_ICON_PATH));
        add(searchButton, constraints);
    }

    private void handleSearchButtonClick() {
        if (isAnimationWorkerRunning()) {
            stopAnimationWorker();
        }
        else {
            startAnimationWorker();
        }
    }

    private boolean isAnimationWorkerRunning() {
        return animationWorker != null && !animationWorker.isDone();
    }

    private void stopAnimationWorker() {
        final var mayInterruptIfRunning = true;
        animationWorker.cancel(mayInterruptIfRunning);
    }

    private void startAnimationWorker() {
        try {
            final var stepDuration = getStepDuration();
            final var maze = MazeHolder.getInstance().getMaze();
            final var searchResult = searchMaze(maze);

            this.animationWorker = new MazeSearchAnimationWorker(maze, searchResult, stepDuration);

            this.animationWorker.addPropertyChangeListener(event -> {
                searchButton.setText(animationWorker.isDone() ? SEARCH_BUTTON_START_TEXT : SEARCH_BUTTON_STOP_TEXT);
                searchButton.setBackground(animationWorker.isDone() ? SEARCH_BUTTON_START_COLOR : SEARCH_BUTTON_STOP_COLOR);
                clearButton.setEnabled(animationWorker.isDone());
                searchResultLabel.setText(animationWorker.isDone() ? searchResult.toString() : SEARCH_RESULT_LABEL_TEXT);
            });

            animationWorker.execute();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }

    private int getStepDuration() {
        final var stepDurationText = stepDurationField.getText();
        final int stepDuration;

        try {
            stepDuration = Integer.parseInt(stepDurationText);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("'" + stepDurationText + "' is not a valid step duration value.");
        }

        if (stepDuration < MIN_STEP_DURATION || stepDuration > MAX_STEP_DURATION)
            throw new NumberFormatException("Step duration must be in range [" + MIN_STEP_DURATION + ", " + MAX_STEP_DURATION + "].");

        return stepDuration;
    }

    private SearchResult<TerrainCell> searchMaze(Maze maze) {
        Supplier<TerrainCell> initial = maze::getTerrainCellAtStart;
        Consumer<TerrainCell> consumer = cell -> {};
        Predicate<TerrainCell> goal = cell -> cell.equals(maze.getTerrainCellAtFinish());
        Function<TerrainCell, List<TerrainCell>> successors = maze::getEightNeighboursConsideringTraversal;

        ToDoubleBiFunction<TerrainCell, TerrainCell> weight = (cell1, cell2) -> {
            var dx = cell1.getX() - cell2.getX();
            var dy = cell1.getY() - cell2.getY();
            var distance = Math.sqrt(dx * dx + dy * dy);
            return (cell1.getType().getWeight() + cell2.getType().getWeight()) * (distance / 2);
        };

        return getSelectedSearchAlgorithm().search(initial, consumer, successors, weight, goal);
    }

    private SearchAlgorithm<TerrainCell> getSelectedSearchAlgorithm() {
        //noinspection unchecked
        return ((SearchAlgorithm<TerrainCell>) searchAlgorithmPicker.getSelectedItem());
    }

    private void addClearButton(GridBagConstraints constraints) {
        clearButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MazeHolder.getInstance().getMaze().clearSearchLayer();
                searchResultLabel.setText(SEARCH_RESULT_LABEL_TEXT);
                clearButton.setEnabled(false);
            }
        });

        clearButton.setBackground(CLEAR_BUTTON_BACKGROUND_COLOR);
        clearButton.setText(CLEAR_BUTTON_TEXT);
        clearButton.setEnabled(false);
        clearButton.setIcon(ImageUtils.loadIcon(CLEAR_ICON_PATH));
        add(clearButton, constraints);
    }

    private void addStepDurationField(GridBagConstraints constraints) {
        constraints.gridwidth = 1;

        constraints.gridx = 0;
        add(new JLabel("Step duration (ms):", JLabel.CENTER), constraints);

        constraints.gridx = 1;
        stepDurationField.setHorizontalAlignment(SwingConstants.CENTER);
        add(stepDurationField, constraints);

        constraints.gridwidth = 2;
        constraints.gridx = 0;
    }

    private void addSearchResultLabel(GridBagConstraints constraints) {
        searchResultLabel.setText(SEARCH_RESULT_LABEL_TEXT);
        searchResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(searchResultLabel, constraints);
    }
}

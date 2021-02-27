package ui;

import models.Maze;
import models.MazeHolder;
import models.cells.TerrainCell;
import search.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.*;

public class MazeSearchPanel extends JPanel {
    private static final int PADDING = 10;
    private static final String SEARCH_BUTTON_START_TEXT = "Search";
    private static final String SEARCH_BUTTON_STOP_TEXT = "Stop";
    private static final Color SEARCH_BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color SEARCH_BUTTON_START_COLOR = new Color(162, 97, 12, 255);
    private static final Color SEARCH_BUTTON_STOP_COLOR = new Color(159, 0, 0, 255);
    private static final String CLEAR_BUTTON_TEXT = "Clear";
    private static final Color CLEAR_BUTTON_BACKGROUND_COLOR = Color.WHITE;
    private static final String SEARCH_RESULT_LABEL_TEXT = "Cost: - | Visited: -";

    private final MazeHolder mazeHolder;
    private final JComboBox<SearchAlgorithm<TerrainCell>> searchAlgorithmPicker = new JComboBox<>();
    private final JButton searchButton = new JButton();
    private final JButton clearButton = new JButton();
    private final JLabel searchResultLabel = new JLabel();

    private MazeSearchAnimationWorker animationWorker;

    public MazeSearchPanel(MazeHolder mazeHolder) {
        this.mazeHolder = mazeHolder;
        this.mazeHolder.addListener(maze -> handleMazeChange());

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipady = 5;
        constraints.insets = new Insets(5, 0, 5, 0);
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridx = 0;
        addSearchAlgorithmPicker(constraints);
        addSearchButton(constraints);
        addClearButton(constraints);
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
        searchAlgorithmPicker.addItem(new AStar<>(cell -> mazeHolder.getMaze().getDiagonalManhattanDistanceToFinish(cell)));
        searchAlgorithmPicker.addItem(new Dijkstra<>());
        searchAlgorithmPicker.addItem(new GreedyBestFirstSearch<>(cell -> mazeHolder.getMaze().getDiagonalManhattanDistanceToFinish(cell)));
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
        var maze = mazeHolder.getMaze();
        var searchResult = searchMaze(maze);

        (animationWorker = new MazeSearchAnimationWorker(maze, searchResult)).addPropertyChangeListener(event -> {
            searchButton.setText(animationWorker.isDone() ? SEARCH_BUTTON_START_TEXT : SEARCH_BUTTON_STOP_TEXT);
            searchButton.setBackground(animationWorker.isDone() ? SEARCH_BUTTON_START_COLOR : SEARCH_BUTTON_STOP_COLOR);
            clearButton.setEnabled(animationWorker.isDone());
            searchResultLabel.setText(animationWorker.isDone() ? searchResult.toString() : SEARCH_RESULT_LABEL_TEXT);
        });

        animationWorker.execute();
    }

    private SearchResult<TerrainCell> searchMaze(Maze maze) {
        Supplier<TerrainCell> initial = maze::getTerrainCellAtStart;
        Consumer<TerrainCell> consumer = cell -> {};
        Predicate<TerrainCell> goal = cell -> cell.equals(maze.getTerrainCellAtFinish());
        Function<TerrainCell, List<TerrainCell>> successors = maze::getNeighbours;

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
                mazeHolder.getMaze().clearSearchLayer();
                searchResultLabel.setText(SEARCH_RESULT_LABEL_TEXT);
                clearButton.setEnabled(false);
            }
        });

        clearButton.setBackground(CLEAR_BUTTON_BACKGROUND_COLOR);
        clearButton.setText(CLEAR_BUTTON_TEXT);
        clearButton.setEnabled(false);
        add(clearButton, constraints);
    }

    private void addSearchResultLabel(GridBagConstraints constraints) {
        searchResultLabel.setText(SEARCH_RESULT_LABEL_TEXT);
        searchResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(searchResultLabel, constraints);
    }
}

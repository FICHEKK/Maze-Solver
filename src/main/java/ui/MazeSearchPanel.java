package ui;

import models.Maze;
import models.cells.NatureCell;
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

    private final MazeView mazeView;
    private final JComboBox<SearchAlgorithm<NatureCell>> searchAlgorithmPicker = new JComboBox<>();
    private final JButton searchButton = new JButton();
    private final JButton clearButton = new JButton();
    private final JLabel searchResultLabel = new JLabel();

    private MazeSearchAnimationWorker animationWorker;

    public MazeSearchPanel(MazeView mazeView) {
        this.mazeView = mazeView;

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

    private void addSearchAlgorithmPicker(GridBagConstraints constraints) {
        searchAlgorithmPicker.addItem(new AStar<>(cell -> mazeView.getMaze().getDiagonalManhattanDistanceToFinish(cell)));
        searchAlgorithmPicker.addItem(new Dijkstra<>());
        searchAlgorithmPicker.addItem(new GreedyBestFirstSearch<>(cell -> mazeView.getMaze().getDiagonalManhattanDistanceToFinish(cell)));
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
        var maze = mazeView.getMaze();
        var searchResult = searchMaze(maze);

        (animationWorker = new MazeSearchAnimationWorker(maze, searchResult)).addPropertyChangeListener(event -> {
            searchButton.setText(animationWorker.isDone() ? SEARCH_BUTTON_START_TEXT : SEARCH_BUTTON_STOP_TEXT);
            searchButton.setBackground(animationWorker.isDone() ? SEARCH_BUTTON_START_COLOR : SEARCH_BUTTON_STOP_COLOR);
            clearButton.setEnabled(animationWorker.isDone());
            searchResultLabel.setText(animationWorker.isDone() ? searchResult.toString() : SEARCH_RESULT_LABEL_TEXT);
        });

        animationWorker.execute();
    }

    private SearchResult<NatureCell> searchMaze(Maze maze) {
        Supplier<NatureCell> initial = maze::getStart;
        Consumer<NatureCell> consumer = cell -> {};
        Predicate<NatureCell> goal = cell -> cell.equals(maze.getFinish());

        Function<NatureCell, List<NatureCell>> successors = cell -> {
            var neighbours = maze.getNeighbours(cell);
            neighbours.removeIf(c -> c.getType() == NatureCell.Type.WALL);
            return neighbours;
        };

        ToDoubleBiFunction<NatureCell, NatureCell> weight = (cell1, cell2) -> {
            var dx = cell1.getX() - cell2.getX();
            var dy = cell1.getY() - cell2.getY();
            var distance = Math.sqrt(dx * dx + dy * dy);
            return (cell1.getType().getWeight() + cell2.getType().getWeight()) * (distance / 2);
        };

        return getSelectedSearchAlgorithm().search(initial, consumer, successors, weight, goal);
    }

    private SearchAlgorithm<NatureCell> getSelectedSearchAlgorithm() {
        //noinspection unchecked
        return ((SearchAlgorithm<NatureCell>) searchAlgorithmPicker.getSelectedItem());
    }

    private void addClearButton(GridBagConstraints constraints) {
        clearButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazeView.getMaze().clearSearchLayer();
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

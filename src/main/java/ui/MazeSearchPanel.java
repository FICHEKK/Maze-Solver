package ui;

import models.Cell;
import search.BreadthFirstSearch;
import search.DepthFirstSearch;
import search.GreedyBestFirstSearch;
import search.SearchAlgorithm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Set;

public class MazeSearchPanel extends JPanel {
    private static final int PADDING = 10;

    private final MazeView mazeView;
    private final JComboBox<SearchAlgorithm<Cell>> searchAlgorithmPicker = new JComboBox<>();
    private final JButton clearButton = new JButton();

    public MazeSearchPanel(MazeView mazeView) {
        this.mazeView = mazeView;

        setLayout(new GridLayout(1, 0, PADDING, 0));
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        addSearchAlgorithmPicker();
        addSearchButton();
        addClearButton();
    }

    private void addSearchAlgorithmPicker() {
        searchAlgorithmPicker.addItem(new DepthFirstSearch<>());
        searchAlgorithmPicker.addItem(new BreadthFirstSearch<>());
        searchAlgorithmPicker.addItem(new GreedyBestFirstSearch<>(cell -> mazeView.getMaze().getManhattanDistanceToFinish(cell)));

        ((JLabel) searchAlgorithmPicker.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        add(searchAlgorithmPicker);
    }

    private void addSearchButton() {
        var searchMazeButton = new JButton();

        searchMazeButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("unchecked")
                var searchAlgorithm = ((SearchAlgorithm<Cell>) searchAlgorithmPicker.getSelectedItem());
                assert searchAlgorithm != null;

                var maze = mazeView.getMaze();
                var visitedCells = new ArrayList<Cell>();

                var path = searchAlgorithm.findPath(
                        maze::getStart,
                        visitedCells::add,
                        cell -> {
                            var neighbours = maze.getNeighbours(cell);
                            neighbours.removeIf(c -> c.getType() == Cell.Type.WALL);
                            return neighbours;
                        },
                        (cell1, cell2) -> 1.0,
                        cell -> cell.equals(maze.getFinish())
                );

                var animationWorker = new MazeSearchAnimationWorker(maze, visitedCells, path);

                animationWorker.addPropertyChangeListener(event -> {
                    if (animationWorker.isDone()) {
                        searchMazeButton.setEnabled(true);
                        clearButton.setEnabled(true);
                    }
                });

                searchMazeButton.setEnabled(false);
                clearButton.setEnabled(false);
                animationWorker.execute();
            }
        });

        searchMazeButton.setText("Search");
        add(searchMazeButton);
    }

    private void addClearButton() {
        clearButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mazeView.getMaze().replaceAllCellsOfType(Set.of(Cell.Type.STEP, Cell.Type.SOLUTION), Cell.Type.PATH);
            }
        });

        clearButton.setText("Clear");
        add(clearButton);
    }
}

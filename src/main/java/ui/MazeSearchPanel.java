package ui;

import models.cells.NatureCell;
import search.BreadthFirstSearch;
import search.DepthFirstSearch;
import search.GreedyBestFirstSearch;
import search.SearchAlgorithm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class MazeSearchPanel extends JPanel {
    private static final int PADDING = 10;

    private final MazeView mazeView;
    private final JComboBox<SearchAlgorithm<NatureCell>> searchAlgorithmPicker = new JComboBox<>();
    private final JButton clearButton = new JButton();

    public MazeSearchPanel(MazeView mazeView) {
        this.mazeView = mazeView;

        setLayout(new GridLayout(1, 0, PADDING, 0));
        setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        addPaintBrushPicker();
        addSearchAlgorithmPicker();
        addSearchButton();
        addClearButton();
    }

    private void addPaintBrushPicker() {
        var paintBrushPicker = new JComboBox<NatureCell.Type>();
        ((JLabel) paintBrushPicker.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        for (var type : NatureCell.Type.values()) {
            paintBrushPicker.addItem(type);
        }

        paintBrushPicker.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                mazeView.setPaintBrush((NatureCell.Type) paintBrushPicker.getSelectedItem());
            }
        });

        add(paintBrushPicker);
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
                var searchAlgorithm = ((SearchAlgorithm<NatureCell>) searchAlgorithmPicker.getSelectedItem());
                assert searchAlgorithm != null;

                var maze = mazeView.getMaze();
                var visited = new ArrayList<NatureCell>();

                var path = searchAlgorithm.findPath(
                        maze::getStart,
                        visited::add,
                        cell -> {
                            var neighbours = maze.getNeighbours(cell);
                            neighbours.removeIf(c -> c.getType() == NatureCell.Type.WALL);
                            return neighbours;
                        },
                        (cell1, cell2) -> (cell1.getType().getWeight() + cell2.getType().getWeight()) / 2,
                        cell -> cell.equals(maze.getFinish())
                );

                var animationWorker = new MazeSearchAnimationWorker(maze, visited, path);

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
                mazeView.getMaze().clearSearchLayer();
            }
        });

        clearButton.setText("Clear");
        add(clearButton);
    }
}

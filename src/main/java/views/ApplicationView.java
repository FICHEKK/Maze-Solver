package views;

import models.Cell;
import models.Maze;
import search.BreadthFirstSearch;
import search.SearchAlgorithm;
import search.SearchNode;
import util.MazeGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ApplicationView extends JFrame {
    private static final String WINDOW_TITLE = "Maze Solver";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;
    private static final int PADDING = 10;

    private static final int MAZE_WIDTH = 30;
    private static final int MAZE_HEIGHT = 30;
    private static final int MIN_DIMENSION = 4;
    private static final int MAX_DIMENSION = 100;

    private final MazeView mazeView = new MazeView();
    private final SearchAlgorithm<Cell> searchAlgorithm = new BreadthFirstSearch<>();

    private JTextField widthTextField;
    private JTextField heightTextField;
    private JSlider wallDensitySlider;

    public ApplicationView() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initGUI();
    }

    private void initGUI() {
        var controlPanel = new JPanel(new GridLayout(1, 0, PADDING, 0));
        controlPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        controlPanel.add(new JLabel("Width:", JLabel.CENTER));
        controlPanel.add(widthTextField = new JTextField());
        widthTextField.setText(String.valueOf(MAZE_WIDTH));

        controlPanel.add(new JLabel("Height:", JLabel.CENTER));
        controlPanel.add(heightTextField = new JTextField());
        heightTextField.setText(String.valueOf(MAZE_HEIGHT));

        wallDensitySlider = new JSlider(0, 100, 100);
        controlPanel.add(new JLabel("Wall density:", JLabel.CENTER));
        controlPanel.add(wallDensitySlider);

        var generateMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze();
            }
        });
        generateMazeButton.setText("Generate");
        controlPanel.add(generateMazeButton);

        var searchMazeButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var maze = mazeView.getMaze();
                var listOfVisitations = new ArrayList<Cell>();

                var solutionHead = searchAlgorithm.findSolution(
                        maze::getStart,
                        listOfVisitations::add,
                        cell -> {
                            var neighbours = maze.getNeighbours(cell);
                            neighbours.removeIf(c -> c.getType() == Cell.WALL);
                            return neighbours;
                        },
                        cell -> cell.equals(maze.getFinish())
                );

                new SearchAnimationSwingWorker(listOfVisitations, solutionHead, maze).execute();
            }
        });
        searchMazeButton.setText("Search");
        controlPanel.add(searchMazeButton);

        add(mazeView, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        generateMaze();
    }

    private void generateMaze() {
        try {
            int width = tryParseInt(widthTextField.getText());
            int height = tryParseInt(heightTextField.getText());

            if (width < MIN_DIMENSION || width > MAX_DIMENSION)
                throw new NumberFormatException("Width must be in range [" + MIN_DIMENSION + ", " + MAX_DIMENSION + "].");

            if (height < MIN_DIMENSION || height > MAX_DIMENSION)
                throw new NumberFormatException("Height must be in range [" + MIN_DIMENSION + ", " + MAX_DIMENSION + "].");

            var wallDensity = wallDensitySlider.getValue() / 100.0;
            var mazeGenerator = new MazeGenerator(width, height, wallDensity);
            mazeView.setMaze(mazeGenerator.generate());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ApplicationView.this, ex.getMessage());
        }
    }

    private int tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException("'" + string + "' is not a valid integer value.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplicationView().setVisible(true));
    }

    private static class SearchAnimationSwingWorker extends SwingWorker<Void, Void> {
        private static final int SEARCH_STEP_DURATION = 5;
        private static final int SOLUTION_STEP_DURATION = 5;

        private final List<Cell> listOfVisitations;
        private final SearchNode<Cell> solutionHead;
        private final Maze maze;

        public SearchAnimationSwingWorker(List<Cell> listOfVisitations, SearchNode<Cell> solutionHead, Maze maze) {
            this.listOfVisitations = listOfVisitations;
            this.solutionHead = solutionHead;
            this.maze = maze;
        }

        @Override
        protected Void doInBackground() throws InterruptedException {
            animateSearchAlgorithm();

            if (solutionHead != null) {
                animateSolution();
            }
            else {
                JOptionPane.showMessageDialog(null, "Solution could not be found.");
            }

            return null;
        }

        private void animateSearchAlgorithm() throws InterruptedException {
            for (var visitedCell : listOfVisitations) {
                maze.setCell(visitedCell.getX(), visitedCell.getY(), Cell.STEP);
                Thread.sleep(SEARCH_STEP_DURATION);
            }
        }

        private void animateSolution() throws InterruptedException {
            for (var node = solutionHead; node != null; node = node.getParent()) {
                maze.setCell(node.getState().getX(), node.getState().getY(), Cell.SOLUTION);
                Thread.sleep(SOLUTION_STEP_DURATION);
            }
        }
    }
}
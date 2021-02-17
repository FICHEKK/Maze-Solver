package ui;

import javax.swing.*;
import java.awt.*;

public class ApplicationView extends JFrame {
    private static final String WINDOW_TITLE = "Maze Solver";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;

    private final MazeView mazeView = new MazeView();

    public ApplicationView() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initGUI();
    }

    private void initGUI() {
        var controlPanel = new JPanel(new GridLayout(0, 1));
        controlPanel.add(new MazeGenerationPanel(mazeView));
        controlPanel.add(new MazeSearchPanel(mazeView));

        add(mazeView, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ApplicationView().setVisible(true));
    }
}
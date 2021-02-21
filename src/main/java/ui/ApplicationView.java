package ui;

import javax.swing.*;
import java.awt.*;

public class ApplicationView extends JFrame {
    private static final String WANTED_LOOK_AND_FEEL = "Nimbus";
    private static final String WINDOW_TITLE = "Maze Solver";
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    private final MazeView mazeView = new MazeView();

    public ApplicationView() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
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
        setWantedLookAndFeelIfPossible();
        SwingUtilities.invokeLater(ApplicationView::new);
    }

    private static void setWantedLookAndFeelIfPossible() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals(WANTED_LOOK_AND_FEEL)) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            // ignore
        }
    }
}
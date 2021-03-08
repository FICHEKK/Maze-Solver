package ui;

import util.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ApplicationView extends JFrame {
    private static final String WANTED_LOOK_AND_FEEL = "Nimbus";
    private static final String WINDOW_ICON_PATH = "/icons/maze.png";
    private static final String WINDOW_TITLE = "Maze Solver";

    private static final int WINDOW_WIDTH = 1440;
    private static final int WINDOW_HEIGHT = 960;
    private static final int PADDING = 20;

    public ApplicationView() {
        setTitle(WINDOW_TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        initGUI();
    }

    private void initGUI() {
        final var mazeView = new MazeView();
        add(mazeView, BorderLayout.CENTER);
        add(createControlPanel(mazeView), BorderLayout.EAST);

        //noinspection ConstantConditions
        setIconImage(ImageUtils.loadIcon(WINDOW_ICON_PATH).getImage());
    }

    private JPanel createControlPanel(MazeView mazeView) {
        var controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 1.0;
        constraints.gridx = 0;

        controlPanel.add(wrapPanelInBorder(new MazeGenerationPanel(), "Generate "), constraints);
        controlPanel.add(new JSeparator(JSeparator.HORIZONTAL), constraints);
        controlPanel.add(wrapPanelInBorder(new MazeEditingPanel(mazeView), "Edit "), constraints);
        controlPanel.add(new JSeparator(JSeparator.HORIZONTAL), constraints);
        controlPanel.add(wrapPanelInBorder(new MazeSearchPanel(), "Search "), constraints);
        controlPanel.add(new JSeparator(JSeparator.HORIZONTAL), constraints);
        controlPanel.add(wrapPanelInBorder(new MazeSaveAndLoadPanel(), "Save / Load "), constraints);

        return controlPanel;
    }

    private JPanel wrapPanelInBorder(JPanel panel, String borderTitle) {
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), borderTitle, TitledBorder.LEFT, TitledBorder.TOP));
        return panel;
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
package ui.buttons;

import util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class ExitButton extends JButton {
    private static final String TEXT = "Exit";
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = Color.RED;
    private static final Dimension DIMENSION = new Dimension(0, 40);
    private static final String ICON_PATH = "/icons/exit.png";

    private final JFrame applicationFrame;

    public ExitButton(JFrame applicationFrame) {
        this.applicationFrame = applicationFrame;
        setGUI();
        addClickListener();
    }

    private void setGUI() {
        setText(TEXT);
        setForeground(TEXT_COLOR);
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(DIMENSION);
        setIcon(ImageUtils.loadIcon(ICON_PATH));
    }

    private void addClickListener() {
        addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
    }

    private void exit() {
        applicationFrame.dispatchEvent(new WindowEvent(applicationFrame, WindowEvent.WINDOW_CLOSING));
    }
}

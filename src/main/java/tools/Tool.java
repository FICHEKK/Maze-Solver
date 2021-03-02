package tools;

import java.awt.event.MouseEvent;

public interface Tool {

    default void mouseClicked(int cellX, int cellY, MouseEvent mouseEvent) { }

    default void mousePressed(int cellX, int cellY, MouseEvent mouseEvent) { }

    default void mouseDragged(int cellX, int cellY, MouseEvent mouseEvent) { }

    default void mouseReleased(int cellX, int cellY, MouseEvent mouseEvent) { }
}

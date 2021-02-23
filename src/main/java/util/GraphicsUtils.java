package util;

import java.awt.*;
import java.util.List;

public final class GraphicsUtils {

    private GraphicsUtils() {
        throw new AssertionError("GraphicsUtils should not be instantiated.");
    }

    public static void drawMultilineString(Graphics g, List<String> lines, int x, int y) {
        for (String line : lines) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
    }

    public static int getMultilineStringWidth(Graphics g, List<String> lines) {
        var maxLineWidth = 0;

        for (String line : lines) {
            var lineWidth = g.getFontMetrics().stringWidth(line);
            if (lineWidth > maxLineWidth) {
                maxLineWidth = lineWidth;
            }
        }

        return maxLineWidth;
    }

    public static int getMultilineStringHeight(Graphics g, List<String> lines) {
        return g.getFontMetrics().getHeight() * lines.size();
    }
}

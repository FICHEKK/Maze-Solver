package util;

import javax.swing.*;
import java.io.IOException;

public final class ImageUtils {

    private ImageUtils() {
        throw new AssertionError("ImageUtils should not be instantiated.");
    }

    public static ImageIcon loadIcon(String path) {
        try (var inputStream = ImageUtils.class.getResourceAsStream(path)) {
            if (inputStream == null)
                throw new IOException("Resource was not found.");

            return new ImageIcon(inputStream.readAllBytes());
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}

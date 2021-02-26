package util;

import models.Maze;
import models.cells.NatureCell;
import models.cells.WaypointCell;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class MazeConverter {

    private MazeConverter() {
        throw new AssertionError("MazeConverter should not be instantiated.");
    }

    public static void serialize(Maze maze, File destination) throws IOException {
        try (var outputStream = new DataOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(destination))))) {
            outputStream.writeShort(maze.getWidth());
            outputStream.writeShort(maze.getHeight());
            outputStream.write(convertMazeNatureCellsToByteArray(maze));

            outputStream.writeShort(maze.getStart().getX());
            outputStream.writeShort(maze.getStart().getY());
            outputStream.writeShort(maze.getFinish().getX());
            outputStream.writeShort(maze.getFinish().getY());
        }
    }

    private static byte[] convertMazeNatureCellsToByteArray(Maze maze) {
        final var bytes = new byte[maze.getWidth() * maze.getHeight()];
        var index = 0;

        for (var y = 0; y < maze.getHeight(); y++) {
            for (var x = 0; x < maze.getWidth(); x++) {
                bytes[index++] = (byte) maze.getNatureCell(x, y).getType().ordinal();
            }
        }

        return bytes;
    }

    public static Maze deserialize(File source) throws IOException {
        try (var inputStream = new DataInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(source))))) {
            var width = (int) inputStream.readShort();
            var height = (int) inputStream.readShort();
            var bytes = inputStream.readNBytes(width * height);

            var types = NatureCell.Type.values();
            var natureCells = new NatureCell[height][width];

            var index = 0;

            for (var y = 0; y < height; y++) {
                for (var x = 0; x < width; x++) {
                    natureCells[y][x] = new NatureCell(x, y, types[bytes[index++]]);
                }
            }

            var start = new WaypointCell(inputStream.readShort(), inputStream.readShort(), WaypointCell.Type.START);
            var finish = new WaypointCell(inputStream.readShort(), inputStream.readShort(), WaypointCell.Type.FINISH);

            return new Maze(natureCells, start, finish);
        }
    }
}

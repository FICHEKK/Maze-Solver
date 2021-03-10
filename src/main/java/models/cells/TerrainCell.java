package models.cells;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class TerrainCell extends Cell {
    private Type type;

    public TerrainCell(int x, int y, Type type) {
        super(x, y);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isTraversable() {
        return type.weight != Double.POSITIVE_INFINITY;
    }

    @Override
    public Color getColor() {
        return type.getColor();
    }

    public enum Type {
        DIRT((byte) 'D', 1, new Color(162, 97, 12)),
        GRASS((byte) 'G', 1, new Color(0, 172, 0)),
        SAND((byte) 'S', 2, new Color(255, 192, 0)),
        SNOW((byte) 's', 3, new Color(255, 255, 255)),
        ICE((byte) 'I', 4, new Color(180, 207, 250)),
        WATER((byte) 'W', 5, new Color(0, 146, 255)),
        BUSH((byte) 'B', Double.POSITIVE_INFINITY, new Color(5, 91, 0)),
        ROCK((byte) 'R', Double.POSITIVE_INFINITY, new Color(146, 146, 146)),
        LAVA((byte) 'L', Double.POSITIVE_INFINITY, new Color(127, 0, 0));

        private static final Map<Byte, Type> ID_TO_TYPE = new HashMap<>();

        static {
            for (var type : values()) {
                if (ID_TO_TYPE.containsKey(type.getId())) {
                    final var takenIdType = ID_TO_TYPE.get(type.getId());
                    throw new AssertionError(takenIdType.name() + " and " + type.name() + " have the same identifier.");
                }

                ID_TO_TYPE.put(type.getId(), type);
            }
        }

        public static Type fromId(byte id) {
            return ID_TO_TYPE.get(id);
        }

        public static Type first() {
            return values()[0];
        }

        private final byte id;
        private final double weight;
        private final Color color;

        Type(byte id, double weight, Color color) {
            this.id = id;
            this.weight = weight;
            this.color = color;
        }

        public byte getId() {
            return id;
        }

        public double getWeight() {
            return weight;
        }

        public Color getColor() {
            return color;
        }

        @Override
        public String toString() {
            return capitalizeFirstLetter(name());
        }

        private static String capitalizeFirstLetter(String word) {
            return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        }
    }
}

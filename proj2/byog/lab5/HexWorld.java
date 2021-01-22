package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    /**
     * The width of the window and the array.
     */
    private int width;

    /**
     * The height of the window and the array.
     */
    private int height;

    /**
     * The side length of a single hexagon.
     */
    private int sideLength;

    /**
     * The world that you can draw hexagons on. Needs to be initialized when using.
     */
    private TETile[][] world;

    /**
     * The renderer.
     */
    private TERenderer ter;

    /**
     * CONSTRUCTOR
     * Calculates the width and height of the window.
     * Initializes the renderer and array.
     * Fills the array with TETile.NOTHING(BLACK TILES).
     * @param sideLength The side length of a single hexagon
     */
    public HexWorld(int sideLength) {
        this.sideLength = sideLength;
        width = (sideLength + 2 * (sideLength - 1)) * 3 + 2 * sideLength;
        height = 5 * sideLength * 2;
        world = new TETile[width][height];
        ter = new TERenderer();
        ter.initialize(width, height);
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Generates a hex world with 19 hexagons, each hexagon has a random type.
     */
    public void generateHexWorld() {
        int x, y, type;
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            x = sideLength - 1 + i * (2 * sideLength - 1);
            y = height / 2 - sideLength * 3 - i * sideLength;
            for (int j = 0; j < 3 + i; j++) {
                type = random.nextInt(5);
                addHexagon(x, y + j * sideLength * 2, type);
                if (i != 2) {
                    type = random.nextInt(5);
                    addHexagon(width - (i + 1) * (2 * sideLength - 1),
                            y + j * sideLength * 2, type);
                }
            }
        }
    }

    /**
     * HELPER METHOD
     * Adds a hexagon of side length sideLength size to a given position in the world.
     * @param x The x position of the Top-Left corner of the hexagon.
     * @param y The y position of the Top-Left corner of the hexagon.
     * @param type The type of the hexagonal land.
     */
    public void addHexagon(int x, int y, int type) {
        for (int  i = 0; i < sideLength; i++) {
            drawLine(sideLength + i * 2, x - i, y + i, type);
            drawLine(sideLength + i * 2, x - i, y + i - 1 + (sideLength - i) * 2, type);
        }
    }

    /**
     * HELPER METHOD
     * Draws a single line with specific length. The hexagon can be divided into multiple
     * lines with different length. So we can draw the lines respectively and the combination of
     * these lines is a hexagon.
     * @param lineLength The length of the line.
     * @param x The x start position of the line.
     * @param y The y start position of the line.
     * @param type The type of the linear land.
     */
    private void drawLine(int lineLength, int x, int y, int type) {
        TETile tileType = getType(type);
        for (int i = 0; i < lineLength; i++) {
            if (x + i < 0 | y < 0 | x + i >= width | y >= height) {
                continue;
            }
            world[x + i][y] = tileType;
        }
    }

    /**
     * Show the world.
     */
    public void show() {
        ter.renderFrame(world);
    }

    /**
     * HELPER METHOD
     * Get the TYPE of the land.
     * @param type The code of the type. 0 for GRASS, 1 for FLOWER, 2 for SAND(DESERT),
     *             3 for TREE(FOREST) and 4 for MOUNTAIN.
     */
    private TETile getType(int type) {
        switch (type) {
            case 0:
                return Tileset.GRASS;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.SAND;
            case 3:
                return Tileset.TREE;
            case 4:
                return Tileset.MOUNTAIN;
            default:
                return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        HexWorld world = new HexWorld(5);
        world.generateHexWorld();
        world.show();
    }
}


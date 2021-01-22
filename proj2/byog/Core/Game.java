package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        TETile[][] map = new TETile[WIDTH][HEIGHT];
        Position player = null;
        long seedNumerical = 0;
        input = input.toUpperCase();
        int startOperate = 0;
        if (input.charAt(0) == 'N') {
            int endOfSeed = input.indexOf('S');
            String seed = input.substring(1, endOfSeed);
            seedNumerical = Long.valueOf(seed);
            //ter.initialize(WIDTH, HEIGHT);
            WorldGenerator generator = new WorldGenerator(map, seedNumerical);
            player = generator.generate();
            //ter.renderFrame(map);
            startOperate = endOfSeed + 1;
        } else if (input.charAt(0) == 'L') {
            player = loadGame(map);
            startOperate = 1;
        }
        int length = input.length();
        while (startOperate < length) {
            char operate = input.charAt(startOperate);
            if (operate != ':') {
                player = operate(operate, map, player);
            } else {
                if (startOperate + 1 < length && input.charAt(startOperate + 1) == 'Q') {
                    saveGame(seedNumerical, player);
                    break;
                }
            }
            startOperate++;
        }
        TETile[][] finalWorldFrame = map;
        return finalWorldFrame;
    }

    /**
     * This method is used to do some interactive operations in the game such as moving.
     * 'w' and 'W' are for moving up, 'a' and 'A' are for moving left,
     * 's' and 'S' are for moving down, 'd' and 'D' are for moving right.
     * @param operate The command from the player.
     * @param map The map(world) of the game.
     * @param player The position of the player.
     */
    private Position operate(char operate, TETile[][] map, Position player) {
        int x = player.getXPosition();
        int y = player.getYPosition();
        switch (operate) {
            case 'W':
                return moveToTarget(new Position(x, y + 1), player, map);
            case 'A':
                return moveToTarget(new Position(x - 1, y), player, map);
            case 'S':
                return moveToTarget(new Position(x, y - 1), player, map);
            case 'D':
                return moveToTarget(new Position(x + 1, y), player, map);
            default:
                return player;
        }
    }

    private Position moveToTarget(Position targetPosition, Position player, TETile[][] map) {
        int targetX = targetPosition.getXPosition();
        int targetY = targetPosition.getYPosition();
        int x = player.getXPosition();
        int y = player.getYPosition();
        if (map[targetX][targetY] == Tileset.FLOOR) {
            map[targetX][targetY] = Tileset.PLAYER;
            map[x][y] = Tileset.FLOOR;
            return new Position(targetX, targetY);
        } else if (map[targetX][targetY] == Tileset.LOCKED_DOOR) {
            map[targetX][targetY] = Tileset.UNLOCKED_DOOR;
            return player;
        } else if (map[targetX][targetY] == Tileset.UNLOCKED_DOOR) {
            map[targetX][targetY] = Tileset.PLAYER;
            map[x][y] = Tileset.FLOOR;
            return new Position(targetX, targetY);
        }
        return player;
    }

    private Position loadGame(TETile[][] map) {
        long seed = 0;
        int x = 0;
        int y = 0;
        try {
            FileInputStream fis = new FileInputStream("./save.txt");
            DataInputStream dis = new DataInputStream(fis);
            seed = dis.readLong();
            x = dis.readInt();
            y = dis.readInt();
            dis.close();
            fis.close();
        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException ie) {
            ie.printStackTrace();
            System.exit(0);
        }
        WorldGenerator generator = new WorldGenerator(map, seed);
        Position player = generator.generate();
        player = moveToTarget(new Position(x, y), player, map);
        return player;
    }

    private void saveGame(long seed, Position player) {
        File f = new File("./save.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(f);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeLong(seed);
            dos.writeInt(player.getXPosition());
            dos.writeInt(player.getYPosition());
            dos.close();
            fos.close();
        } catch (FileNotFoundException fe) {
            System.out.println("File not found");
            System.exit(0);
        } catch (IOException ie) {
            ie.printStackTrace();
            System.exit(0);
        }
    }
}

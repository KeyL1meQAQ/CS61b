package hw4.puzzle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Board implements WorldState {

    private int[][] tiles;

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j
     */
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     */
    public int tileAt(int i, int j) {
        if (i > -1 && i < size() && j > -1 && j < size()) {
            return tiles[i][j];
        }
        throw new IndexOutOfBoundsException("Index out of bound: i and j need"
                + "to be numbers between 0 and N-1");
    }

    /**
     * Returns the board size N
     */
    public int size() {
        return tiles.length;
    }

    /**
     * Returns the neighbors of the current board
     */
    public Iterable<WorldState> neighbors() {
        Set<WorldState> neighbors = new HashSet<>();
        int blankX = 0, blankY = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) == 0) {
                    blankX = i;
                    blankY = j;
                }
            }
        }
        int[][] nextState = new int[size()][size()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                nextState[i][j] = tileAt(i, j);
            }
        }
        int[][] surround = surround(blankX, blankY);
        for (int i = 0; i < 4; i++) {
            if (tileAt(surround[i][0], surround[i][1]) != 0) {
                nextState[blankX][blankY] = nextState[surround[i][0]][surround[i][1]];
                nextState[surround[i][0]][surround[i][1]] = 0;
                Board board = new Board(nextState);
                neighbors.add(board);
                nextState[surround[i][0]][surround[i][1]] = nextState[blankX][blankY];
                nextState[blankX][blankY] = 0;
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate
     */
    public int hamming() {
        int hamming = 0;
        int size = size();
        for (int i = 1; i < size * size; i++) {
            if (tileAt(toX(i), toY(i)) != i) {
                hamming++;
            }
        }
        return hamming;
    }

    /**
     * Manhattan estimate
     */
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) != 0 && tileAt(i, j) != i * size() + j + 1) {
                    manhattan = manhattan + Math.abs(i - toX(tileAt(i, j)))
                            + Math.abs(j - toY(tileAt(i, j)));
                }
            }
        }
        return manhattan;
    }

    /**
     * Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * Returns true if this board's tile values are the same
     * position as y's
     */
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board board = (Board) y;
        if (size() != board.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                if (tileAt(i, j) != board.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    /**
     * Get the index of the tiles surrounded.
     * @return
     */
    private int[][] surround(int i, int j) {
        if (i < 0 || i > size() - 1 || j < 0 || j > size() - 1) {
            throw new IndexOutOfBoundsException("Index out of bound: i and j need"
                    + "to be numbers between 0 and N-1");
        }
        int[][] surround = new int[4][2];
        surround[0][0] = getIndexSurround(i, 0);
        surround[0][1] = j;
        surround[1][0] = getIndexSurround(i, 1);
        surround[1][1] = j;
        surround[2][0] = i;
        surround[2][1] = getIndexSurround(j, 0);
        surround[3][0] = i;
        surround[3][1] = getIndexSurround(j, 1);
        return surround;
    }

    private int getIndexSurround(int i, int mode) {
        switch (mode) {
            case 0:
                if (i - 1 < 0) {
                    return i;
                }
                return i - 1;
            case 1:
                if (i + 1 > size() - 1) {
                    return i;
                }
                return  i + 1;
            default:
                return -1;
        }
    }

    private int[] toXY(int i) {
        int[] xy = new int[2];
        int x = toX(i);
        int y = toY(i);
        if (y - 1 < 0) {
            xy[0] = x - 1;
            xy[1] = size() - 1;
        } else {
            xy[0] = x;
            xy[1] = y - 1;
        }
        return xy;
    }

    private int toX(int i) {
        return (i - 1) / size();
    }

    private int toY(int i) {
        return (i - 1) % size();
    }
}

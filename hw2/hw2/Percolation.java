package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF gridDisjointSet;

    private int sideLength;

    private int[][] grid;

    private int numberOfOpen;

    /**
     * Create N-by-N grid, with all the sites initially blocked.
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Illegal argument, N needs to be larger than 0");
        }
        sideLength = N;
        gridDisjointSet = new WeightedQuickUnionUF(sideLength * sideLength);
        grid = new int[sideLength][sideLength];
        for (int i = 0; i < sideLength; i++) {
            for (int j = 0; j < sideLength; j++) {
                grid[i][j] = 0;
            }
        }
        numberOfOpen = 0;
    }

    /**
     * Open the site(row, col) if it is not open already.
     */
    public void open(int row, int col) {
        if (row < 0 | col < 0 | row > sideLength - 1 | col > sideLength - 1) {
            throw new IndexOutOfBoundsException("Index out of bound: row or col cannot be larger"
                    + "than N - 1 or smaller than 0");
        }
        if (grid[row][col] == 0) {
            grid[row][col] = 1;
            numberOfOpen++;
            connectSurrounding(row, col);
        }
    }

    /**
     * Is the site(row, col) opened?
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 | col < 0 | row > sideLength - 1 | col > sideLength - 1) {
            throw new IndexOutOfBoundsException("Index out of bound: row or col cannot be larger"
                    + "than N - 1 or smaller than 0");
        }
        if (grid[row][col] == 0) {
            return false;
        }
        return true;
    }

    /**
     * Is the site(row, col) full?
     */
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) {
            return false;
        }
        if (row == 0) {
            return true;
        }
        int thisSite = row * sideLength + col;
        int root = gridDisjointSet.find(thisSite);
        for (int i = 0; i < sideLength; i++) {
            int targetSite = i;
            if (root == gridDisjointSet.find(targetSite)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Number of open sites.
     */
    public int numberOfOpenSites() {
        return numberOfOpen;
    }

    /**
     * Does the system percolates?
     */
    public boolean percolates() {
        int thisSite, targetSite;
        for (int i = 0; i < sideLength; i++) {
            if (isOpen(0, i)) {
                thisSite = 0 * sideLength + i;
                int root = gridDisjointSet.find(thisSite);
                for (int j = 0; j < sideLength; j++) {
                    if (isOpen(sideLength - 1, j)) {
                        targetSite = (sideLength - 1) * sideLength + j;
                        if (gridDisjointSet.find(targetSite) == root) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Connect an opened site with all the sites surrounding which are opened already.
     */
    private void connectSurrounding(int row, int col) {
        int thisSite = row * sideLength + col;
        int targetSite;
        if (row - 1 > -1) {
            if (isOpen(row - 1, col)) {
                targetSite = (row - 1) * sideLength + col;
                if (!gridDisjointSet.connected(thisSite, targetSite)) {
                    gridDisjointSet.union(thisSite, targetSite);
                }
            }
        }
        if (row + 1 < sideLength) {
            if (isOpen(row + 1, col)) {
                targetSite = (row + 1) * sideLength + col;
                if (!gridDisjointSet.connected(thisSite, targetSite)) {
                    gridDisjointSet.union(thisSite, targetSite);
                }
            }
        }
        if (col - 1 > -1) {
            if (isOpen(row, col - 1)) {
                targetSite = row * sideLength + col - 1;
                if (!gridDisjointSet.connected(thisSite, targetSite)) {
                    gridDisjointSet.union(thisSite, targetSite);
                }
            }
        }
        if (col + 1 < sideLength) {
            if (isOpen(row, col + 1)) {
                targetSite = row * sideLength + col + 1;
                if (!gridDisjointSet.connected(thisSite, targetSite)) {
                    gridDisjointSet.union(thisSite, targetSite);
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}

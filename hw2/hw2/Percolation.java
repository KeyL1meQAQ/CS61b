package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.introcs.StdRandom;

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
        gridDisjointSet = new WeightedQuickUnionUF(sideLength * sideLength + 2);
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
        int thisSite = coordinateTo1D(row, col);
        int topNode = sideLength * sideLength;
        if (gridDisjointSet.connected(thisSite, topNode)) {
            return true;
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
        int topNode = sideLength * sideLength;
        int bottomNode = topNode + 1;
        if (gridDisjointSet.connected(topNode, bottomNode)) {
            return true;
        }
        return false;
    }

    /**
     * Connect an opened site with all the sites surrounding which are opened already.
     */
    private void connectSurrounding(int row, int col) {
        int thisSite = coordinateTo1D(row, col);
        int targetSite;
        if (row - 1 > -1) {
            if (isOpen(row - 1, col)) {
                targetSite = coordinateTo1D(row - 1, col);
                gridDisjointSet.union(thisSite, targetSite);
            }
        }
        if (row + 1 < sideLength) {
            if (isOpen(row + 1, col)) {
                targetSite = coordinateTo1D(row + 1, col);
                gridDisjointSet.union(thisSite, targetSite);
            }
        }
        if (col - 1 > -1) {
            if (isOpen(row, col - 1)) {
                targetSite = coordinateTo1D(row, col - 1);
                gridDisjointSet.union(thisSite, targetSite);
            }
        }
        if (col + 1 < sideLength) {
            if (isOpen(row, col + 1)) {
                targetSite = coordinateTo1D(row, col + 1);
                gridDisjointSet.union(thisSite, targetSite);
            }
        }
        if (row == 0) {
            targetSite = sideLength * sideLength;
            gridDisjointSet.union(thisSite, targetSite);
        }
        if (row == sideLength - 1) {
            targetSite = sideLength * sideLength + 1;
            gridDisjointSet.union(thisSite, targetSite);
        }
    }

    private int coordinateTo1D(int row, int col) {
        return sideLength * row + col;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(20);
        do {
            int row = StdRandom.uniform(20);
            int col = StdRandom.uniform(20);
            percolation.open(row, col);
        } while (!percolation.percolates());
        double threshold = percolation.numberOfOpenSites() / 400d;
        System.out.println(threshold);
    }
}

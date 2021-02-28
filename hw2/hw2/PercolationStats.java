package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private double[] results;

    private int times;

    /**
     * Perform T independent experiment in a N-by-N grid.
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N < 0 | T < 0) {
            throw new IllegalArgumentException("Illegal argument: T and N need to be"
                    + "larger than 0");
        }
        times = T;
        results = new double[times];
        for (int i = 0; i < times; i++) {
            Percolation percolation = pf.make(N);
            do {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                percolation.open(row, col);
            } while (!percolation.percolates());
            results[i] = percolation.numberOfOpenSites() / (double) (N * N);
        }
    }

    /**
     * Sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * Sample standard deviation of percolation threshold.
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * Low endpoint of 95% confidence interval
     */
    public double confidenceLow() {
        return mean() - (1.96 * stddev()) / Math.sqrt(times);
    }

    /**
     * High endpoint of 95% confidence interval
     */
    public double confidenceHigh() {
        return mean() + (1.96 * stddev()) / Math.sqrt(times);
    }

}

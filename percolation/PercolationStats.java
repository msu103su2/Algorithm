/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int trials;
    private double[] results;
    private double avg;
    private double s;
    private final double confidence95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.trials = trials;
            results = new double[trials];
            for (int i = 0; i < trials; i++) {
                Percolation p = new Percolation(n);
                double ni = p.numberOfOpenSites();
                results[i] = ni / (n * n);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        avg = StdStats.mean(results);
        return avg;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        s = StdStats.stddev(results);
        return s;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return avg - confidence95 * s / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return avg + confidence95 * s / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[0]);
        PercolationStats PS = new PercolationStats(n, trails);
        StdOut.println(
                "Mean =" + PS.mean() + '\n' + "Stddev =" + PS.stddev() + '\n'
                        + "95% confidence interval =["
                        + PS.confidenceLo() + ", " + PS.confidenceHi() + ']');
    }
}

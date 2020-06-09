/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private double avg;
    private double s;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.trials = trials;
            double[] results = new double[trials];
            int[] order = new int[n * n];
            for (int i = 0; i < n * n; i++) {
                order[i] = i;
            }
            for (int i = 0; i < trials; i++) {
                StdRandom.shuffle(order);
                Percolation p = new Percolation(n);
                int j = 0;
                while (!p.percolates()) {
                    int row = order[j] / n + 1;
                    int col = order[j] % n + 1;
                    p.open(row, col);
                    j++;
                }
                results[i] = (1.0 * j) / (n * n);
                avg = StdStats.mean(results);
                s = StdStats.stddev(results);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return avg;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return s;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return avg - 1.96 * s / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return avg + 1.96 * s / Math.sqrt(trials);
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

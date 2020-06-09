/* *****************************************************************************
 *  Name: Shan
 *  Date: 06/08/2020
 *  Description: Hw1
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private WeightedQuickUnionUF qf;
    private int openSites;
    private final int n;
    private final int nP;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 0) {
            throw new NegativeArraySizeException();
        }
        else if (n == 0) {
            throw new IllegalArgumentException();
        }
        else {
            this.n = n;
            this.nP = n + 2;
            openSites = 0;
            grid = new int[nP][nP];
            qf = new WeightedQuickUnionUF(nP * nP);
            for (int i = 0; i < nP; i++)
                for (int j = 0; j < nP; j++) {
                    grid[i][j] = 0;
                }
            for (int j = 0; j < n; j++) {
                grid[0][j + 1] = 1;
                grid[n + 1][j + 1] = 1;
                qf.union(j, j + 1);
                qf.union(nP * (n + 1) + j, nP * (n + 1) + j + 1);
            }
        }
    }

    // connect two sites
    private void connect(int row0, int col0, int row1, int col1) {
        qf.union(row0 * nP + col0, row1 * nP + col1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > n + 1 || col < 0 || col > n + 1) {
            throw new IllegalArgumentException();
        }
        else {
            if (!isOpen(row, col)) {
                grid[row][col] = 1;
                openSites++;
                if (isOpen(row - 1, col)) {
                    connect(row, col, row - 1, col);
                }
                if (isOpen(row, col - 1)) {
                    connect(row, col, row, col - 1);
                }
                if (isOpen(row + 1, col)) {
                    connect(row, col, row + 1, col);
                }
                if (isOpen(row, col + 1)) {
                    connect(row, col, row, col + 1);
                }
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > n + 1 || col < 0 || col > n + 1) {
            throw new IllegalArgumentException();
        }
        else {
            return grid[row][col] != 0;
        }
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > n + 1 || col < 0 || col > n + 1) {
            throw new IllegalArgumentException();
        }
        else {
            return root(row, col) == root(0, 1);
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isFull(nP - 1, 1);
    }

    private int root(int row, int col) {
        return qf.find(row * nP + col);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation p = new Percolation(n);
        int[] order = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            order[i] = i;
        }
        StdRandom.shuffle(order);
        while (!p.percolates()) {
            int row = order[p.numberOfOpenSites()] / n + 1;
            int col = order[p.numberOfOpenSites()] % n + 1;
            p.open(row, col);
        }
        StdOut.print(p.numberOfOpenSites());
    }
}

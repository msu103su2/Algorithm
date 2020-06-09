/* *****************************************************************************
 *  Name: Shan
 *  Date: 06/08/2020
 *  Description: Hw1
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {

    private int[][] grid;
    private int openSites;
    private final int n;
    private final int nP;
    private int[][] sz;
    private final int[] order;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;
        this.nP = n + 2;
        openSites = 0;
        order = new int[n * n];
        grid = new int[nP][nP];
        sz = new int[nP][nP];
        for (int i = 0; i < nP; i++)
            for (int j = 0; j < nP; j++) {
                grid[i][j] = 0;
                sz[i][j] = 0;
            }
        for (int j = 0; j < n; j++) {
            grid[0][j + 1] = 1;
            grid[n + 1][j + 1] = nP * (nP - 1) + 1;
        }
        for (int i = 0; i < n * n; i++) {
            order[i] = i;
        }
        StdRandom.shuffle(order);
    }

    // connect two sites
    private void connect(int row0, int col0, int row1, int col1) {
        int cache0 = root(row0, col0);
        int cache1 = root(row1, col1);
        int root0row = cache0 / nP;
        int root0col = cache0 % nP;
        int root1row = cache1 / nP;
        int root1col = cache1 % nP;
        if (sz[root1row][root1col]
                <= sz[root0row][root0col]) {
            grid[root0row][root0col] = cache1;
            sz[root1row][root1col] += sz[root0row][root0col];
        }
        else {
            grid[root1row][root1col] = cache0;
            sz[root0row][root0col] += sz[root1row][root1col];
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || row > n + 1 || col < 0 || col > n + 1) {
            throw new IllegalArgumentException();
        }
        else {
            if (!isOpen(row, col)) {
                grid[row][col] = row * nP + col;
                sz[row][col] = 1;
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
        int row, col;
        while (!percolates()) {
            row = order[openSites] / n + 1;
            col = order[openSites] % n + 1;
            open(row, col);
        }
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isFull(nP - 1, 1);
    }

    private int root(int row, int col) {
        int r = row;
        int c = col;
        int temp;
        while (grid[r][c] != r * nP + c) {
            temp = grid[r][c] / nP;
            c = grid[r][c] % nP;
            r = temp;
        }
        return grid[r][c];
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation p = new Percolation(n);
        StdOut.print(p.numberOfOpenSites());
    }
}

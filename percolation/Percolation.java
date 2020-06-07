/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Percolation {

    private int[][] grid;
    private int OpenSites;
    private int n;
    private int N;
    private int[][] sz;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.n = n;
        this.N = n + 2;
        OpenSites = 0;
        grid = new int[N][N];
        sz = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                grid[i][j] = 0;
                sz[i][j] = 0;
            }
        for (int j = 0; j < n; j++) {
            grid[0][j + 1] = 1;
            grid[n + 1][j + 1] = N * (N - 1) + 1;
        }
    }

    //connect two sites
    private void connect(int row0, int col0, int row1, int col1) {
        int cache0 = root(row0, col0);
        int cache1 = root(row1, col1);
        if (sz[cache1 / N][cache1 % N]
                >= sz[cache0 / N][cache0 % N]) {
            grid[row0][col0] = cache1;
            sz[cache1 / N][cache1 % N] += sz[cache0 / N][cache0 % N];
        }
        else {
            grid[row1][col1] = cache0;
            sz[cache0 / N][cache0 % N] += sz[cache1 / N][cache1 % N];
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            grid[row][col] = row * N + col;
            sz[row][col] = 1;
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

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row][col] != 0;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return root(row, col) == root(0, 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return OpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isFull(N - 1, 1);
    }

    private int root(int row, int col) {
        while (grid[row][col] != row * N + col) {
            row = grid[row][col] / N;
            col = grid[row][col] % N;
        }
        return grid[row][col];
    }


    // test client (optional)
    public static void main(String[] args)
}

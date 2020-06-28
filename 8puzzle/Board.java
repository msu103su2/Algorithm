/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {

    private final int[][] tiles;
    private final int n;
    private final int ham;
    private final int man;
    private final boolean computted;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        ham = hamming();
        man = manhattan();
        computted = true;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d  ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }


    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        if (computted) return ham;
        else {
            int count = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (tiles[i][j] != (i * n + j + 1) % (n * n)) count++;
                }
            }
            return count;
        }
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (computted) return man;
        else {
            int count = 0;
            int goali;
            int goalj;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (tiles[i][j] == 0) {
                        goali = n - 1;
                        goalj = n - 1;
                    }
                    else {
                        goali = (tiles[i][j] - 1) / n;
                        goalj = (tiles[i][j] - 1) % n;
                    }
                    if (i > goali) count += i - goali;
                    if (i < goali) count += -(i - goali);
                    if (j > goalj) count += j - goalj;
                    if (j < goalj) count += -(j - goalj);
                }
            }
            return count;
        }
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.n == that.n) && Arrays.deepEquals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> nbs = new Queue<>();
        int[][] cpTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cpTiles[i][j] = tiles[i][j];
            }
        }
        int zeroI = 0;
        int zeroJ = 0;
        outer:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cpTiles[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                    break outer;
                }
            }
        }

        int count = 0;

        try {
            cpTiles[zeroI][zeroJ] = cpTiles[zeroI - 1][zeroJ];
            cpTiles[zeroI - 1][zeroJ] = 0;
            nbs.enqueue(new Board(cpTiles));
            cpTiles[zeroI - 1][zeroJ] = cpTiles[zeroI][zeroJ];
            cpTiles[zeroI][zeroJ] = 0;
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            cpTiles[zeroI][zeroJ] = cpTiles[zeroI][zeroJ - 1];
            cpTiles[zeroI][zeroJ - 1] = 0;
            nbs.enqueue(new Board(cpTiles));
            cpTiles[zeroI][zeroJ - 1] = cpTiles[zeroI][zeroJ];
            cpTiles[zeroI][zeroJ] = 0;
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            cpTiles[zeroI][zeroJ] = cpTiles[zeroI + 1][zeroJ];
            cpTiles[zeroI + 1][zeroJ] = 0;
            nbs.enqueue(new Board(cpTiles));
            cpTiles[zeroI + 1][zeroJ] = cpTiles[zeroI][zeroJ];
            cpTiles[zeroI][zeroJ] = 0;
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        try {
            cpTiles[zeroI][zeroJ] = cpTiles[zeroI][zeroJ + 1];
            cpTiles[zeroI][zeroJ + 1] = 0;
            nbs.enqueue(new Board(cpTiles));
            cpTiles[zeroI][zeroJ + 1] = cpTiles[zeroI][zeroJ];
            cpTiles[zeroI][zeroJ] = 0;
        }
        catch (ArrayIndexOutOfBoundsException e) {
        }

        return nbs;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] ntiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ntiles[i][j] = tiles[i][j];
            }
        }
        int ai = StdRandom.uniform(n);
        int aj = StdRandom.uniform(n);
        int bi = StdRandom.uniform(n);
        int bj = StdRandom.uniform(n);
        while ((ai == bi && aj == bj) || tiles[ai][aj] == 0 || tiles[bi][bj] == 0) {
            bi = StdRandom.uniform(n);
            bj = StdRandom.uniform(n);
        }
        int cache = ntiles[ai][aj];
        ntiles[ai][aj] = ntiles[bi][bj];
        ntiles[bi][bj] = cache;
        return new Board(ntiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        StdOut.print(args[0]);
    }

}

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;

import static java.lang.Math.abs;

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
                    if (tiles[i][j] != (i * n + j + 1) % n * n) count++;
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
                    goali = (tiles[i][j] - 1) / n;
                    goalj = (tiles[i][j] - 1) % n;
                    count += abs(i - goali);
                    count += abs(j - goalj);
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
        return new listofBoards(tiles);
    }

    private class listofBoards implements Iterable<Board> {

        private final Board[] nbs;
        private int index = 0;

        public listofBoards(int[][] intiles) {
            int n = tiles[0].length;
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = intiles[i][j];
                }
            }
            Board[] cache = new Board[4];
            int zeroi = 0;
            int zeroj = 0;
            outer:
            for (; zeroi < n; zeroi++) {
                for (; zeroj < n; zeroj++) {
                    if (tiles[zeroi][zeroj] == 0) break outer;
                }
            }

            int count = 0;

            try {
                int test = tiles[zeroi - 1][zeroj];
                tiles[zeroi][zeroj] = tiles[zeroi - 1][zeroj];
                tiles[zeroi - 1][zeroj] = 0;
                cache[count++] = new Board(tiles);
                tiles[zeroi - 1][zeroj] = tiles[zeroi][zeroj];
                tiles[zeroi][zeroj] = 0;
            }
            catch (ArrayIndexOutOfBoundsException e) {
            }

            try {
                int test = tiles[zeroi][zeroj - 1];
                tiles[zeroi][zeroj] = tiles[zeroi][zeroj - 1];
                tiles[zeroi][zeroj - 1] = 0;
                cache[count++] = new Board(tiles);
                tiles[zeroi][zeroj - 1] = tiles[zeroi][zeroj];
                tiles[zeroi][zeroj] = 0;
            }
            catch (ArrayIndexOutOfBoundsException e) {
            }

            try {
                int test = tiles[zeroi + 1][zeroj];
                tiles[zeroi][zeroj] = tiles[zeroi + 1][zeroj];
                tiles[zeroi + 1][zeroj] = 0;
                cache[count++] = new Board(tiles);
                tiles[zeroi + 1][zeroj] = tiles[zeroi][zeroj];
                tiles[zeroi][zeroj] = 0;
            }
            catch (ArrayIndexOutOfBoundsException e) {
            }

            try {
                int test = tiles[zeroi][zeroj + 1];
                tiles[zeroi][zeroj] = tiles[zeroi][zeroj + 1];
                tiles[zeroi][zeroj + 1] = 0;
                cache[count++] = new Board(tiles);
                tiles[zeroi][zeroj + 1] = tiles[zeroi][zeroj];
                tiles[zeroi][zeroj] = 0;
            }
            catch (ArrayIndexOutOfBoundsException e) {
            }

            nbs = new Board[count];
            for (int i = 0; i < count; i++) nbs[i] = cache[i];
        }

        public Iterator<Board> iterator() {
            return new ListIterator();
        }

        private class ListIterator implements Iterator<Board> {
            public boolean hasNext() {
                return index < nbs.length;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public Board next() {
                if (!hasNext()) throw new java.util.NoSuchElementException();
                else return nbs[index++];
            }
        }
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
        int cache = ntiles[ai][aj];
        ntiles[ai][aj] = ntiles[bi][bj];
        ntiles[bi][bj] = cache;
        return new Board(ntiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
    }

}

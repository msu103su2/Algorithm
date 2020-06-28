/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final Stack<Board> seq = new Stack<>();
    private int sMoves;
    private final Board initial;
    private final boolean sSolvable;
    private final boolean computted;

    private class Node implements Comparable<Node> {
        public final Node prev;
        private final int hamming, manhattan;
        private final int moves;
        private final Board bd;

        public Node(Board in, Node prev) {
            bd = in;
            hamming = in.hamming();
            manhattan = in.manhattan();
            this.prev = prev;
            if (prev == null) moves = 0;
            else moves = prev.getMoves() + 1;
        }

        public int getMoves() {
            return moves;
        }

        public int compareTo(Node that) {
            if (priorityF() < that.priorityF()) return -1;
            else if (priorityF() == that.priorityF()) return 0;
            else return 1;
        }

        public int priorityF() {
            return manhattan + moves;
        }

        public boolean isGoal() {
            return bd.isGoal();
        }

        public Board getBd() {
            return bd;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        else {
            this.initial = initial;
            sSolvable = isSolvable();
            computted = true;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (computted) return sSolvable;
        else {
            MinPQ<Node> itCandidates = new MinPQ<>();
            Queue<Node> itSeq = new Queue<>();
            MinPQ<Node> twinCandidates = new MinPQ<>();
            Queue<Node> twinSeq = new Queue<>();

            itCandidates.insert(new Node(initial, null));
            twinCandidates.insert(new Node(initial.twin(), null));

            while (!itCandidates.min().isGoal() && !twinCandidates.min().isGoal()) {
                Node temp = itCandidates.delMin();
                itSeq.enqueue(temp);
                for (Board nb : temp.getBd().neighbors()) {
                    if (temp.prev == null) itCandidates.insert(new Node(nb, temp));
                    else if (!(nb.equals(temp.prev.getBd())))
                        itCandidates.insert(new Node(nb, temp));
                }

                temp = twinCandidates.delMin();
                twinSeq.enqueue(temp);
                for (Board nb : temp.getBd().neighbors()) {
                    if (temp.prev == null) twinCandidates.insert(new Node(nb, temp));
                    else if (!(nb.equals(temp.getBd()))) twinCandidates.insert(new Node(nb, temp));
                }
            }

            if (itCandidates.min().isGoal()) {
                Node temp = itCandidates.delMin();
                sMoves = temp.getMoves();
                itSeq.enqueue(temp);
                while (temp.prev != null) {
                    seq.push(temp.getBd());
                    temp = temp.prev;
                }
                seq.push(temp.getBd());
                return true;
            }
            else return false;
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return sMoves;
        else return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) return seq;
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}

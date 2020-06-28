/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Queue<Node> seq = new Queue<>();
    private int sMoves;
    private final Board initial;
    private final boolean sSolvable;
    private final boolean computted;

    private class Node implements Comparable<Node> {
        private final int hamming, manhattan;
        private final int moves;
        private final Board bd;
        private final Node prev;

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
            return hamming + moves;
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
        this.initial = initial;
        sSolvable = isSolvable();
        computted = true;
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
                    if (!(nb.equals(temp.getBd()))) itCandidates.insert(new Node(nb, temp));
                }

                temp = twinCandidates.delMin();
                twinSeq.enqueue(temp);
                for (Board nb : temp.getBd().neighbors()) {
                    if (!(nb.equals(temp.getBd()))) twinCandidates.insert(new Node(nb, temp));
                }
            }

            if (itCandidates.min().isGoal()) {
                sMoves = itCandidates.min().getMoves();
                itSeq.enqueue(itCandidates.delMin());
                seq = itSeq;
                return true;
            }
            else return false;
        }
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return sMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        Queue<Board> answer = new Queue<>();
        answer.enqueue(seq.dequeue().getBd());
        return answer;
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

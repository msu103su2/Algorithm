/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (!checkpoints(points)) throw new IllegalArgumentException();
        else {
            Point[] cpy = points.clone();
            int n = points.length;
            LineSegment[] temp = new LineSegment[n * (n - 1)];
            int count = 0;
            int colc = 2;
            for (Point origin : points) {
                Arrays.sort(cpy, origin.slopeOrder());
                for (int i = 1; i < n - 1; ) {
                    colc = 2;
                    for (int j = i + 1; j < n; j++) {
                        if (origin.slopeTo(cpy[i]) == origin.slopeTo(cpy[j])) {
                            colc++;
                        }
                        else break;
                    }
                    if (colc > 3) {
                        if (origin.compareTo(cpy[i]) < 0)
                            temp[count++] = new LineSegment(origin, cpy[i + colc - 2]);
                        i = i + colc - 1;
                    }
                    else i++;
                }
                cpy = points.clone();
            }
            lineSegments = new LineSegment[count];
            for (int i = 0; i < count; i++) {
                lineSegments[i] = temp[i];
            }
        }
    } // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return lineSegments.length;
    }      // the number of line segments

    public LineSegment[] segments() {
        return lineSegments.clone();
    }               // the line segments

    private boolean checkpoints(Point[] points) {
        if (points == null) return false;
        else {
            for (Point p : points) {
                if (p == null) return false;
            }
            Arrays.sort(points);
            int n = points.length;
            for (int i = 0; i < n - 1; i++) {
                if (points[i].compareTo(points[i + 1]) == 0) return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

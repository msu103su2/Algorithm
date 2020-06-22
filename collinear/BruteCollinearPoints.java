/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (!checkpoints(points)) throw new IllegalArgumentException();
        else {
            int n = points.length;
            LineSegment[] temp = new LineSegment[n * (n - 1)];
            int count = 0;
            for (int i1 = 0; i1 < n; i1++) {
                for (int i2 = i1 + 1; i2 < n; i2++) {
                    double k1 = points[i1].slopeTo(points[i2]);
                    outer:
                    for (int i3 = i2 + 1; i3 < n; i3++) {
                        double k2 = points[i2].slopeTo(points[i3]);
                        for (int i4 = i3 + 1; i4 < n; i4++) {
                            double k3 = points[i3].slopeTo(points[i4]);
                            if (k1 == k2 && k2 == k3) {
                                temp[count++] = new LineSegment(points[i1], points[i4]);
                                break outer;
                            }
                        }
                    }
                }
            }
            lineSegments = new LineSegment[count];
            for (int i = 0; i < count; i++) {
                lineSegments[i] = temp[i];
            }
        }
    }   // finds all line segments containing 4 points

    public int numberOfSegments() {
        return lineSegments.length;
    }      // the number of line segments

    public LineSegment[] segments() {
        return lineSegments.clone();
    }          // the line segments

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
}

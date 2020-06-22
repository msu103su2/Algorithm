/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

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
            Point[] starts = new Point[n];
            int startsCounter = 0;
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
                        Point start = cpy[i], end = cpy[i + colc - 2];
                        boolean addNewStart = false;
                        if (origin.compareTo(cpy[i]) < 0) start = origin;
                        else if (origin.compareTo(cpy[i + colc - 2]) > 0) end = origin;
                        if (startsCounter == 0) {
                            temp[count++] = new LineSegment(start, end);
                            addNewStart = true;
                        }
                        else if (start.compareTo(starts[startsCounter - 1]) > 0) {
                            temp[count++] = new LineSegment(start, end);
                            addNewStart = true;
                        }
                        if (addNewStart) starts[startsCounter++] = start;
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
        return lineSegments;
    }               // the line segments

    private boolean checkpoints(Point[] points) {
        if (points.length == 0) return false;
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

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private int numOfPoints;
    private SET<Point2D> points;

    public PointSET() {
        numOfPoints = 0;
        points = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return numOfPoints == 0;
    }                     // is the set empty?

    public int size() {
        return numOfPoints;
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            points.add(p);
            numOfPoints++;
        }
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }            // does the set contain point p?

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.01);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.line(0, 0, 1, 0);
        StdDraw.line(1, 0, 1, 1);
        StdDraw.line(1, 1, 0, 1);
        StdDraw.line(0, 1, 0, 0);
        for (Point2D p : points) {
            p.draw();
        }
        StdDraw.show();
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> inRect = new SET<>();
        for (Point2D p : points) {
            if (rect.contains(p)) inRect.add(p);
        }
        return inRect;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return p;
        double minDst = Double.POSITIVE_INFINITY;
        if (isEmpty()) return null;
        Point2D minDstPt = p;
        for (Point2D that : points) {
            if (!p.equals(that)) {
                double newDst = p.distanceTo(that);
                if (newDst < minDst) {
                    minDstPt = that;
                    minDst = newDst;
                }
            }
        }
        return minDstPt;
    }// a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        PointSET pointSET = new PointSET();

        In in = new In(args[0]);
        while (in.hasNextLine()) {
            if (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                StdOut.print(String.format("%f\t%f\n", x, y));
                pointSET.insert(new Point2D(x, y));
            }
            else in.readLine();
        }

        pointSET.draw();
        /*
        pointSET.nearest(new Point2D(0.5, 0.5));
        StdOut.print(pointSET.numOfPoints);
        RectHV rectHV = new RectHV(0.2, 0.2, 0.7, 0.7);

        StdDraw.setPenColor(Color.RED);
        for (Point2D p : pointSET.range(rectHV)) {
            p.draw();
        }
        StdDraw.setPenRadius();
        rectHV.draw();
        StdDraw.show();*/
    }                 // unit testing of the methods (optional)
}

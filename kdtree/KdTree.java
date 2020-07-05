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

import java.awt.Color;

public class KdTree {

    private int numOfPoints;
    private Node root;
    private final boolean rootCmpX = true;

    private class Node {
        private Point2D p;
        private Node left;
        private Node right;

        public Node(Point2D p) {
            this.p = p;
        }
    }

    public KdTree() {
        numOfPoints = 0;
    }                              // construct an empty set of points

    public boolean isEmpty() {
        return numOfPoints == 0;
    }                     // is the set empty?

    public int size() {
        return numOfPoints;
    }                        // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            root = put(root, p, true);
            numOfPoints++;
        }
    }              // add the point to the set (if it is not already in the set)

    private Node put(Node x, Point2D p, boolean cmpX) {
        if (x == null) return new Node(p);
        double cmp;
        if (cmpX)
            cmp = p.x() - x.p.x();
        else
            cmp = p.y() - x.p.y();

        if (cmp <= 0)
            x.left = put(x.left, p, !cmpX);
        else
            x.right = put(x.right, p, !cmpX);
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        boolean cmpX = rootCmpX;
        while (x != null) {
            if (x.p.equals(p)) return true;
            double cmp;
            if (cmpX)
                cmp = p.x() - x.p.x();
            else
                cmp = p.y() - x.p.y();

            if (cmp <= 0) x = x.left;
            else x = x.right;
            cmpX = !cmpX;
        }
        return false;
    }           // does the set contain point p?

    public void draw() {
        drawPoint(root, rootCmpX, 1, 1);
        StdDraw.show();
    }                        // draw all points to standard draw

    private void drawPoint(Node x, boolean cmpX, double px, double py) {
        if (x != null) {
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.setPenRadius(0.01);
            x.p.draw();
            StdDraw.setPenRadius();
            if (cmpX) {
                StdDraw.setPenColor(Color.RED);
                if (x.p.y() < py)
                    StdDraw.line(x.p.x(), 0, x.p.x(), py);
                else
                    StdDraw.line(x.p.x(), py, x.p.x(), 1);
            }
            else {
                StdDraw.setPenColor(Color.BLUE);
                if (x.p.x() < px)
                    StdDraw.line(0, x.p.y(), px, x.p.y());
                else
                    StdDraw.line(px, x.p.y(), 1, x.p.y());
            }
            drawPoint(x.left, !cmpX, x.p.x(), x.p.y());
            drawPoint(x.right, !cmpX, x.p.x(), x.p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> inRect = new SET<>();
        Node x = root;
        range(rect, inRect, x, rootCmpX);
        return inRect;
    }            // all points that are inside the rectangle (or on the boundary)

    private void range(RectHV rect, SET<Point2D> inRect, Node x, boolean cmpX) {
        if (x == null) return;
        if (intersect(rect, x.p, cmpX)) {
            if (rect.contains(x.p)) {
                inRect.add(x.p);
            }
            range(rect, inRect, x.left, !cmpX);
            range(rect, inRect, x.right, !cmpX);
        }
        else if (toRT(rect, x.p, cmpX)) range(rect, inRect, x.right, !cmpX);
        else range(rect, inRect, x.left, !cmpX);
    }

    private boolean intersect(RectHV rect, Point2D p, boolean cmpX) {
        if (cmpX) return p.x() <= rect.xmax() && p.x() >= rect.xmin();
        else return p.y() <= rect.ymax() && p.y() >= rect.ymin();
    }

    private boolean toRT(RectHV rect, Point2D p, boolean cmpX) {
        if (cmpX) return p.x() < rect.xmin();
        else return p.y() < rect.ymin();
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return p;
        if (isEmpty()) return null;
        Node min = nearestPoint(root, p, rootCmpX);
        return min.p;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private Node nearestPoint(Node x, Point2D p, boolean cmpX) {
        if (x == null) return new Node(new Point2D(10, 10));
        else {
            double cmp;
            if (cmpX) cmp = p.x() - x.p.x();
            else cmp = p.y() - x.p.y();

            if (cmp <= 0) {
                Node minLD = nearestPoint(x.left, p, !cmpX);
                double minLDDst = minLD.p.distanceTo(p);
                if (minLDDst <= -cmp) return minLD;
                else {
                    Node minRT = nearestPoint(x.right, p, !cmpX);
                    double minRTDst = minRT.p.distanceTo(p);
                    double xDst = x.p.distanceTo(p);
                    if (minLDDst <= xDst) {
                        if (minLDDst < minRTDst) return minLD;
                        else return minRT;
                    }
                    else {
                        if (xDst < minRTDst) return x;
                        else return minRT;
                    }
                }
            }
            else {
                Node minRT = nearestPoint(x.right, p, !cmpX);
                double minRTDst = minRT.p.distanceTo(p);
                if (minRTDst <= cmp) return minRT;
                else {
                    Node minLD = nearestPoint(x.left, p, !cmpX);
                    double minLDDst = minLD.p.distanceTo(p);
                    double xDst = x.p.distanceTo(p);
                    if (minLDDst <= xDst) {
                        if (minLDDst < minRTDst) return minLD;
                        else return minRT;
                    }
                    else {
                        if (xDst < minRTDst) return x;
                        else return minRT;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        In in = new In(args[0]);
        while (in.hasNextLine()) {
            if (!in.isEmpty()) {
                double x = in.readDouble();
                double y = in.readDouble();
                kdTree.insert(new Point2D(x, y));
            }
            else in.readLine();
        }

        kdTree.nearest(new Point2D(0.5, 0.5));
        StdOut.print(kdTree.numOfPoints);
        RectHV rectHV = new RectHV(0.2, 0.2, 0.7, 0.7);

        StdDraw.setPenColor(Color.RED);
        for (Point2D p : kdTree.range(rectHV)) {
            p.draw();
        }
        StdDraw.setPenRadius();
        rectHV.draw();
        StdDraw.show();
    }                 // unit testing of the methods (optional)
}

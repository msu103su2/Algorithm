/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;


public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int sz;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        first = new Node();
        last = new Node();
        sz = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return sz == 0;
    }

    // return the number of items on the deque
    public int size() {
        return sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        else {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.prev = null;
            if (size() == 0) {
                first.next = null;
                last = first;
            }
            else
                first.next = oldfirst;
            oldfirst.prev = first;
            sz++;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        else {
            Node oldlast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            if (size() == 0) {
                last.prev = null;
                first = last;
            }
            else
                last.prev = oldlast;
            oldlast.next = last;
            sz++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        else if (size() == 1) {
            Item item = first.item;
            first = new Node();
            last = new Node();
            sz--;
            return item;
        }
        else {
            Item item = first.item;
            first = first.next;
            first.prev = null;
            if (size() == 2) last.prev = null;
            sz--;
            return item;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size() == 0) throw new java.util.NoSuchElementException();
        else if (size() == 1) {
            Item item = last.item;
            first = new Node();
            last = new Node();
            sz--;
            return item;
        }
        else {
            Item item = last.item;
            last = last.prev;
            last.next = null;
            if (size() == 2) first.next = null;
            sz--;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            else {
                Item item = current.item;
                current = current.next;
                return item;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> nq = new Deque<>();
        for (int i = 0; i < 5; i++) {
            nq.addLast(i);
            nq.addFirst(i);
        }
        for (Integer s : nq)
            StdOut.println(s);
        int sz = nq.size() / 2;
        for (int i = 0; i < sz; i++) {
            StdOut.print("Item removed from last：" + nq.removeLast() + '\n');
            StdOut.print("Item removed from first：" + nq.removeFirst() + '\n');
            StdOut.print("Empty ：" + nq.isEmpty() + '\n');
            StdOut.print("Size ：" + nq.size() + '\n');
        }
    }

}


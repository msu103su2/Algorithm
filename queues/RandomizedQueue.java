/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        else {
            if (N == s.length) resize(2 * s.length);
            s[N++] = item;
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = s[i];
        s = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        else {
            int index = StdRandom.uniform(N);
            Item item = s[index];
            s[index] = s[--N];
            s[N] = null;
            if (N > 0 && N == s.length / 4) resize(s.length / 2);
            return item;
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        else {
            int index = StdRandom.uniform(N);
            Item item = s[index];
            return item;
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int i = N;
        private int[] order = new int[N];

        public ListIterator() {
            for (int j = 0; j < N; j++)
                order[j] = j;
            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            else return s[order[--i]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> nrq = new RandomizedQueue<>();
        for (int i = 0; i < 9; i++)
            nrq.enqueue(i);
        StdOut.print(nrq.sample());
        StdOut.print("Empty : " + nrq.isEmpty() + '\n');
        while (!nrq.isEmpty()) {
            StdOut.print("Poped item : " + nrq.dequeue() + '\n');
            for (Integer i : nrq) StdOut.println(i);
            StdOut.print('\n');
        }
    }
}

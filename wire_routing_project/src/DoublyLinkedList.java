/**
 * TODO: Add a sort() method, based on the Quicksort Algorithm, to the
 * DoublyLinkedList implementation.
 */

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.function.BiPredicate;

/**
 * If you want to call yourself a List, then implement this interface:
 */

interface List<T> extends Iterable<T> {
    void add(T x);
    T remove(int i);
    boolean remove(T x);
    T get(int i);
    boolean contains(T x);
    int size();
    default boolean isEmpty() {
        return size() == 0;
    }
}

public class DoublyLinkedList<T> implements List<T> {

    /**
     * Node is a pair containing a data field and pointers to
     * the previous and next nodes in the list.
     */

    class Node {
        T data;
        Node next, prev;

        Node(T data) {
            this(data, null, null);
        }

        Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    final Node head; // always points to the headnode for this list
    int n;           // the number of nodes in this list, initially 0
    int modCount;    // the number of modificationss made to this list

    /**
     * Creates the empty list.
     */

    public DoublyLinkedList() {
        head = new Node(null);
        head.prev = head.next = head;
    }


    public DoublyLinkedList(List<T> lst) {
        head = new Node(null);
        head.prev = head.next = head;

        for(T item : lst)
            this.add(item);
    }

    /**
     * TODO
     *
     * Rearranges the elements of this list so they are in order according
     * to the supplied predicate by using the Quicksort Algorithm. Your
     * implementation must run in O(n log n) time on the average.
     */

    public void sort(BiPredicate<T, T> lessThan) {
        // Call quicksortHelper to get the job done.

        //if(n > 1)
        sortHelper(lessThan, head.next, head.prev);
    }

    private void sortHelper(BiPredicate<T,T> lessThan, Node low, Node high) {

        if(low != high && low.prev != high) {
           Node position = partition(lessThan, low,high);
           sortHelper(lessThan, low, position.prev);
           sortHelper(lessThan, position.next, high);
        }

    }

    /**
     * TODO
     *
     * Be sure to follow the Partition Algorithm described in lecture.
     * (See the notes from lec9a for details.)
     *
     * Partitions the elements in the list about the pivot (which is the
     * data in the low node), and then returns the node containing the
     * pivot. Note that you should move the data around rather than
     * trying to adjust the links in the nodes.
     */

    public Node partition(BiPredicate<T, T> lessThan, Node low, Node high) {
        T pivot = low.data;
        Node free = low;
        //low = low.next;

        outter:
            while(true) {
                while (lessThan.test(pivot, high.data)) {
                    high = high.prev;
                    if (high == low.prev) break outter;
                }
                free.data = high.data;
                free = high;
                high = high.prev;
                if (high == low.prev) break;


                while (lessThan.test(low.data, pivot)) {
                    low = low.next;
                    if (low == high.next) break outter;
                }
                free.data = low.data;
                free = low;
                low = low.next;
                if (low == high.next) break;
        }
        free.data = pivot;
        return free;
    }

    /**
     * Inserts the value x at the end of this list.
     */

    public void add(T x) {
        modCount++;
        n++;
        Node p = new Node(x, head.prev, head);
        head.prev = head.prev.next = p;
    }

    /**
     * Removes the element at index i from this list.
     * @return the data in the removed node.
     * @throws IndexOutOfBoundsException iff i is out of range for this list.
     */

    public T remove(int i) {
        if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException();
        Node p = head.next;
        while (i > 0) {
            p = p.next;
            i--;
        }
        return removeHelper(p);
    }

    /**
     * Helper method to remove the node pointed to by p.
     */

    private T removeHelper(Node p) {
        assert p != head;
        n--;
        modCount++;
        p = p.prev;
        T ans = p.next.data;
        p.next.next.prev = p;
        p.next = p.next.next;
        return ans;
    }


    public boolean remove(T x) {
        //assert x != null;

        Node p = head.next;

        // p.data == null as break condition bc I still add a block with null data
        while(p != null && p.data != null) {
            if(p.data.equals(x)){
                //remove
                p.next.prev = p.prev;
                p.prev.next = p.next;

                n--;
                modCount++;
                // see if this fixes ConcurrentModEx
                //iterator().next();

                return true;
            }
            p = p.next;
        }
        return false;
    }


    /**
     * Returns the i-th element from this list, where i is a zero-based
     * index.
     * @throws IndexOutOfBoundsException iff i is out of range for this list.
     */

    public T get(int i) {
        if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException();
        Node p = head.next;
        while (i > 0) {
            p = p.next;
            i--;
        }
        return p.data;
    }


    /**
     * add's to the second spot in the list because of the implementation of Path that
     * puts the wire.from coordinate at the first position in the list
     */
    public void addFront(T x) {
        Node next = head.next;

        n++;
        modCount++;

        Node p = new Node(x, next, next.next);
        next.next.prev = next.next = p;
    }



    /**
     * Returns true iff the value x appears somewhere in this list.
     */

    public boolean contains(T x) {
        Node p = head.next;
        while (p != head) {
            if (p.data.equals(x))
                return true;
            p = p.next;
        }
        return false;
    }

    /**
     * Returns the number of elements in this list.
     */

    public int size() {
        return n;
    }

    /**
     * Returns an iterator for this list.
     */

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node curr = head.next;
            int expectedModCount = modCount;
            boolean canRemove = false;

            public boolean hasNext() {
                return curr != head;
            }

            public T next() {
                if (modCount != expectedModCount)
                    throw new ConcurrentModificationException();
                T ans = curr.data;
                curr = curr.next;
                canRemove = true;
                return ans;
            }

            public void remove() {
                if (!canRemove)
                    throw new IllegalStateException();
                canRemove = false;
                removeHelper(curr.prev);
                expectedModCount++;
            }
        };
    }

    /**
     * Returns a string representing this list (resembling a Racket list).
     */

    public String toString() {
        if (isEmpty())
            return "()";
        Iterator<T> it = iterator();
        StringBuilder ans = new StringBuilder("(").append(it.next());
        while (it.hasNext())
            ans.append(" ").append(it.next());
        return ans.append(")").toString();
    }

    /**
     * TODO: Write a unit test.
     *
     * Here are just a few simple tests to get you started.
     */

    public static void main(String... args) {
        DoublyLinkedList<Integer> xs = new DoublyLinkedList<>();
        int[] a = new int[] { 4, 3, 0, 6, 5, 7, 2, 8, 1 };
        for (int x : a)
            xs.add(x);
        // Sort xs in the natural order:
        xs.sort((x, y) -> x < y);
        for (int i = 0; i < xs.size(); i++)
            assert i == xs.get(i);
        // Sort xs in the reverse of the natural order:
        xs.sort((x, y) -> x > y);
        for (int i = 0; i < xs.size(); i++)
            assert xs.size() - 1 - i == xs.get(i);
        System.out.println("All tests passed...");
    }

}



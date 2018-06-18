/**
 * There's nothing for you to do here.
 * 
 * Hallmarks of a DoublyLinkedList:
 * - headnode (also called a dummy node)
 * - backward pointers to the previous node in the list
 * - circularity: last node points forward to the headnode and the
 *                headnode points backward to the last node
 */

import java.util.Iterator;
import java.util.ConcurrentModificationException;

/**
 * If you want to call yourself a List, then implement this interface:
 */

interface List<T> extends Iterable<T> {
  void add(T x); 
  T remove(int i);
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

}


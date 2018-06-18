import java.util.Comparator;

/**
 * lab11: starter code
 *
 * A PriorityQueue is a collection of keys, ordered according to a
 * specified comparator. The keys will be stored in a heap that is
 * represented as an array. The main operations on a PriorityQueue are
 * offer(), peek(), and poll().
 *
 *
 * #1 and #2: Implement the heap utilities siftUp() and siftDown()
 * that will be used to restore the ordering property on a heap after
 * inserting or deleting, respectively, keys from the heap.
 *
 * #3 and #4: Implement the offer() and poll() methods, as described
 * in lecture.
 *
 * If you are unable to complete this exercise during lab, continue to
 * work on it on your own because you will need an implementation of
 * PriorityQueue for when you implement Huffman's algorithm next week.
 */

public class PriorityQueue<E> {
  
  E[] heap = newTable(10);
  Comparator<E> comp;
  int n;
  HashMap<E, Integer> indexMap = new HashMap<>(1000);

  /**
   * Creates a priority queue that orders its elements according to
   * the specified comparator.
   */

  public PriorityQueue(Comparator<E> comp) {
    this.comp = comp;
  }

  /**
   *
   * Inserts the specified element into this priority queue. 
   *
   * Implementation requirements:
   *
   * (1) If the heap array is full, then create a new array that is
   * twice the size and copy in the keys.
   *
   * (2) Add the new element as a new rightmost leaf on the last level
   * of the heap and then run the siftUp() utility to adjust the
   * ordering of the keys within the heap.
   */

  public void offer(E e) {
    // she's full
    if(n == heap.length)
      heap = newTable(2 * n, heap);

    // put key and index in hashmap
    indexMap.put(e,n);

    heap[n] = e;
    siftUp(n++);

  }

  /**
   * Retrieves, but does not remove, the head of this queue, or returns
   * null if this queue is empty.
   */

  public E peek() {
    if (isEmpty())
      return null;
    return heap[0];
  }

  /**
   *
   * Retrieves and removes the head of this queue, or returns null if this
   * queue is empty.
   *
   * Implementation requirements:
   *
   * (1) To delete the root, cut off the rightmost leaf on the last
   * level of the heap and move its key into root position.
   *
   * (2) Adjust the heap ordering property by running the siftDown()
   * utility.
   */

  public E poll() {
    if(isEmpty())
      return null;

    E polled = heap[0]; //pre-dec because it's an index
    heap[0] = heap[n - 1]; //pre-dec because it's an index
    heap[--n] = null;
    siftDown(0);
    return polled;
  }

  /**
   * Removes the given element from the queue. Returns true iff the remove
   * was successful. This must run in O(log n) time.
   */
  public boolean remove(E e) {
    Integer index = indexMap.get(e);

    if(index != null && index >= 0 && index < n) {

      swap(index, --n);
      siftDown(index);
      return true;
    }

    return false;
  }

  /**
   * Returns the number of keys in this queue.
   */

  public int size() {
    return n;
  }
  
  /**
   * Returns true iff this queue is empty.
   */

  public boolean isEmpty() {
    return size() == 0;
  }

  /////////////////////////////////////
  // Utility methods below this line //
  /////////////////////////////////////

  /**
   *
   * Restores the ordering property at node p so that the first n
   * elements of array heap form a heap ordered by this queue's
   * comparator. Used by poll().
   *
   * Implementation requirements: Use O(1) additional space. In
   * particular, this means that you should not use recursion to
   * implement this method.
   */

  public void siftDown(int p) {
    int left, right, min, last = p;
    boolean sift = true;

    while(sift) {
      left = leftChild(last);
      if (left < n) {
        min = left;
        right = rightChild(last);
        if (right < n) {
          min = comp.compare(heap[left], heap[right]) > 0? right: left;
        }

        if(comp.compare(heap[last], heap[min]) > 0) {
          swap(last, min);
          last = min;
          sift = true;
        }
        else {
          sift = false;
        }

      } else {
        return;
      }
    }
  }


  /**
   * 
   * Restores the heap ordering property by sifting the key at
   * position q up into the heap. Used by offer().
   *
   * Implementation requirements: Use O(1) additional space. In
   * particular, this means that you should not use recursion to
   * implement this method.
   */

  public void siftUp(int q) {
    int p = parent(q);
    while(comp.compare(heap[q], heap[p]) < 0  && p >= 0) {
      swap(p, q);
      q = p;
      p = parent(q);
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("[");

    if(heap[0] != null)
      sb.append(heap[0]);
    else
      return sb.append("]").toString();

    for(int i = 1; i < size(); i++) {
      sb.append(", " + heap[i]);
      //sb.append(heap[i]);
    }

    return sb.append("]").toString();

  }

  /**                                                                           
   * Exchanges the elements at indices i and j in the heap.       
   */

  private void swap(int i, int j) {
    if (i != j) {
      E temp = heap[i];
      heap[i] = heap[j];

      // fixup map
      indexMap.put(heap[i], i);
      heap[j] = temp;

      // fixup map
      indexMap.put(heap[j], j);
    }
  }

  /**                                                                           
   * Returns a logical pointer to the left child of node p.                     
   */

  private static int leftChild(int p) {
    return 2 * p + 1;
  }

  /**                                                                           
   * Returns a logical pointer to the right child of node p.                    
   */

  private static int rightChild(int p) {
    return leftChild(p) + 1;
  }

  /**                                                                           
   * Returns a logical pointer to the parent of node p.                         
   */

  private static int parent(int p) {
    return (p - 1) / 2;
  }

  /**
   * Technical workaround for creating a generic array.
   */

  @SafeVarargs 
  private static <E> E[] newTable(int length, 
                                  E... table) {
    return java.util.Arrays.copyOf(table, length);
  }

}

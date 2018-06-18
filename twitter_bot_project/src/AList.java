/**
 * TODO: Implement the addFront() method.
 */

import java.util.Iterator;



/*              __
               / _)
        .-^^^-/ /
    __/       /
    <__.|_|-|_|
*/
public class AList<K, V> extends DoublyLinkedList<Assoc<K, V>> {

  /**
   * THis is function adds a new association to the front of the list after head.
   * It is a void method and doesn't return anything
   * @Param an association that will be added to the list
   */
  public void addFront(Assoc<K, V> a) {
    Node last = head.prev;
    Node newNode = new Node(a, head, head.next);
    head.next = newNode;
    newNode.next.prev = newNode;
    n++;
    modCount++;
  }
  
  /**
   * Returns the association with the given key in this map, if it
   * exists, and null otherwise. Self-adjusts by moving the returned
   * association to the front.
   */

  public Assoc<K, V> get(K key) {
    Assoc<K, V> a;
    Iterator<Assoc<K, V>> it = iterator();
    while (it.hasNext()) {
      a = it.next();
      if (key.equals(a.key)) {
        it.remove();
        addFront(a);
        return a;
      }
    }
    return null;
  }

}

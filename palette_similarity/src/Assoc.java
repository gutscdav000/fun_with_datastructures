/**
 * There's nothing for you to do here.
 */

public class Assoc<K, V> {
  K key;
  V value;

  public Assoc(K key, V value) {
    this.key = key;
    this.value = value;
  }


  // overloaded constructor to dereference Associations
  public Assoc(Assoc<K,V> k) {
    this.key = k.key;
    this.value = k.value;
  }

  public String toString() {
    return "(" + key + "," + value + ")";
  }
}

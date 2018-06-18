/**
 * There's nothing for you to do here, but note that
 * we've added the usual size() and isEmpty() methods
 * to the interface.
 */

public interface Map<K, V> {
  V put(K key, V value);
  default V remove(K key) {
    throw new UnsupportedOperationException();
  }
  V get(K key);
  int size();
  default boolean isEmpty() {
    return size() == 0;
  }
}

import java.security.AlgorithmConstraints;
import java.util.Iterator;

/**
 * To Do:
 *  (1) Thoroughly document this class and its fields/methods.  (done)
 *  (2) Implement the size() method defined in Map.             (done)
 *  (3) Implement the remove() method defined in Map.           (done)
 */

   /*_/|
   =0-0=
   \'I'|
   |<|,,\_
   |[>,,/,\
   |[|,\_,,)
   ((J(=_*/
public class HashMap<K, V> implements Map<K, V> {
  AList<K, V>[] table;
  int n = 0;

  /*
  Creates a new hashmap of size 3
   */
  public HashMap() {
    this(3);
  }


  /*
  creates a new hashmap of size N
  @param an integer for the size of the new map
   */
  public HashMap(int n) {
    table = newTable(n);
    for (int i = 0; i < table.length; i++)
      table[i] = new AList<>();
  }


  /*
  a function which creates a new key value association.
  if it's already in the table puts it in the table at the front and returns null
  @param key of type K that will be the key in the assoc
  @param value of type V that will be the value in the assoc
  @return the Value of type V for the association. if none  found null is returned
   */
  public V put(K key, V value) {
    int i = hash(key);
    Assoc<K, V> a = table[i].get(key);
    if (a == null) {
      table[i].addFront(new Assoc<K, V>(key, value));
      n++;
      return null;
    }
    V ans = a.value;
    a.value = value;
    return ans;
  }


  /*
  a funciton which gets the association from the table if it is found
  @param a key of type K used to find the association
  @ returns a value from the association if one is found or null will be returned
   */
  public V get(K key) {
    int i = hash(key);
    Assoc<K, V> a = table[i].get(key);
    if (a == null)
      return null;
    return a.value;
  }

  /**
   * To Do
   * A function which will return the size of the hashmap
   * @returns an integer corresponding with the size of the hashmap
   */

  public int size() {
    return n;
  }


  /*
   * a function which removes an association from the tree. This function modifies the list
   * @param a key of type K that is currently in the hashmap
   * @returns the value from the association of type V if it doesn't find the association it will return null
   */
  public V remove(K key) {
   int i = hash(key);

   Assoc<K, V> tmp = table[i].get(key);
   if(tmp != null) {
     n--;
     return table[i].remove(0).value;
   }

   return null;

  }


  /*
   *a function which will finds an spot for the association to be placed at
   *@param the key of type K
   * @returns an integer corresponding to the array index
   */
  public int hash(K key) {
    return Math.abs(key.hashCode()) % table.length;
  }

  /**
   * Technical workaround for creating a generic array.
   */

  @SafeVarargs 
  private static <K, V> AList<K, V>[] newTable(int length, 
                                               AList<K, V>... table) {
    return java.util.Arrays.copyOf(table, length);
  }
}
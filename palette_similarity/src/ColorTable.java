
import java.awt.Color;
import java.util.Random;
import java.util.Iterator;

/**
 * There is much to do here!
 *
 * A ColorTable represents a dictionary of frequency counts, keyed on
 * Color. However, it is implemented as a HashMap whose keys are of
 * type TinyColor and whose values are of type Long. The benefit to
 * doing this is so that we can reduce the size of the key space by
 * limiting each Color to a certain number of bits per channel.
 *
 * To Do:
 * Implement this class, including whatever DATA MEMBERS you need
 * and all of the public methods below. You MAY create any number of
 * private methods if you find them to be helpful. Replace all TO DO
 * comments with appropriate javadoc style comments. Be sure to
 * document all data fields and helper methods you define.
 */


/*
All the wonderful ascii art I've lifted from:
https://gist.github.com/paulirish/292907
        .
       /'
      //
  .  //
  |\ /7
 /' " \
.   ☉☉    ?
| (    \  /
|  '._  '
/   \ '-'

*/



public class ColorTable extends HashMap<TinyColor, Long> {
  private int bitsPerChannel;
  private double rehashThreshold;

  private int modCount;

  // colorHash is the hash map that will contain all of the colors and corresponding frequencies
  //private HashMap<Color, Long> colorHash;
  /* this variable refers to the table capacity
  (e.g. the size of the array containing the hashes)*/
  //private int tableCap;

  /**
   * To Do
   * 
   * Constructs a color table with a starting capacity of
   * initialCapacity. Keys in the color key space are truncated to
   * bitsPerChannel bits. The rehashThrehold specifies the maximum
   * tolerable load factor before triggering a rehash. Note that
   * because we are using chaining as our collision resolution
   * strategy, the load factor may rise above 1 without impacting
   * performance.
   * 
   * @throws RuntimeException if initialCapacity is not in the range
   * [1..Constants.MAX_CAPACITY]
   * @throws RuntimeException if bitsPerChannel is not in the range
   * [1..8]
   */

  /*
  constructor that calls HashMap consructor to initialize the array for the map.
 It also sets the parameters to the fields then proceeds for the following:
 - is the initial capacity greater than the maximum set in constants
 - is the bits per channel value in the range [1,8]
 - is the rehash threshold zero or less
   */
  public ColorTable(int initialCapacity, int bitsPerChannel, 
                    double rehashThreshold) {
    super(initialCapacity);

    // throw exception when capacity or bits per channel ranges are exceeded
    if(initialCapacity > Constants.MAX_CAPACITY || initialCapacity < 1)
      throw new RuntimeException();
    if(bitsPerChannel > 8 || bitsPerChannel < 1)
      throw new RuntimeException();

    if(rehashThreshold <= 0)
      throw new RuntimeException();


    // otherwise set initial values
    this.bitsPerChannel = bitsPerChannel;
    this.rehashThreshold = rehashThreshold;
    //this.tableCap = initialCapacity;
  }

  /**
   * To Do
   * Returns the number of bits per channel used by the colors in this table.
   * @returns an integer corresponding to the number of bits we are using for the hashtable
   */
  public int getBitsPerChannel() {
    return bitsPerChannel;
  }

  /**
   * To Do
   * Returns the frequency count associated with the given color. Note
   * that colors not explicitly represented in the table are assumed
   * to be present with a count of zero.
   * @param color Key corresponding with the desired value
   * @return a long corresponding to the frequency of that color
   */

  public Long get(Color color) {
    Long tmp = super.get(new TinyColor(color, bitsPerChannel));

    if(tmp == null)
      return 0L;

    return tmp;
  }

  /**
   * TO DO
   * 
   * Associates the count with the color code in this table. Do NOT store
   * associations with a count of zero. The addition of a new association may
   * trigger a rehash.
   *
   * @throws IllegalStateException if count is negative.
   *
   *
   *
   * This function takes a key/color and frequency/value to be put into the hashmap.
   * it throws an illegal state exception when the count is les sthan zero.
   * It calls the Hashmap super constructor. It rehashes when the load factor
   * exceeds the predefined threshopde.
   * @param a color that corresponds with the key in the association to be placed in the hashmap
*    @param a Long that corresponds with the frequency of that color in the association
 *   @ returns a long which is the frequency from the association
   */

  public Long put(Color color, Long count) {

    if(count < 0)
      throw new IllegalStateException();

    TinyColor umaCor = new TinyColor(color, bitsPerChannel);
    Long tmp = super.put(umaCor, count);
    if(count == 0) {
      remove(umaCor);
    }


      /*if(remove(umaCor) == 0)
        return tmp;*/


    if(getLoadFactor() > rehashThreshold)
      rehash();

    if(tmp == null)
      return 0L;

    return tmp;// esd 0L

  }


  /**
   * TO DO
   * 
   * Increments the frequency count associated with color. Note that
   * colors not explicitly represented in the table are assumed to be
   * present with a count of zero.
   */


  /*
   A void method which increments the frequency of the association corresponding
   the color given. calls the hashmap super get. if the association isn't found
   it puts it in the hashmap
    @param a color that will be the key used to return the association from the map
    */
  public void increment(Color color) {
    TinyColor umaCor = new TinyColor(color, bitsPerChannel);
    Long tmp = super.get(umaCor);
    if(tmp != null)
      put(umaCor, ++tmp);
    else
      put(umaCor, 1L);

    // check and see if rehash is necessary
    if(getLoadFactor() > rehashThreshold)
      rehash();

  }

  /**
   * TO DO
   * 
   * Returns the load factor for this table.
   * @return a double of the load factor
   */

  public double getLoadFactor() {
    return (double)size()/(double)capacity();
  }

  /**
   * To Do
   * 
   * Returns the size of the internal array representing this table.
   * @return an integer that is the size of the internal array of the map
   */
  public int capacity() {
    return table.length;
  }

  /**
   * T ODO
   * 
   * Increases the size of the array to the smallest prime greater
   * than double the current size that is of the form 4j + 3, and then
   * moves all the key/value associations into the new array.
   * 
   * Hints: 
   * -- Make use of Util.isPrime().
   * -- Multiplying a positive integer n by 2 could result in a
   *    negative number, corresponding to integer overflow. You should
   *    detect this possibility and crop the new size to
   *    Constants.MAX_CAPACITY.
   * 
   * @throws RuntimeException if the table is already at maximum capacity.
   *
   *
   *
   */


  /*
  rehash is a void method that finds the next biggest prime when the load factor has been exceeded.
  In the event the maximum capacity has been exceeded it throws a runtime exception
  if the capacity overflows it resets to the max capacity. finally it makes a bigger
  hashmap to contain more alists of associations.
   */
  public void rehash() {
    int initCap = 2 * capacity() + 1;

    if(initCap > Constants.MAX_CAPACITY)
      throw new RuntimeException();


    while(true) {
      if((initCap % 4 == 3) && Util.isPrime(initCap))
        break;

      initCap += 2;

      // there was an overflow
      if(initCap < 0) {
        initCap = Constants.MAX_CAPACITY;
        break;
      }
    }
    ColorTable tmp = new ColorTable(initCap, bitsPerChannel, rehashThreshold);
    for(int i = 0; i < capacity(); i++) {
      for(Assoc<TinyColor, Long> asso: table[i])
        tmp.put(asso.key.expand(), asso.value);
    }
    this.table = tmp.table;

  }

  /**
   * T ODO
   * 
   * Returns an Iterator that marches through each color in the key
   * color space and returns the sequence of frequency counts.
   */



  /*
  this iterator is used to parse the color space incrementing by the number of bit pixels we want
  to look at. iterator contains two methods: hasNexgt and Next.
  fields:
  r, g, b: individual color components ranging from [0,255]
  sizeof: a method that calculates the number of bits to increment by in the color space
   */
  public Iterator<Long> iterator() {
    return new Iterator<Long>() {
      int r = 0, g = 0, b = 0;
      int sizeof = (int) Math.pow(2, (8 - bitsPerChannel));  // because this should be in C. . .


      /*
      a function that tells whether or not you hav arrivd at the end of the hashmap.
      the end of the hashmap is reached when the r value is greater than 256.
      @return a boolean value true if there is a next false if there is not
       */
      public boolean hasNext() {
        return r < 256;
      }


      /*
      a function which increments from one value to the next by
      moving the current color component further down the color space
      towards 255. then it will return the value from the current association
      in the hashmap.
      @return a Long that is the frequency of the current association.
       */
      public Long next() {
        Color umaCor = new Color(r, g, b);

        b += sizeof;
        if (b > 255) {
          b = 0;
          g += sizeof;
          if (g > 255) {
            g = 0;
            r += sizeof;
          }
        }
      return get(umaCor);
      }
    };
  }

  /**
   * Returns a String representation of this table.
   */

  public String toString() {/*
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (int i = 0; i < capacity(); i++)
      for (Assoc<TinyColor, Long> a : table[i])
        sb.append(a).append(" ");
    String ans = sb.toString().trim();
    return ans + "}";*/
    return "";
  }

  /**
   * Simple testing.
   */

  public static void main(String[] args) {
    ColorTable ct = new ColorTable(11, 1, .49);
    ct.put(Color.RED, 5L);
    ct.put(Color.GREEN, 6L);
    ct.put(Color.BLUE, 7L);

    Iterator<Long> it = ct.iterator();
    while (it.hasNext())
      System.out.println(it.next());
        
    ColorTable table = new ColorTable(3, 6, .49); 
    for (int code : new int[] { 32960, 4293315, 99011, 296390 })
      table.increment(new Color(code));

    System.out.println("capacity: " + table.capacity() + "should be 7"); // Expected: 7
    System.out.println("size: " + table.size()+ "should be 3");         // Expected: 3
    
    System.out.println(table);  
  }

}

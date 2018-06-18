import java.awt.Color;

/**
 * There's nothing for you to do here, but be sure to read the
 * comments for the constructor so you understand how a Color is
 * compressed into a TinyColor.
 */

public class TinyColor {

  int code;
  int bitsPerChannel;

  /**
   * The 3 components of a Color are packed into one 32-bit int. The
   * result is used as a hash code for Colors in the ColorTable.
   * 
   * Each color component occupies exactly bitsPerChanel bits in the encoding.
   * 
   * The color components are shifted to the right to drop the lower
   * order bits.  Then the three reduced color components are packed
   * into the lower order 3 * bitsPerChannel bits of the returned
   * code.
   */

  public TinyColor(Color color, int bitsPerChannel) {
    this.bitsPerChannel = bitsPerChannel;
    int r = color.getRed(), g = color.getGreen(), b = color.getBlue(); 
    if (bitsPerChannel >= 1 && bitsPerChannel <= 8) {
      int leftovers = 8 - bitsPerChannel;
      int mask = (1 << bitsPerChannel) - 1; // In binary, this is bitsPerChannel ones.
      // Isolate the higher bitsPerChannel bits of each color component byte by
      // shifting right and masking off the higher order bits.
      r >>= leftovers; 
      r &= mask;
      g >>= leftovers; 
      g &= mask;
      b >>= leftovers; 
      b &= mask;
      // Finally, pack the color components into an int by left shifting into position
      // and xor-ing together.
      code = (((r << bitsPerChannel) ^ g) << bitsPerChannel) ^ b;
    }
    else {
      throw new RuntimeException(String.format("Unsupported number of bits per channel: %d",
          bitsPerChannel));
    }
  }

  /**
   * Undoes the last step in the encoding operation to reconstitute
   * the code into a Color object.
   */

  public Color expand() {
    int r = code, g = code, b = code;
    int mask = (1 << bitsPerChannel) - 1; // In binary, this is bitsPerChannel ones.
    int leftovers = 8 - bitsPerChannel;
    // Isolate the higher bitsPerChannel bits of each color component byte.
    b &= mask;
    b <<= leftovers;
    g >>= bitsPerChannel;
    g &= mask;
    g <<= leftovers;
    r >>= 2 * bitsPerChannel;
    r &= mask;
    r <<= leftovers;
    return new Color(r, g, b);
  }

  public boolean equals(Object that) {
    return that instanceof TinyColor && 
      ((TinyColor) that).code == code;
  }

  public int hashCode() {
    return Math.abs(code);
  }

  public String toString() {
    return "" + code;
  }
}
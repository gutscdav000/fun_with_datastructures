import java.util.Iterator;

/**
 * A Path represents a sequence of connected coordinates.
 */

public class Path extends DoublyLinkedList<Coord> implements Iterable<Coord> {
  protected Wire wire;
  
  public Path() {

  }

  public Path(Wire wire) {
    this.wire = wire;
    add(wire.from);
  }

  /**
   * Returns the length of this path.
   */

  public int length() {
    return size();
  }
  
}

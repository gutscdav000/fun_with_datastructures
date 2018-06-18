import java.awt.*;
import java.util.Iterator;

/**
 * TODO #6
 *
 * Most of the work for this project involves implementing the
 * connectAllWires() method in this class. Feel free to create
 * any helper methods that you deem necessary. 
 * 
 * Your goal is to come up with an efficient algorithm that will 
 * find a layout that connects all the wires (if one exists) while
 * attempting to minimize the overall wire length. Note that you
 * are expected to describe your algorithm in a report in the
 * Driver comment.
 */

public class PathFinder {

  /**
   * TODO
   * 
   * Lays out a path connecting each wire on the chip, and then 
   * returns a map that associates a wire id numbers to the paths
   * corresponding to the connected wires on the grid. If it is 
   * not possible to connect the endpoints of a wire, then there
   * should be no association for the wire id# in the result.
   */

  public static Map<Integer, Path> connectAllWires(Chip chip) {
    Map<Integer, Path> layout = new HashMap<>();

    // fill priority queue to find shortest path  for the shortest distance first
    PriorityQueue<Wire> connectionOrder = new PriorityQueue<>((x,y) -> (x.separation() - y.separation()));

    // add all the wires to the queue
    for(Wire w : chip.wires)
      connectionOrder.offer(w);

    // find the shortest path for wires in the order that the priority queue spits the out
    while(!connectionOrder.isEmpty()) {
      Wire curr = connectionOrder.poll();

      Path shortPath = shortestPath(curr, chip);

      // if we cannot find a valid path throw the exception
      if(shortPath.size() == 1 && !curr.from.equals(curr.to))
        throw new NoPathException(curr);

      layout.put(curr.wireId, shortPath);

    }
    return layout;
  }


  private static Path shortestPath(Wire wire, Chip chip) {

    // return path of size 1 when from and to are same coord
    if(wire.to.equals(wire.from))
      return new Path(wire);

    // priority queue that maintains the part of the grid we are exploreing
    PriorityQueue<Coord> explore = new PriorityQueue<>((x,y) -> {
      // sorts by euclidian distance
      int xdis = (int) Math.sqrt(Math.pow(wire.to.x - x.x, 2) + Math.pow(wire.to.y - x.y, 2));
      int ydis = (int) Math.sqrt(Math.pow(wire.to.x - y.x, 2) + Math.pow(wire.to.y - y.y, 2));
      return xdis - ydis;
      }
    );
    HashMap<Coord, Coord> path = new HashMap<>();
    Coord curr;
    explore.offer(wire.from); // add the first coord to be explored

    while(!explore.isEmpty()) {
      curr = explore.poll();

      // we've reached the end when current equals to
      if(curr.equals(wire.to)) {
        Path result = new Path(wire);

        // build up the result in reverse
        while(!path.get(curr).equals(wire.from)) {
          curr = path.get(curr);
          result.addFront(curr);

        }
        result.add(wire.to);

        // ACTUALLY lay the wire
        chip.layoutWire(wire.wireId, result);

        return result;
      }

      // get the neighbors of the current coord
      List<Coord> neighbors = curr.neighbors(chip.dim);
      for(Coord c : neighbors) {
        // if it is availible and it is not already in the path (e.g. has not been visited)
        // Note: wire.to is always taken so we have to have a separate check for that
        // THEN offer it to explore and insert it into the path map
        if((chip.isFree(c) || c.equals(wire.to)) && path.get(c) == null) {
          explore.offer(c);
          path.put(c, curr);
        }

      }
    }

    return new Path(wire);
  }

  /**
   * 
   * Returns the sum of the lengths of all non-null paths in the given layout.
   */

  public static int totalWireUsage(Map<Integer, Path> layout) {
    int sum = 0;

    Iterator<Path> it = layout.values();

    while(it.hasNext()) {
      Path hold = it.next();
      if(hold != null)
        sum += hold.length();
    }

    return sum;
  }

}

class NoPathException extends RuntimeException {
  public Wire wire;

  public NoPathException(Wire wire) {
    this.wire = wire;
  }
}


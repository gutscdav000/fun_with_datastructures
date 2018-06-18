import static org.junit.Assert.*;
import java.awt.Dimension;
import java.util.Random;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.io.File;
import org.junit.Test;
import org.omg.PortableInterceptor.INACTIVE;

import javax.xml.bind.SchemaOutputResolver;

/**
 * TODO #1: Implement the upgrades to the legacy code, as described in
 * the Canvas assignment. When you are done, go to Chip.
 *
 * Write a comprehensive suite of unit tests!!!!
 * 
 * We include some tests to get you started, but you need to write many more
 * of your own!
 */

public class Testing {

  @Test
  public void testDLLCopyConstructor() {
    List<Coord> ls1 = new DoublyLinkedList<>();
    ls1.add(new Coord(1, 2));
    ls1.add(new Coord(3, 4));
    ls1.add(new Coord(5, 6));
    ls1.remove(new Coord(3, 4)); // overloaded remove from p2
    assertEquals(2, ls1.size());
    assertEquals(new Coord(1, 2), ls1.get(0));
    assertEquals(new Coord(5, 6), ls1.get(1));
    List<Coord> ls2 = new DoublyLinkedList<>(ls1);
    assertEquals(2, ls1.size());
    assertEquals(2, ls2.size());
    ls1.remove(new Coord(1, 2));
    assertEquals(1, ls1.size());
    assertEquals(2, ls2.size());
    assertEquals(new Coord(5, 6), ls1.get(0));
    Coord coord = new Coord(7, 8);
    ls2.add(coord);
    assertEquals(1, ls1.size());
    assertEquals(3, ls2.size());
    assertEquals(new Coord(5, 6), ls1.get(0));
    assertEquals(new Coord(1, 2), ls2.get(0));
    assertEquals(new Coord(5, 6), ls2.get(1));
    assertEquals(new Coord(7, 8), ls2.get(2));
    assertEquals(coord, ls2.get(2));
    // It's a shallow copy. Here, I'll prove it:
    coord.x = 6;
    assertEquals(coord, ls2.get(2));
    assertEquals(new Coord(6, 8), ls2.get(2));
    for (int i = 0; i < 3; i++)
      ls2.remove(0);
    assertTrue(ls2.isEmpty());
    assertFalse(ls1.isEmpty());
  }

  @Test
  public void testHashMapToString() {
    /**
     * The associations in a HashMap are enclosed in curly braces and
     * separated by commas. Each association is written as key=value.
     * The order of the associations is not specified. The only added
     * spaces are the ones after each comma.
     */
    Map<Integer, Integer> hm = new HashMap<>();
    assertEquals("{}", hm.toString());
    hm.put(2,  1);
    assertEquals("{2=1}", hm.toString());
    hm.put(2, 0);
    assertEquals("{2=0}", hm.toString());
    hm.put(3, 1);
    hm.put(10, 2);
    hm.put(8, 3);
    hm.put(5, 4);
    assertTrue(verifyHashMapToString("{2=0, 3=1, 5=4, 8=3, 10=2}",
        hm.toString()));
  }

  @Test
  public void testHashMapConcurrentMods() {
    Map<Integer, String> hm = new HashMap<>();
    hm.put(2, "two");
    hm.put(3, "three");
    hm.put(10, "ten");
    hm.put(8, "eight");
    hm.put(5, "five");

    Iterator<Integer> keyIt = hm.keys();
    int sum = 0;
    try {
      while (keyIt.hasNext()) {
        sum = keyIt.next();
        hm.put(7, "seven");
      }
      fail();
    }
    catch (ConcurrentModificationException ex) { }

    Iterator<String> valIt = hm.values();
    String all = "";
    try {
      while (valIt.hasNext()) {
        all += valIt.next();
        hm.remove(4);
      }
      assertEquals(25, all.length());
    }
    catch (ConcurrentModificationException ex) {
      fail();
    }

    valIt = hm.values();
    all = "";
    try {
      while (valIt.hasNext()) {
        all += valIt.next();
        hm.remove(5);
      }
      fail();
    }
    catch (ConcurrentModificationException ex) { }
  }

  @Test
  public void testPriorityQueueToString() {
    /**
     * The entries in a PriorityQueue are enclosed in square brackets and
     * separated by commas. The entries are given in the order in which
     * they appear in the internal heap array, not in priority order.
     * The only added spaces are the ones after each comma.
     */
    PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> x - y);
    assertEquals("[]", pq.toString());
    pq.offer(7);
    assertEquals("[7]", pq.toString());
    pq.offer(3);
    assertEquals("[3, 7]", pq.toString());
    for (int i = 10; i <= 20; i += 2)
      pq.offer(i);
    assertEquals("[3, 7, 10, 12, 14, 16, 18, 20]", pq.toString());
  }

  @Test
  public void testPriorityQueueRemove() {
    PriorityQueue<Coord> pq = new PriorityQueue<>((p1, p2) -> p1.x - p2.x);
    pq.offer(new Coord(5, 0));
    assertTrue(pq.remove(new Coord(5, 0)));
    assertTrue(pq.isEmpty());
    Coord[] data = new Coord[]{
        new Coord(2, 0),
        new Coord(4, 1),
        new Coord(7, 2),
        new Coord(1, 3),
        new Coord(9, 4),
        new Coord(3, 5),
        new Coord(0, 6), // data[6]
        new Coord(8, 7),
    };
    for (Coord coord : data)
      pq.offer(coord);
    assertEquals(8, pq.size());
    assertEquals("[(0, 6), (2, 0), (1, 3), (4, 1), (9, 4), (7, 2), (3, 5), (8, 7)]",
        pq.toString());
    assertEquals(data[6], pq.peek());
    assertTrue(pq.remove(data[6]));
    assertEquals(7, pq.size());
    assertEquals("[(1, 3), (2, 0), (3, 5), (4, 1), (9, 4), (7, 2), (8, 7)]",
        pq.toString());
    assertEquals(data[3], pq.peek());
    assertFalse(pq.remove(data[6]));
    assertEquals(data[3], pq.peek());
    for (Coord coord : data)
      pq.remove(coord);
    assertTrue(pq.isEmpty());
  }

  @Test
  public void testPriorityQueueRemoveStressTest() {
    // This might take a couple seconds! Longer than that means you're doing it
    // wrong!
    PriorityQueue<Integer> xs = new PriorityQueue<>((x, y) -> x - y);
    int n = 100_000;
    Random rand = new Random();
    // Capture the current time:
    long start = System.currentTimeMillis();
    for (int i = 0; i < n; i++)
      xs.offer(rand.nextInt(n));
    for (int i = 0; i < n; i++)
      xs.remove(rand.nextInt(n));
    // Print elapsed time:
    System.out.println(System.currentTimeMillis() - start);
  }

  //*********************
  // Wire Routing Tests *
  //*********************

  @Test
  public void chip4File() {
    String chipName = "small_04";
    String fileName = String.format("%s/%s%s", Constants.INPUTS_FOLDER,
                                    chipName, Constants.EXTENSION);
    Chip chip4 = new Chip(new File(fileName));
    assertEquals(7, chip4.dim.width);
    assertEquals(6, chip4.dim.height);
    assertEquals(4, chip4.obstacles.size());
    assertEquals(1, chip4.wires.size());
    assertEquals(1, chip4.wires.get(0).wireId);
    assertEquals(4, chip4.wires.get(0).from.x);
    assertEquals(3, chip4.wires.get(0).from.y);
    assertEquals(2, chip4.wires.get(0).to.x);
    assertEquals(3, chip4.wires.get(0).to.y);
  }

  @Test
  public void tinyObstacle() {
    Obstacle obs = new Obstacle(5, 5, 5, 5);
    assertTrue(obs.contains(new Coord(5, 5)));
  }

  @Test
  public void testObstacleContains() {
    Obstacle obs;
    obs = new Obstacle(new Coord(1, 1), new Coord(2, 2));
    assertTrue(obs.contains(new Coord(1, 1)));
    assertTrue(obs.contains(new Coord(1, 2)));
    assertTrue(obs.contains(new Coord(2, 1)));
    assertTrue(obs.contains(new Coord(2, 2)));
    assertFalse(obs.contains(new Coord(0, 0)));
    assertFalse(obs.contains(new Coord(5, 3)));
    assertFalse(obs.contains(new Coord(7, 7)));
    assertFalse(obs.contains(new Coord(2, 6)));
    assertFalse(obs.contains(new Coord(1, 5)));
    assertFalse(obs.contains(new Coord(0, 3)));

    obs = new Obstacle(new Coord(9, 9), new Coord(10, 12));
    assertTrue(obs.contains(new Coord(9, 9)));
    assertTrue(obs.contains(new Coord(9, 10)));
    assertTrue(obs.contains(new Coord(9, 11)));
    assertTrue(obs.contains(new Coord(9, 12)));
    assertTrue(obs.contains(new Coord(10, 9)));
    assertTrue(obs.contains(new Coord(10, 10)));
    assertTrue(obs.contains(new Coord(10, 11)));
    assertTrue(obs.contains(new Coord(10, 12)));
    assertFalse(obs.contains(new Coord(0, 0)));
    assertFalse(obs.contains(new Coord(5, 3)));
    assertFalse(obs.contains(new Coord(7, 7)));
    assertFalse(obs.contains(new Coord(2, 6)));
    assertFalse(obs.contains(new Coord(1, 5)));
    assertFalse(obs.contains(new Coord(0, 3)));

    obs = new Obstacle(new Coord(14, 12), new Coord(14, 100));
    for (int x = 0; x <= 200; x++)
      for (int y = 0; y <= 200; y++)
        assertEquals(x == 14 && y >= 12 && y <= 100,
            obs.contains(new Coord(x, y)));
    obs = new Obstacle(new Coord(14, 12), new Coord(100, 12));
    for (int x = 0; x <= 200; x++)
      for (int y = 0; y <= 200; y++)
        assertEquals(y == 12 && x >= 14 && x <= 100,
            obs.contains(new Coord(x, y)));
  }

  @Test
  public void tinyWire() {
    Wire w1 = new Wire(1, 1, 2, 3, 4);
    assertEquals(1, w1.wireId);
    assertEquals(new Coord(1, 2), w1.from);
    assertEquals(new Coord(3, 4), w1.to);
    assertEquals(4, w1.separation());

    Wire w2 = new Wire(2, 3, 4, 1, 2);
    assertEquals(2, w2.wireId);
    assertEquals(new Coord(3, 4), w2.from);
    assertEquals(new Coord(1, 2), w2.to);
    assertEquals(4, w2.separation());
  }

  @Test
  public void testWireSeparator() {
    Wire wire = new Wire(1, new Coord(1, 1), new Coord(2, 2));
    assertEquals(2, wire.separation());
    wire = new Wire(2, new Coord(3, 1), new Coord(6, 2));
    assertEquals(4, wire.separation());
    wire = new Wire(1, new Coord(3, 3), new Coord(6, 6));
    assertEquals(6, wire.separation());
    wire = new Wire(1, new Coord(7, 3), new Coord(7, 6));
    assertEquals(3, wire.separation());
    wire = new Wire(1, new Coord(1, 3), new Coord(2, 3));
    assertEquals(1, wire.separation());
    wire = new Wire(1, new Coord(1, 3), new Coord(1, 3));
    assertEquals(0, wire.separation());
  }

  @Test
  public void chip4Manual() {
    HashMap<Integer, Integer> hm = new HashMap<>();
    hm.put(2,  0);
    hm.put(3, 1);
    hm.put(10, 2);
    hm.put(8, 3);
    hm.put(5, 4);
    
    Dimension dim;
    List<Obstacle> obstacles = new DoublyLinkedList<>();
    List<Wire> wires = new DoublyLinkedList<>();

    // Build the chip described in small_04
    dim = new Dimension(7, 6);
    obstacles.add(new Obstacle(1, 1, 1, 4));
    obstacles.add(new Obstacle(1, 4, 3, 4));
    obstacles.add(new Obstacle(3, 2, 3, 4));
    obstacles.add(new Obstacle(3, 2, 5, 2));
    wires.add(new Wire(1, 4, 3, 2, 3));
    Chip chip4 = new Chip(dim, obstacles, wires);

    // Test properties of chip4.
    assertEquals(7, chip4.dim.width);
    assertEquals(6, chip4.dim.height);
    assertEquals(4, chip4.obstacles.size());
    assertEquals(1, chip4.wires.size());
    assertEquals(1, chip4.wires.get(0).wireId);
    assertEquals(4, chip4.wires.get(0).from.x);
    assertEquals(3, chip4.wires.get(0).from.y);
    assertEquals(2, chip4.wires.get(0).to.x);
    assertEquals(3, chip4.wires.get(0).to.y);
  }

  @Test
  public void chip9Manual() {
    Dimension dim;
    List<Obstacle> obstacles = new DoublyLinkedList<>();
    List<Wire> wires = new DoublyLinkedList<>();

    // Build the chip described in small_09
    dim = new Dimension(4, 5);
    wires.add(new Wire(1, 1, 0, 1, 4));
    wires.add(new Wire(2, 0, 1, 2, 1));
    wires.add(new Wire(3, 0, 2, 2, 2));
    wires.add(new Wire(4, 0, 3, 2, 3));
    Chip chip9 = new Chip(dim, obstacles, wires);

    // TODO: Test properties of chip9.

    //vanilla tests
    assertEquals(chip9.dim.width, 4);
    assertEquals(chip9.dim.height, 5);
    assertEquals(chip9.obstacles.size(), 0);
    assertEquals(chip9.wires.size(), 4);

    assertEquals(1, chip9.wires.get(0).wireId);
    assertEquals(1, chip9.wires.get(0).from.x);
    assertEquals(0, chip9.wires.get(0).from.y);
    assertEquals(1, chip9.wires.get(0).to.x);
    assertEquals(4, chip9.wires.get(0).to.y);

    assertEquals(2, chip9.wires.get(1).wireId);
    assertEquals(0, chip9.wires.get(1).from.x);
    assertEquals(1, chip9.wires.get(1).from.y);
    assertEquals(2, chip9.wires.get(1).to.x);
    assertEquals(1, chip9.wires.get(1).to.y);

    assertEquals(3, chip9.wires.get(2).wireId);
    assertEquals(0, chip9.wires.get(2).from.x);
    assertEquals(2, chip9.wires.get(2).from.y);
    assertEquals(2, chip9.wires.get(2).to.x);
    assertEquals(2, chip9.wires.get(2).to.y);

    assertEquals(4, chip9.wires.get(3).wireId);
    assertEquals(0, chip9.wires.get(3).from.x);
    assertEquals(3, chip9.wires.get(3).from.y);
    assertEquals(2, chip9.wires.get(3).to.x);
    assertEquals(3, chip9.wires.get(3).to.y);

    // path tests
    Map<Integer, Path> layout = PathFinder.connectAllWires(chip9);
    //Iterator<Path> iter = ((HashMap<Integer,Path>)layout).values();

    String[] paths = {"((1, 0) (2, 0) (3, 0) (3, 1) (3, 2) (3, 3) (3, 4) (2, 4) (1, 4))", "((0, 1) (1, 1) (2, 1))","((0, 2) (1, 2) (2, 2))","((0, 3) (1, 3) (2, 3))"};
    Iterator<Integer> iter = ((HashMap<Integer,Path>)layout).keys();

    while(iter.hasNext()) {
      Integer next = iter.next();
      assertEquals(((HashMap<Integer, Path>) layout).get(next).toString(), paths[--next].toString());
    }
  }

  @Test
  public void chip4Layout() {
    String chipName = "small_04";
    String fileName = String.format("%s/%s%s", Constants.INPUTS_FOLDER,
                                    chipName, Constants.EXTENSION);
    Chip chip4 = new Chip(new File(fileName));
    Map<Integer, Path> layout = PathFinder.connectAllWires(chip4);
    assertNotNull(layout);
    assertEquals(1, layout.size());

    /*
    7
    6
    4
    1 1 1 4
    1 4 3 4
    3 2 3 4
    3 2 5 2
    1
    4 3 2 3
     */
    // TODO: Test properties of layout.get(1).

    //vanilla tests
    assertEquals(chip4.dim.width, 7);
    assertEquals(chip4.dim.height, 6);
    assertEquals(chip4.obstacles.size(), 4);
    assertEquals(chip4.wires.size(), 1);

    assertEquals(1, chip4.wires.get(0).wireId);
    assertEquals(4, chip4.wires.get(0).from.x);
    assertEquals(3, chip4.wires.get(0).from.y);
    assertEquals(2, chip4.wires.get(0).to.x);
    assertEquals(3, chip4.wires.get(0).to.y);

    assertEquals(1, chip4.obstacles.get(0).upperLeft.x);
    assertEquals(1, chip4.obstacles.get(0).upperLeft.y);
    assertEquals(1, chip4.obstacles.get(0).lowerRight.x);
    assertEquals(4, chip4.obstacles.get(0).lowerRight.y);

    assertEquals(1, chip4.obstacles.get(1).upperLeft.x);
    assertEquals(4, chip4.obstacles.get(1).upperLeft.y);
    assertEquals(3, chip4.obstacles.get(1).lowerRight.x);
    assertEquals(4, chip4.obstacles.get(1).lowerRight.y);

    assertEquals(3, chip4.obstacles.get(2).upperLeft.x);
    assertEquals(2, chip4.obstacles.get(2).upperLeft.y);
    assertEquals(3, chip4.obstacles.get(2).lowerRight.x);
    assertEquals(4, chip4.obstacles.get(2).lowerRight.y);

    assertEquals(3, chip4.obstacles.get(3).upperLeft.x);
    assertEquals(2, chip4.obstacles.get(3).upperLeft.y);
    assertEquals(5, chip4.obstacles.get(3).lowerRight.x);
    assertEquals(2, chip4.obstacles.get(3).lowerRight.y);

    // path test
    assertEquals(layout.get(1).toString(),
            "((4, 3) (4, 4) (4, 5) (3, 5) (2, 5) (1, 5) (0, 5) (0, 4) (0, 3) (0, 2) (0, 1) (0, 0) (1, 0) (2, 0) (2, 1) (2, 2) (2, 3))");

  }

  /*********************************************************************
   * Benchmarks: Computes layouts for chips described in data/wire*.in *
   *********************************************************************/

  @Test
  public void runBatches() {
     runBatchFor("small");
     runBatchFor("medium");
     runBatchFor("big");
     runBatchFor("huge");
     runBatchFor("nopath");
  }

  /**
   * Runs the benchmarks on all filenames starting with the given prefix.
   */

  public static void runBatchFor(String prefix) {
    System.out.println(String.format("Routing chips %s/%s*%s\n",
        Constants.INPUTS_FOLDER, prefix, Constants.EXTENSION));
    File folder = new File(Constants.INPUTS_FOLDER);
    for (File file : folder.listFiles()) 
      if (file.isFile() && file.getName().startsWith(prefix) &&
          file.getName().endsWith(Constants.EXTENSION)) {
        System.out.println("========== " + file.getName() + " ==========");
        Chip chip = new Chip(file);
        System.out.println("before:\n" + chip);
        Map<Integer, Path> layout = new HashMap<>();
        try {
          layout = PathFinder.connectAllWires(chip);
        }
        catch (NoPathException ex) {
          System.out.println(String.format("*** WARNING: Could not layout %s.",
              ex.wire));
        }
        if (layout.size() != chip.wires.size())
          System.out.println();
        System.out.println("after:\n" + chip);
        chip.initLayout();
        if (!validateLayout(layout, chip))
          System.out.println(file.getName());
        boolean valid = validateLayout(layout, chip);
         assertTrue(valid);//***
        System.out.println(valid? file.getName() + " is valid!": file.getName() + " is not valid!");
        System.out.println("cost: " + PathFinder.totalWireUsage(layout));
      } 
    System.out.println("==============================");
    System.out.println("Benchmarks completed...\n");
  }

  /**
   * Returns true iff the given wire layout is legal on the given grid.
   */

  public static boolean validateLayout(Map<Integer, Path> layout, Chip chip) {
    String msg = "Incorrect %s of path for wire %d, found %s, expected %s.";
    Dimension dim = chip.dim;
    List<Obstacle> obstacles = chip.obstacles;
    List<Wire> wires = chip.wires;
    int numWires = wires.size();
    for (int i = 1; i <= numWires; i++) {
      Path path = layout.get(i);
      if (path != null) {
        Coord start = path.get(0), end = path.get(path.size() - 1);
        if (!start.equals(wires.get(i - 1).from)) {
          System.out.println(String.format(msg, "start", 
              i, start, wires.get(i - 1).from));
          return false;
        }
        if (!end.equals(wires.get(i - 1).to)) {
          System.out.println(String.format(msg, "end", 
              i, start, wires.get(i - 1).to));
          return false;
        }
        java.util.Set<Coord> used = new java.util.HashSet<>();
        for (int j = 0; j < path.size(); j++) {
          Coord cell = path.get(j);
          // Make sure the cell coordinates are in range for the grid.
          if (!cell.onBoard(dim))
            return false;
          // Make sure none of the wires cross each other.
          if (used.contains(cell)) 
            return false;
          // Make sure that the path consists only of connected neighbors.
          if (j > 0 && !path.get(j - 1).neighbors(dim).contains(cell))
            return false;
          // Make sure that the path doesn't pass through an obstacle.
          for (Obstacle obs : obstacles) {
            if (obs.contains(cell))
              return false;
          }
          used.add(cell);
        }
      }
    }
    return true;
  }

  /**
   * Verifies the form of HashMap.toString()
   */

  public static boolean verifyHashMapToString(String expected, String actual) {
    int n = actual.length();
    boolean quickChecks = expected.length() == n &&
        expected.startsWith(actual.substring(0, 1)) &&
        expected.endsWith(actual.substring(n - 1));
    if (!quickChecks)
      return false;
    String[] expectedTokens = expected.substring(1, n - 1).split(", ");
    String[] actualTokens = actual.substring(1, n - 1).split(", ");
    for (String token : expectedTokens) {
      boolean found = false;
      for (String s : actualTokens)
        if (token.equals(s)) {
          found = true;
          break;
        }
      if (!found)
        return false;
    }
    return true;
  }

}

import java.io.File;
import javax.swing.SwingUtilities;

/**
 * This is the main entry point for the application. When run, a GUI
 * will appear showing the chip with the wires connected. The layout
 * computed by PathFinder will be used unless it returns an empty
 * layout. In that case, a fixedLayout will be used (if it exists for
 * the chip).
 *
 * TODO #3: Follow the instructions on Canvas to run the GUI. When you
 * are done, go to Obstacle.
 *
 * TODO #7: Write your report here:
 *
 *
 * 1) Give an English description of your solution strategy. Explain exactly how wires are connected and
 *    what you do when you are unable to make a connection.
 *
 *    My solution strategy uses a breadth first search to find the shortest path of an individual wire with the
 *    euclidean distance formula, while using a priority queue that sorts the manhattan distance between the
 *    starting and ending point for a particular wire with the wires with the shortest path being pushed to the
 *    front of the queue for choosing which wire to lay first.
 *
 *    - the PriorityQueue for ordering the manhattan distance between to and from points for each wire is made
 *
 *    - next all of the wires for the board are offered to the queue.
 *
 *    while the queue is not empty (e.g. not all of the wires have been laid):
 *        * get the next wire from the queue
 *        * find the shortest path
 *        * actually lay the wire
 *        * put the ID of the wire in a hashmap and map it to the wire path
 *    if any of the wires being laid don't have a path throw a NoPathException
 *
 *
 *
 * 2) Write a high-level pseudo-code description of your algorithm to route one wire.
 *
 * The Shortest Path method is used for finding the optimal path for one wire using a breadth first search. This method uses two main data
 * structures in finding the path: the first is a HashMap that maps the coordinate of one space on the grid to
 * the space on the grid from which it came (e.g. its parent coordinate) this structure is useful for building
 * up the shortest path after the whole board has been explored; the second structure is a priority queue that
 * stores the nodes to be explored, ordering them by their euclidean distances.
 *
 * first we check the base case: is the starting and ending point the same, return a new path of size 1
 *
 * now we put the first coordinate (from) into the priority queue of points to be explored
 *
 * so long as the priority queue isn't empty and the current coordinate isn't the to coordinate do {
 *
 *     - grap the next coord in the priority queue of points to be explored
 *
 *     - get all of the neighbors of this current coordinate
 *     - for every neighbor:
 *          if this neighbor is free and is not in the path hashmap:
 *              - put it in the hashmap mapping the neighbor to its parent
 *              - add the neighbor to the queue of points to be explored
 *
 *
 *
 * }
 *
 * iff the queue is empty throw NoPathException
 *
 * - otherwise start a new path of size 1
 *
 * while while the parent of the current value in the path isn't the first coord (from) {
 *    - get the parent of the last current coordinate
 *    - add the current coord to the path right after the first/from coord
 * }
 *
 * - add the last/to coord
 * - lay the path for this wire
 * - return the path
 *
 *
 * 3)Enumerate ANY data structures you use along the way and explain their purpose in your algorithm.
 *
 *
 *    // since they're technically "data structures"
 *
 *    Chip :       Chip is a data structure that is used to store all of the board/chip information
 *                 for each problem. it maintains information regarding the wires, obstacles, chip
 *                 dimmensions, and obstacles. the constructor for the chip innitializes the chip to
 *                 it's unconnected state.
 *
 *    Wire :       wire objects contain the wire ID as well as starting and ending points on the chip.
 *                 it also has a useful method for finding the manhattan distance between the start and
 *                 end points.
 *
 *    Obstacle :   obstacle objects contain the dimensions for the size of the obstacle, as well as a
 *                 method to check and see if a particular coordinate is contained within the obstacle.
 *
 *    Path :       path objects are glorified linked lists where each node is one of the coords in a
 *                 shortest path for a wire.
 *
 *    Coord :      coords are x , y pairs. the objects have methods for finding adjacent coords on the
 *                 board, as well as all neighbors and a check to see if it is on the board
 *
 *     // main data structures
 *
 *    shortest path explore PriorityQueue :  this is a priority queue that organizes each coord on the grid by
 *                                           their straight line (euclidean distance) from that coord to the
 *                                           ending coord. We use the euclidean distance because we are looking
 *                                           for the absolute shortest path to the end point irrespective of the
 *                                           "cost" of getting there since each coord we touch have the equal cost
 *                                           of 1 to traverse it.
 *
 *    connect all wires PriorityQueue : this is a priority queue that organizes each wire put into it by
 *                                      the manhattan distance between the starting/from and ending/to coords.
 *                                      it is ordered this way because we want to know the shortest path each
 *                                      wire could possibly find. since it costs one move to move horozontally
 *                                      and vertically and two moves to move diagonally we use manhattan distance.
 *
 *    layout HashMap :          the layout maps a wire ID to the shortest path for that wire on the chip.
 *                              whenever a shortest path is layed on the ship the path is put into the hashmap.
 *                              this is where the "solution" is stored which can be useful for validating and
 *                              vizualizing the results.
 *
 *    path HashMap :          the path hashmap maps the current coordinate to its parent coordinate. this hashmap
 *                            becomes useful once we have explored all of the grid for a certain chip and we need
 *                            to build up the path for that wire. since each coord in the map maps to it's parent
 *                            coordinate (e.g. how it got to that point) we can start at the end point and traverse
 *                            the hashmap backwards until we get to the starting point, at which point we will have
 *                            the exact shortest path that was used to get to the endpoint.
 *
 * 4)Tell us about any heuristics you've implemented and why you think they're useful.
 *
 *        two Heuristics are used at different stages of the algorithm:
 *
 *        1) we need a heuristic to determine the order in which individual wires get laid
 *           out for each chip in the connectAllWires method. For this decision I use the manhattan distance between the
 *           starting and ending points for each wire. It is ordered this way because we
 *           want to know the shortest path each wire could possibly find, not the direct
 *           line distance which would be more suitble for finding the straight line distance.
 *           since it costs one move to move horozontally and vertically and two moves to move
 *           diagonally we use manhattan distance.
 *
 *
*        2)  in the shortest path method we also need a heuristic for determining the order
 *        in which choose to explore the chip space. for this i used the euclidean (straight line)
 *        distance between the current coordinate and the end coordinate. By doing this we look
 *        for the absolute fastest path for getting to the end point from the current coord. This
 *        didn't seem like a logical strategy to me since manhattan takes into account the extra
 *        cost for moving diagonally. I thought this would be the most logical heuristic to use,
 *        but this methodology would not pass the small 11 and 12 chips, medium 1 and 4 chips, failed all of the big
 *        chips, and ran infinitly on the huge board. I wonder if this has something to do with
 *        manhattan distance potentially blocking or not finding a path needed by one of the
 *        wires that would not be impeded by using the euclidean distance. These are just
 *        conjectures, I'm not exactly sure why this heuristic performed so much better.
 *        This has been vexing to me. When using euclidean distance the only two chips I couldn't lay
 *        all of the wires for were the small 11 and 12 chips which is much better performance than
 *        having these two on top of the others.
 *
 *
 *
 *
 *
 * 5)In a table, summarize your successes and failures on each of the sample input files that are included with the project.
 *   If you are unable to produce a layout for one of the chips, state whether or not this is due to a NoPathException
 *   or your algorithm being too slow (non-terminating).
 *
 *   small_01.in : success -> cost: 2
 *   small_02.in : success -> cost: 3
 *   small_03.in : success -> cost: 14
 *   small_04.in : success -> cost: 17
 *   small_05.in : success -> cost: 12
 *   small_06.in : success -> cost: 20
 *   small_07.in : success -> cost: 12
 *   small_08.in : success -> cost: 27
 *   small_09.in : success -> cost: 18
 *   small_10.in : success -> cost: 11
 *   small_11.in : failure -> throws NoPathException for wire 3
 *   small_12.in : failure -> throws NoPathException for wire 2
 *
 *   medium_01.in : success -> cost: 70
 *   medium_02.in : success -> cost: 253
 *   medium_03.in : success -> cost: 105
 *   medium_04.in : success -> cost: 243
 *
 *   big_01.in : success -> cost: 445
 *   big_02.in : success -> cost: 702
 *   big_03.in : success -> cost: 1277
 *   big_04.in : success -> cost: 5238
 *
 *   huge_01.in : success -> 12364
 *
 *   nopath_01.in : failure -> throws NoPathException for wire 1
 *   nopath_02.in : failure -> throws NoPathException for wire 3
 */

public class Driver {

  public static void main(String... args) {
    String chipName = "small_04";  // change this name for different chips

    System.out.println(Constants.TITLE);
    System.out.println(String.format("Start the GUI on %s ...", chipName));
    String fileName = String.format("%s/%s%s", Constants.INPUTS_FOLDER,
                                    chipName, Constants.EXTENSION);
    File file = new File(fileName);
    Chip chip = new Chip(file);
    Map<Integer, Path> layout = getLayout(chip);

    int[][] data;
    if (layout.isEmpty() && (data = getData(chipName)) != null) {
      /**
       * data[i] contains the flattened coordinates of wire (i+1)'s path.
       * Manually lay out the wires for this chip as specified by data.
       */
      for (int i = 0; i < data.length; i++) {
        Path p = new Path();
        for (int j = 0; j < data[i].length; j += 2) {
          Coord coord = new Coord(data[i][j], data[i][j + 1]);
          p.add(coord);
          chip.grid.put(coord, i + 1);
        }
        layout.put(i + 1, p);
      }
    }
    SwingUtilities.invokeLater(() -> new GUI(chip, layout, fileName));
  }

  /**
   * Use PathFinder to layout the wires on the chip.
   */

  public static Map<Integer, Path> getLayout(Chip chip) {
    Map<Integer, Path> layout;
    try {
      layout = PathFinder.connectAllWires(chip);
    }
    catch (NoPathException ex) {
      System.out.println(String.format("Could not layout %s.", ex.wire));
      layout = new HashMap<>();
    }
    return layout;
  }

  /**
   * Returns a data description of the wire connections for the chip with
   * the given name, if one exists in the database. Otherwise, returns null.
   */

  public static int[][] getData(String chipName) {
    Map<String, int[][]> database = new HashMap<>();
    database.put("small_02", new int[][] {
        { 0, 0, 1, 0, 1, 1 }, // 1
    });
    database.put("small_09", new int[][] {
        { 1, 0, 2, 0, 3, 0, 3, 1, 3, 2, 3, 3, 3, 4, 2, 4, 1, 4 }, // 1
        { 0, 1, 1, 1, 2, 1 }, // 2
        { 0, 2, 1, 2, 2, 2 }, // 3
        { 0, 3, 1, 3, 2, 3 }, // 4
    });
    database.put("medium_03", new int[][] {
        { 10, 12, 10, 13, 10, 14, 10, 15, 10, 16, 10, 17, 10, 18, 10, 19, 10,
            20, 10, 21 }, // 1
        { 12, 10, 13, 10, 14, 10, 15, 10, 16, 10, 17, 10, 18, 10, 19, 10, 20,
            10, 21, 10 }, // 2
        { 23, 12, 23, 13, 23, 14, 23, 15, 23, 16, 23, 17, 23, 18, 23, 19, 23,
            20, 23, 21 }, // 3
        { 12, 23, 13, 23, 14, 23, 15, 23, 16, 23, 17, 23, 18, 23, 19, 23, 20,
            23, 21, 23 }, // 4
        { 11, 13, 11, 14, 11, 15 }, // 5
        { 22, 13, 22, 14, 22, 15 }, // 6
        { 11, 18, 11, 19, 11, 20 }, // 7
        { 22, 18, 22, 19, 22, 20 }, // 8
        { 13, 11, 14, 11, 15, 11 }, // 9
        { 18, 11, 19, 11, 20, 11 }, // 10
        { 13, 22, 14, 22, 15, 22 }, // 11
        { 18, 22, 19, 22, 20, 22 }, // 12
        { 17, 13, 17, 14, 17, 15 }, // 13
        { 16, 18, 16, 19, 16, 20 }, // 14
        { 13, 16, 14, 16, 15, 16 }, // 15
        { 18, 17, 19, 17, 20, 17 }, // 16
        { 12, 13, 12, 14, 12, 15, 13, 15, 14, 15, 15, 15, 16, 15 }, // 17
        { 18, 16, 19, 16, 19, 15, 20, 15, 20, 14, 20, 13, 21, 13 }, // 18
        { 13, 21, 13, 20, 13, 19, 14, 19, 15, 19, 15, 18, 15, 17 }, // 19
        { 17, 18, 17, 19, 17, 20, 18, 20, 18, 21, 19, 21, 20, 21 }, // 21
        { 10, 10 }, // 21
    });
    return database.get(chipName);
  }
}
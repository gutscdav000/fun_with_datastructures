/**
 * TO DO: Read through this code carefully because your cache in
 * SequenceAligner will be made up of Result objects. Note that a Result
 * contains a field of type Direction.
 * 
 * A Result represents the result of a Subproblem that is stored
 * in the cache.
 */

public class Result {
  /** 
   * The subproblem score. 
   */

  private int score;          
  
  /**  
   * The direction of the neighboring entry in the cache from which this
   * entry is extended. (See definition of Direction below.)
   */

  private Direction parent;   
  
  /**
   * A flag indicating whether or not this subproblem is along the optimal
   * path. Used by the GUI when showing the path.
   */

  private boolean mark = false;
  
  /**
   * Creates an unmarked result with the given score and no parent.
   */

  public Result(int score) {
    this(score, Direction.NONE);
  }

  /**
   * Creates an umarked result with the given score and parent.
   */

  public Result(int score, Direction parent) {
    this.score = score; 
    this.parent = parent;
  }

  /**
   * Returns the score associated with this result.
   */

  public int getScore() {
    return score;
  }

  /**
   * Returns the parent associated with this result.
   */

  public Direction getParent() {
    return parent;
  }

  /**
   * Marks this result as being along the optimal path.
   */

  public void markPath() {
    mark = true;
  }

  /**
   * Returns true iff this result is known to be along the optimal path.
   */

  public boolean onPath() {
    return mark;
  }

  /**
   * Returns a textual representation of this result.
   */

  public String toString() {
    return String.format("Result[score=%d,parent=%d%s]",
        score, parent, onPath() ? ",*" : "");
  }
}


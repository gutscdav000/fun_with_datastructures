import java.util.Random;

/**
 * TO DO
 *
 * There is much for you to do here.
 *
 * Implement the bestResult(), fillCache(), getResult(), and
 * traceback() methods, in that order. This is the biggest part of
 * this project.
 * 
 * @author David Gutsch
 */

public class SequenceAligner {
  private static Random gen = new Random();

  private String s, t;
  private int n, m;
  private String alignedS, alignedT;
  private Result[][] cache;
  private Judge judge;

  /**
   * Generates a pair of random DNA strands, where s is of length n and
   * t has some length between n/2 and 3n/2, and aligns them using the
   * default judge.
   */

  public SequenceAligner(int n) {
    this(randomDNA(n),
        randomDNA(n - gen.nextInt(n / 2) *
            (gen.nextInt(2) * 2 - 1)));
  }

  /**
   * Aligns the given strands using the default judge.
   */

  public SequenceAligner(String s, String t) {
    this(s, t, new Judge());
  }
  
  /**
   * Aligns the given strands using the specified judge.
   */

  public SequenceAligner(String s, String t, Judge judge) {
    // Pads s and t with blanks on left to synchronize indices on the cache
    s = s.trim().toUpperCase();
    t = t.trim().toUpperCase();
    this.s = " " + s;
    this.t = " " + t;
    this.judge = judge;
    n = s.length();
    m = t.length();
    cache = new Result[n + 1][m + 1];
    fillCache();
    //printIsCache();
    traceback();
  }

  /**
   * Returns the s strand. Note: the strand is padded on left with one blank.
   */

  public String getS() {
    return s;
  }

  /**
   * Returns the t strand. Note: the strand is padded on left with one blank.
   */

  public String getT() {
    return t;
  }
  
  /**
   * Returns the judge associated with this pair.
   */

  public Judge getJudge() {
    return judge;
  }
  
  /**
   * Returns the aligned version of the s strand. Note: the aligned strand
   * is not padded with a blank.
   */

  public String getAlignedS() {
    return alignedS;
  }

  /**
   * Returns the aligned version of the t strand. Note: the aligned strand
   * is not padded with a blank.
   */

  public String getAlignedT() {
    return alignedT;
  }

  /**
   * TO DO
   *
   * Returns the Result corresponding to the biggest payoff among the scores
   * for the three different directions.
   *
   * Tiebreaking Rule: So that your code will identify the same alignment
   * as is expected in Testing, we establish the following preferred order
   * of operations: M (diag), I (left), D (up). This only applies when you
   * are picking the operation with the biggest payoff and two or more
   * operations have the same max score.
   */

  public static Result bestResult(int diag, int left, int up) {
    //set arbitrary max
    int max = diag;
    Direction tmp = Direction.DIAGONAL;

    //is left greater
    if(left > diag) {
      max = left;
      tmp = Direction.LEFT;
    }
    // is up greater than the other 2
    if(up > left && up > diag) {
      max = up;
      tmp = Direction.UP;
    }

    // return new result
    return new Result(max, tmp);
  }

  /**
   *  TO DO: Solve the alignment problem using bottom-up dynamic programming
   *  algorithm described in lecture. When you're done, cache[i][j] will hold
   *  the result of solving the alignment problem for the first i characters
   *  in s and the first j characters in t.
   *
   *  Your algorithm must run in O(n * m) time, where n is the length of s
   *  and m is the length of t.
   *
   */

  private void fillCache() {
    //cache = new Result[n][m];

    //
    int cost = judge.getGapCost();
    cache[0][0] = new Result(0, Direction.NONE); //origin
    cache[0][0].markPath(); // can't forget to mark the first one

    // fill row 0
    for(int i = 1; i <= m; i++)
      cache[0][i] = new Result(i * cost, Direction.LEFT);

    // fill col 0
    for(int y = 1; y <= n; y++)
      cache[y][0] = new Result(y * cost, Direction.UP);

    // fill the rest of the scores
    // + diag: add score of this index to previous,
    // + left: add cost to score (e.g. subtract cost)
    // + up:   add cost to score (e.g. subtract cost)
    for(int i = 1; i <= n; i++)
      for(int j = 1; j <= m; j++)
        cache[i][j] = bestResult(cache[i - 1][j - 1].getScore() + judge.score(s.charAt(i), t.charAt(j)), cache[i][j - 1].getScore() + cost, cache[i - 1][j].getScore() + cost);
  }


  private void printIsCache() {
    for(int i = 0; i < cache.length; i ++) {
      for (int j = 0; j < cache[0].length; j++) {
        System.out.print(Boolean.toString(cache[i][j].onPath()) + " ");
      }
      System.out.println();
    }

  }
  
  /**
   * TO DO: Returns the result of solving the alignment problem for the
   * first i characters in s and the first j characters in t. You can
   * find the result in O(1) time by looking in your cache.
   */

  public Result getResult(int i, int j) {
    return cache[i][j];
  }
  
  /**
   * TO DO: Mark the path by tracing back through parent pointers, starting
   * with the Result in the lower right corner of the cache. Run Result.markPath()
   * on each Result along the path. The GUI will highlight all such marked cells
   * when you check 'Show path'. As you're tracing back along the path, build 
   * the aligned strings in alignedS and alignedT (using Constants.GAP_CHAR
   * to denote a gap in the strand).
   * 
   * Your algorithm must run in O(n + m) time, where n is the length of s
   * and m is the length of t.
   */

  private void traceback() {
    int i = n, j = m;

    // create new string builder to build reverse of DNA sequence path
    StringBuilder newS= new StringBuilder();
    StringBuilder newT = new StringBuilder();
    // grab the result containing final score
    Result hold = cache[n][m];

    // trace through sequence until direction is non (i.e. we've reached the origin of the cache)
    while(!hold.getParent().equals(Direction.NONE)) {

      // diagonal path is chosen
      if(hold.getParent().equals(Direction.DIAGONAL)) {
        //System.out.println("diag");
        newT.append(t.charAt(j)); // horizontal
        newS.append(s.charAt(i)); // vertical
        cache[i][j].markPath();
        hold = cache[--i][--j];
      }

      // left path is chosen
      else if(hold.getParent().equals(Direction.LEFT)) {
        //System.out.println("left");
        newT.append(t.charAt(j));
        newS.append(Constants.GAP_CHAR);
        cache[i][j].markPath();
        hold = cache[i][--j];
      }

      // up path is chosen
      else if(hold.getParent().equals(Direction.UP)) {
        //System.out.println("up");
        newT.append(Constants.GAP_CHAR);
        newS.append(s.charAt(i));
        cache[i][j].markPath();
        hold = cache[--i][j];
      }
    }

    // reverse & convert string builder to string and return
    alignedS = newS.reverse().toString();
    alignedT = newT.reverse().toString();
  }
  /**
   * Returns true iff these strands are seemingly aligned.
   */

  public boolean isAligned() {
    return alignedS != null && alignedT != null &&
        alignedS.length() == alignedT.length();
  }
  
  /**
   * Returns the score associated with the current alignment.
   */

  public int getScore() {
    if (isAligned())
      return judge.score(alignedS, alignedT);
    return 0;
  }

  /**
   * Returns a nice textual version of this alignment.
   */

  public String toString() {
    if (!isAligned())
      return "[s=" + s.trim() + ",t=" + t.trim() + "]";
    final char GAP_SYM = '.', MATCH_SYM = '|', MISMATCH_SYM = ':';
    StringBuilder ans = new StringBuilder();
    ans.append(alignedS).append('\n');
    int n = alignedS.length();
    for (int i = 0; i < n; i++)
      if (alignedS.charAt(i) == Constants.GAP_CHAR ||
          alignedT.charAt(i) == Constants.GAP_CHAR)
        ans.append(GAP_SYM);
      else if (alignedS.charAt(i) == alignedT.charAt(i))
        ans.append(MATCH_SYM);
      else
        ans.append(MISMATCH_SYM);
    ans.append('\n').append(alignedT).append('\n').
        append("score = ").append(getScore());
    return ans.toString();
  }

  /**
   * Returns a DNA strand of length n with randomly selected nucleotides.
   */

  public static String randomDNA(int n) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++)
      sb.append("ACGT".charAt(gen.nextInt(4)));
    return sb.toString();
  }

}

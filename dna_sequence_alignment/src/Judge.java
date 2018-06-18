/**
 * This class is used to score alignments. 
 * 
 * TODO: You are to implement the two score() methods.
 *  
 * @author David Gutsch
 */

public class Judge {

  public static final int DEFAULT_MATCH_COST = 2;
  public static final int DEFAULT_MISMATCH_COST = -2;
  public static final int DEFAULT_GAP_COST = -1;
  
  private int matchCost, mismatchCost, gapCost;
  
  /**
   * Creates the default judge.
   */

  public Judge() {
    this(DEFAULT_MATCH_COST, DEFAULT_MISMATCH_COST, DEFAULT_GAP_COST);
  }
  
  /**
   * Creates a judge using the specified costs.
   */

  public Judge(int matchCost, int mismatchCost, int gapCost) {
    this.matchCost = matchCost;
    this.mismatchCost = mismatchCost;
    this.gapCost = gapCost;
  }
  
  /**
   * Returns the gap cost used by this judge.
   */

  public int getGapCost() {
    return gapCost;
  }
  
  /**
   * Returns the match cost used by this judge.
   */

  public int getMatchCost() {
    return matchCost;
  }
  
  /**
   * Returns the mismatch cost used by this judge.
   */

  public int getMismatchCost() {
    return mismatchCost;
  }
  
  /**
   * TO DO
   * 
   * Returns the score associated with the two characters.
   */

  public int score(char a, char b) {
    if(a == Constants.GAP_CHAR || b == Constants.GAP_CHAR)
      return gapCost;
    else if(a == b)
      return matchCost;
    else
      return mismatchCost;
  }
  
  /**
   * TO DO
   * 
   * Returns the score associated with the two strings.
   */

  public int score(String s, String t) {
    int n = s.length() >= t.length()? s.length(): t.length();
    int sum = 0;
    for(int i = 0; i < n; i++)
      sum += score(s.charAt(i), t.charAt(i));

    return sum;
  }
}

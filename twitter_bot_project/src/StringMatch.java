/**
 * TODO #3
 *
 * There is much for you to do here.
 *
 * When you are done here, go to MatchBot.
 */

public class StringMatch {

  /**
   *
   *
   * Returns the result of running the naive algorithm to match
   * pattern in text.  A result is an association of two integers: the
   * position in the text of the match (possibly -1) and the exact
   * number of key comparisons performed to find the match.
   */

  public static Assoc<Integer, Integer> matchNaive(String pattern,
                                                   String text) {
    int n = text.length(), m = pattern.length(), i = 0, j = 0, k = 0, compCost = 0;

    if(m == 0)
      return new Assoc<Integer, Integer>(0,compCost);
    if(n == 0)
      return new Assoc<Integer, Integer>(-1, compCost);

    while(k <= n - m) {
      compCost++;
      if(pattern.charAt(i) == text.charAt(j)) {
        // keep matching
        i++; j++;
        if(i == m)
          return new Assoc<Integer, Integer>(k, compCost);
      } else {
        // slide pattern forward and start over
        k++;
        i = 0;
        j = k;
      }

    }
    return new Assoc<Integer, Integer>(-1, compCost);
  }
  
  /**
   *
   * Populates flink with the failure links for the KMP machine
   * associated with the given pattern, and returns the cost in terms
   * of the number of character comparisons.
   */

  public static int buildKMP(String pattern, int[] flink) {
    int m = pattern.length();

    flink[0] = -1;
    if(m == 0) {
      return 0;
    }

    /*flink[1] = 0;
    if(m == 1)
      return 0;*/

    int i = 2;
    int j = -1;
    int compCost = 0;

    while (i <= m) {
      //compute flink(i) based on earlier flinks
      j = flink[i - 1];
      while(j != -1) {
        compCost++;
        if(pattern.charAt(j) == pattern.charAt(i - 1)) {
          break;
        }
        j = flink[j];
      }

      flink[i] = j + 1;
      i++;

    }
    return compCost;
  }
  
  /**
   * TODO
   *
   * Returns the result of running the KMP machine specified by flink
   * (built for the given pattern) on the text.
   */

  public static Assoc<Integer, Integer> runKMP(String pattern,
                                               String text,
                                               int[] flink) {
    int n = text.length(), m = pattern.length(), compCost = 0;

    if (m == 0)
      return new Assoc<Integer, Integer>(0, compCost);
    if (n == 0)
      return new Assoc<Integer, Integer>(-1, compCost);

    int state = -1, j = -1;
    char c = text.charAt(0);

    while(true) {
      if(state == -1) {
        state++;
        j++;

        if(j == n)
          return new Assoc<Integer, Integer>(-1, compCost);
        c = text.charAt(j);

      }
      compCost++;
      if(c == pattern.charAt(state)) {
        state++;
        if(state == m)
          return new Assoc<Integer, Integer>(j - m + 1, compCost);

        j++;
        if(j == n)
          return new Assoc<Integer, Integer>(-1, compCost);

        c = text.charAt(j);

      }
      else {
        state = flink[state];
      }


    }
  }
  
  /**
   * Returns the result of running the KMP algorithm to match pattern in text.
   * The number of comparisons includes the cost of building the machine from
   * the pattern.
   */

  public static Assoc<Integer, Integer> matchKMP(String pattern, String text) {
    int m = pattern.length();
    int[] flink = new int[m + 1];
    int comps = buildKMP(pattern, flink);
    Assoc<Integer, Integer> ans = runKMP(pattern, text, flink);
    return new Assoc<>(ans.key, comps + ans.value);
  }
  
  /**
   *
   * Populates delta1 with the shift values associated with each character in
   * the alphabet. Assume delta1 is large enough to hold any ASCII value.
   */

  public static void buildDelta1(String pattern, int[] delta1) {
    int m = pattern.length();

    // fill delta array with m value
    for(int i = 0; i < delta1.length; i++)
      delta1[i] = m;

    // then fill m - 1 - i
    for(int i = 0; i < pattern.length(); i++)
        delta1[pattern.charAt(i)] = m - 1 - i;

  }

  /**
   *
   * Returns the result of running the simplified Boyer-Moore algorithm using
   * the delta1 table from the pre-processing phase.
   */

  public static Assoc<Integer, Integer> runBoyerMoore(String pattern,
                                                      String text,
                                                      int[] delta1) {
    int m = pattern.length(), n = text.length(), i = m - 1, k = 0, j = m - 1, compCost = 0;
    char c;

      if (m == 0)
          return new Assoc<Integer, Integer>(0, compCost);
      if (n == 0)
          return new Assoc<Integer, Integer>(-1, compCost);



    // search for matches
    while(j <= n - 1) {
        c = text.charAt(j - k);
        i = m - k - 1;
        compCost++;
        if(pattern.charAt(i) == c) {
            // we found a match
            if(i == 0)
                return new Assoc<Integer, Integer>(j - m + 1, compCost);

            k++;
            //i = j - k + 1;


        } else {
            if(((int)c) > delta1.length)
                c = (char)m;
            else {
                c = (char)delta1[text.charAt(j - k)];
            }


            //j = j + Math.max(1, delta1[c] - k);
            j = j + Math.max(1, ((int)c) - k);
            k = 0;
        }

    }

    return new Assoc<Integer, Integer>(-1, compCost);

  }


  
  /**
   * Returns the result of running the simplified Boyer-Moore
   * algorithm to match pattern in text.
   */

  public static Assoc<Integer, Integer> matchBoyerMoore(String pattern,
                                                        String text) {
    int[] delta1 = new int[Constants.SIGMA_SIZE];
    buildDelta1(pattern, delta1);
    return runBoyerMoore(pattern, text, delta1);
  }
  
}

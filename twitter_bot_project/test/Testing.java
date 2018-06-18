import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.junit.Test;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

/**
 * TODO: Write your own tests!!!
 */

public class Testing {
  private static Random random = new Random();
  private static String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

  @Test
  public void testRank() {
    PopularityBot billyBot = new PopularityBot();
    for (String lyric : new String[]{
        "We didn't start the fire",
        "It was always burning",
        "Since the world's been turning",
        "We didn't start the fire",
        "No we didn't light it",
        "But we tried to fight it",
    })
        billyBot.tweets.add(lyric);
    billyBot.split();
    billyBot.rank();
    assertEquals(3, billyBot.vocab.size());
    assertEquals("didnt", billyBot.vocab.get(0).key);
    assertEquals("fire", billyBot.vocab.get(1).key);
    assertEquals("start", billyBot.vocab.get(2).key);
    assertEquals(Integer.valueOf(3), billyBot.vocab.get(0).value);
    assertEquals(Integer.valueOf(2), billyBot.vocab.get(1).value);
    assertEquals(Integer.valueOf(2), billyBot.vocab.get(2).value);
  }

  @Test
  public void testNaive() {
    String[] pats = new String[] {
        "AAAA",
        "BAAA",
        "AAAB",
        "AAAC",
        "ABAB",
    };
    String text = "AAAAAAAAABAAAAAAAAAB";
    assertEquals(20, text.length());
    Integer[][] results = new Integer[][] {
        { 0, 4 },
        { 9, 13 },
        { 6, 28 },
        { -1, 62 },
        { -1, 35 },
    };
    int i = 0;
    for (String pat : pats) {
      Assoc<Integer, Integer> res = StringMatch.matchNaive(pat, text);
      assertEquals(results[i][0], res.key);
      assertEquals(results[i][1], res.value);
      i++;
    }
  }

  @Test 
  public void smallGods() { // Shout out to Terry Pratchett.
    String[] pats = new String[] {
        "",
        "A",
        "AB",
        "AA",
        "AAAA",
        "BAAA",
        "AAAB",
        "AAAC",
        "ABAB",
        "ABCD",
        "ABBA",
        "AABC",
        "ABAAB",
        "AABAACAABABA",
        "ABRACADABRA",
    };
    int[][] flinks = new int[][] {
        { -1 },
        { -1, 0 },
        { -1, 0, 0 },
        { -1, 0, 1 },
        { -1, 0, 1, 2, 3 },
        { -1, 0, 0, 0, 0 },
        { -1, 0, 1, 2, 0 },
        { -1, 0, 1, 2, 0 },
        { -1, 0, 0, 1, 2 },
        { -1, 0, 0, 0, 0 },
        { -1, 0, 0, 0, 1 },
        { -1, 0, 1, 0, 0 },
        { -1, 0, 0, 1, 1, 2 },
        { -1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1 },
        { -1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4 },
    };                                        //*
    int[] comps = new int[] { 0, 0, 1, 1, 3, 3, 5, 5, 3, 3, 3, 4, 5, 16, 12 };
    int i = 0;
    for (String pat : pats) {
      int[] flink = new int[pat.length() + 1];
      assertEquals(comps[i], StringMatch.buildKMP(pat, flink));
      assertArrayEquals(flinks[i], flink);
      i++;
    }
  }

  @Test
  public void lec13bKMP() {
    String[] pats = new String[] {
        "AABC",
        "ABCDE",
        "AABAACAABABA",
        "ABRACADABRA",
    };
    int[][] flinks = new int[][] {
        { -1, 0, 1, 0, 0 },
        { -1, 0, 0, 0, 0, 0 },
        { -1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1 },
        { -1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4 },
    };
    String text = "AAAAAABRACADABAAAAAAAAAAAAAAAAAAAAAAA" +
        "BCAAAAAAAAAAABAABAAAAAAAAAAAAAAA";
    Integer[][] results = new Integer[][]{
        { 35, 68 },
        { -1, 128 },
        { -1, 123 },
        { -1, 126 },
    };
    int i = 0;
    for (String pat : pats) {
      Assoc<Integer, Integer> res = StringMatch.runKMP(pat, text, flinks[i]);
      assertEquals(results[i][0], res.key);
      assertEquals(results[i][1], res.value);
      i++;
    }
  }

  @Test
  public void vanillaBuildDeltaTest() {

    String pat1 = "AADACA";
    int[] match1 = {0, 6, 1, 3};
    int[] delta1 = new int[Constants.SIGMA_SIZE];
    StringMatch.buildDelta1(pat1, delta1);

    assertEquals(delta1['A'], 0);
    assertEquals(delta1['B'], 6);
    assertEquals(delta1['C'], 1);
    assertEquals(delta1['D'], 3);

    String pat2 = "FEDCBA";
    int[] match2 = {0, 1, 2, 3, 4, 5};
    int[] delta2 = new int[Constants.SIGMA_SIZE];
    StringMatch.buildDelta1(pat2, delta2);

    assertEquals(delta2['A'], 0);
    assertEquals(delta2['B'], 1);
    assertEquals(delta2['C'], 2);
    assertEquals(delta2['D'], 3);
    assertEquals(delta2['E'], 4);
    assertEquals(delta2['F'], 5);



    String pat4 = "ABBEBB";
    int[] match4 = {5, 0, 6, 6, 2};
    int[] delta4 = new int[Constants.SIGMA_SIZE];
    StringMatch.buildDelta1(pat4, delta4);

    assertEquals(delta4['A'], 5);
    assertEquals(delta4['B'], 0);
    assertEquals(delta4['C'], 6);
    assertEquals(delta4['D'], 6);
    assertEquals(delta4['E'], 2);


    String pat5 = "BDCDDCD";
    int[] match5 = {7, 6, 1, 0, 7};
    int[] delta5 = new int[Constants.SIGMA_SIZE];
    StringMatch.buildDelta1(pat5, delta5);

    assertEquals(delta5['A'], 7);
    assertEquals(delta5['B'], 6);
    assertEquals(delta5['C'], 1);
    assertEquals(delta5['D'], 0);
    assertEquals(delta5['E'], 7);

    String pat6 = "ABBCBB";
    int[] match6 = {5, 0, 2, 6, 6};
    int[] delta6 = new int[Constants.SIGMA_SIZE];
    StringMatch.buildDelta1(pat6, delta6);

    assertEquals(delta6['A'], 5);
    assertEquals(delta6['B'], 0);
    assertEquals(delta6['C'], 2);
    assertEquals(delta6['D'], 6);
    assertEquals(delta6['E'], 6);

  }

  @Test
  public void simpleBoyerMooreRun() {

    String pat1 = "AADACA";
    String text1 = "BAACAADACACACAA";
    Assoc<Integer, Integer> ans = StringMatch.matchBoyerMoore(pat1, text1);
    //System.out.println(ans.key);
    assertTrue(ans.key == 4);

    String pat2 = "AB";
    String text2 = "ABBAABBAAB";
    ans = StringMatch.matchBoyerMoore(pat2, text2);
    //System.out.println(ans.key);
    assertTrue(ans.key == 0);

    String text3 = "CDBAFDCAB";
    ans = StringMatch.matchBoyerMoore(pat2, text3);
    //System.out.println(ans.key);
    assertTrue(ans.key == 7);


    String text4 = "XXXXXXXXXXXXXXXXXXXXXXX";
    String pat3 = "y";
    ans = StringMatch.matchBoyerMoore(pat3, text4);
    //System.out.println(ans.key);
    assertTrue(ans.key == -1);

    text4 = "alksdjfalksfj";
    pat3 = "";
    ans = StringMatch.matchBoyerMoore(pat3, text4);
    assertTrue(ans.key == 0);



    text4 = "";
    pat3 = "SoyElFuegoQArdaEnTuPiel";
    ans = StringMatch.matchBoyerMoore(pat3, text4);
    assertTrue(ans.key == -1);
  }


  @Test
  public void testFlinks() {
    String pattern = "AABAACAABABA";
    int m = pattern.length();
    int[] flink = new int[m + 1];
    StringMatch.buildKMP(pattern, flink);
    // System.out.println(java.util.Arrays.toString(flink));
    assertArrayEquals(flink, new int[] {
        -1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1
    });
    assertTrue(verifyMachine(pattern, flink));
  }

  /**
   * The following tests may be useful when writing your REPORT.
   * Remove the // in front of @Test to run.
   */

  @Test
  public void testEmpty() {
    System.out.println("testEmpty");
    match("", "");
    match("", "ab");
    System.out.println();
  }

  @Test
  public void testOneChar() {
    System.out.println("testOneChar");
    match("a", "a");
    match("a", "b");
    System.out.println();
  }

  @Test
  public void testRepeat() {
    System.out.println("testRepeat");
    match("aaa", "aaaaa");
    match("aaa", "abaaba");
    match("abab", "abacababc");
    match("abab", "babacaba");
    System.out.println();
  }

  @Test
  public void testPartialRepeat() {
    System.out.println("testPartialRepeat");
    match("aaacaaaaac", "aaacacaacaaacaaaacaaaaac");
    match("ababcababdabababcababdaba", "ababcababdabababcababdaba");
    System.out.println();
  }

  @Test
  public void testRandomly() {
    System.out.println("testRandomly");
    for (int i = 0; i < 100; i++) {
      String pattern = makeRandomPattern();
      for (int j = 0; j < 100; j++) {
        String text = makeRandomText(pattern);
        match(pattern, text);
      }
    }
    System.out.println();
  }

  /* Helper functions */

  private static String makeRandomPattern() {
    StringBuilder sb = new StringBuilder();
    int steps = random.nextInt(10) + 1;
    for (int i = 0; i < steps; i++) {
      if (sb.length() == 0 || random.nextBoolean()) {  // Add literal
        int len = random.nextInt(5) + 1;
        for (int j = 0; j < len; j++)
          sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
      } 
      else {  // Repeat prefix
        int len = random.nextInt(sb.length()) + 1;
        int reps = random.nextInt(3) + 1;
        if (sb.length() + len * reps > 1000)
          break;
        for (int j = 0; j < reps; j++)
          sb.append(sb.substring(0, len));
      }
    }
    return sb.toString();
  }

  private static String makeRandomText(String pattern) {
    StringBuilder sb = new StringBuilder();
    int steps = random.nextInt(100);
    for (int i = 0; i < steps && sb.length() < 10000; i++) {
      if (random.nextDouble() < 0.7) {  // Add prefix of pattern
        int len = random.nextInt(pattern.length()) + 1;
        sb.append(pattern.substring(0, len));
      } 
      else {  // Add literal
        int len = random.nextInt(30) + 1;
        for (int j = 0; j < len; j++)
          sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
      }
    }
    return sb.toString();
  }

  private static void match(String pattern, String text) {
    // run all three algorithms and test for correctness
    Assoc<Integer, Integer> ansNaive = StringMatch.matchNaive(pattern, text);
    Integer expected = text.indexOf(pattern);
    assertEquals(expected, ansNaive.key);
    Assoc<Integer, Integer> ansKMP = StringMatch.matchKMP(pattern, text);
    assertEquals(expected, ansKMP.key);
    Assoc<Integer, Integer> ansBoyerMoore =
        StringMatch.matchBoyerMoore(pattern, text);
    assertEquals(expected, ansBoyerMoore.key);
    System.out.println(String.format("%5d %5d %5d : %s",
        ansNaive.value, ansKMP.value, ansBoyerMoore.value,
        (ansNaive.value < ansKMP.value &&
            ansNaive.value < ansBoyerMoore.value) ?
            "Naive" :
              (ansKMP.value < ansNaive.value &&
                  ansKMP.value < ansBoyerMoore.value) ?
                  "KMP" : "Boyer-Moore"));
  }

  //--------- For Debugging ---------------
  public static boolean verifyMachine(String pattern, int[] flink) {
    int m = pattern.length();
    if (flink.length != pattern.length() + 1) 
      return false;  // bad length
    if (flink[0] != -1)
      return false;
    for (int i = 2; i < m; i++) {
      if (flink[i] < 0 || flink[i] >= i) 
        return false;  // link out of range
      // Search for the nearest state whose label is a suffix of i's label
      String iLabel = pattern.substring(0, i);  
      int j = i - 1;
      while (j >= 0 && !iLabel.endsWith(pattern.substring(0, j))) 
        j--;
      if (flink[i] != j) 
        return false;  // fails to the wrong state
    }
    return true;
  }
}

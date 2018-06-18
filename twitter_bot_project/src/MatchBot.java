/**
 * TODO #4
 *
 * Design and run a variety of experiments in MatchBot.main() and then
 * summarize your results below in this comment. Which of the three algorithms
 * works best on the Twitter dataset? What about other kinds of data?
 * Hypothesize about the reasons why one algorithm is better suited to a
 * particular type of data. Support your conclusions with evidence.
 *
 * ==========================================================================
 * REPORT:  For all of my tests I found that the Boyer - Moore algorithm was fastest, followed by the Naive
 *          algorithm which I found particularly interesting, and finally followed by the KMP algorithm. it seems
 *          like the boyer-moore algorithm has roughly a quarter the amount of number of comparisons compared to the
 *          other two algorithms. This part of the analysis was the most obvious due to how many characters may be
 *          potentially skipped while traversing the string. I find it interesting to see that naive is faster than
 *          KMP in all of the test I ran. I think this is the case because of the cost of building the state machine.
 *          If the size of the pattern is non trivial then there are many connections between nodes of the string that
 *          must be accounted for making the shear number of comparisons better with the naive algorithm.
 *
 *
 *
 *           *** Donald test  (normal size): ***naive comps = 245101, KMP comps = 254024, Boyer-Moore comps = 49564
 *           *** CNN breaking news (normal size): ***naive comps = 277748, KMP comps = 285696, Boyer-Moore comps = 63001
 *           *** Donald test  (small size): **: ***naive comps = 31169, KMP comps = 32332, Boyer-Moore comps = 6364
 *           *** CNN breaking news (small size): ***naive comps = 35063, KMP comps = 36034, Boyer-Moore comps = 8077
 *           *** CNN breaking news (large size): ***naive comps = 448268, KMP comps = 461274, Boyer-Moore comps = 101369
 *           *** Donald test  (large size): **: ***naive comps = 392345, KMP comps = 406533, Boyer-Moore comps = 79115
 *
 */

public class MatchBot extends TwitterBot {

  /**
   * Constructs a MatchBot to operate on the last numTweets of the given user.
   */

  public MatchBot(String user, int numTweets) {
    super(user, numTweets);
  }
  
  /**
   * TODO
   *
   * Employs the KMP string matching algorithm to add all tweets containing 
   * the given pattern to the provided list. Returns the total number of 
   * character comparisons performed.
   */

  public int searchTweetsKMP(String pattern, List<String> ans) {
    int compCost = 0;
    Assoc<Integer, Integer> holder;

    int[] flinks = new int[pattern.length() + 1];
    int flinkcost = StringMatch.buildKMP(pattern, flinks);
    compCost = flinkcost;

    for(String tweet : tweets) {

      //holder = StringMatch.matchKMP(pattern, tweet);
      holder = StringMatch.matchKMP(pattern.toLowerCase(), tweet.toLowerCase());
      compCost += holder.value - flinkcost;

      if(holder.key != -1)
        ans.add(tweet);
    }
    return compCost;
  }
  
  /**
   * TODO
   *
   * Employs the naive string matching algorithm to add all tweets containing 
   * the given pattern to the provided list. Returns the total number of 
   * character comparisons performed.
   */

  public int searchTweetsNaive(String pattern, List<String> ans) {
    int compCost = 0;
    Assoc<Integer, Integer> holder;

    for(String tweet : tweets) {

      holder = StringMatch.matchNaive(pattern.toLowerCase(), tweet.toLowerCase());
      compCost += holder.value;

      if(holder.key != -1)
        ans.add(tweet);
    }
    return compCost;
  }

  /**
   * Now, do something similar to the above, but for the Boyer-Moore
   * matching algorithm. Use your results to write your REPORT, as
   * described in the comment at the top of this file.
   */

  public int searchTweetsBoyerMoore(String pattern, List<String> ans) {
    int compCost = 0;
    Assoc<Integer, Integer> holder;

    for(String tweet : tweets) {

      holder = StringMatch.matchBoyerMoore(pattern.toLowerCase(), tweet.toLowerCase());
      compCost += holder.value;

      if(holder.key != -1)
        ans.add(tweet);
    }
    return compCost;

  }

  /**
   * TODO: Write your experiments here!
   */

  public static void main(String... args) {
    String handle = "realDonaldTrump", pattern = "mexico";
    System.out.println("Starting");
    MatchBot bot = new MatchBot(handle, 2000);
    System.out.println("made bot");
    // search all tweets for the pattern
    List<String> ansNaive = new DoublyLinkedList<>();
    System.out.println("naive start");

    // test 1
    int compsNaive = bot.searchTweetsNaive(pattern, ansNaive); 

    List<String> ansKMP = new DoublyLinkedList<>();
    System.out.println("kmp start");
    int compsKMP = bot.searchTweetsKMP(pattern, ansKMP);

    List<String> ansBM = new DoublyLinkedList<>();
    System.out.println("Boyer - Moore start");
    int compsBM = bot.searchTweetsBoyerMoore(pattern, ansBM);


    
    System.out.println("*** Donald test  (normal size): ***naive comps = " + compsNaive +
        ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBM);

    /*for (int i = 0; i < ansKMP.size(); i++) {
      String tweet = ansKMP.get(i);
      assert tweet.equals(ansNaive.get(i));
      System.out.println(i++ + ". " + tweet);
      System.out.println(pattern + " appears at index " + 
          tweet.toLowerCase().indexOf(pattern.toLowerCase()));
    }*/

    // test 2
    handle = "cnnbrk";
    pattern = "syria";
    bot = new MatchBot(handle, 2000);
    compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
    compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
    compsBM = bot.searchTweetsBoyerMoore(pattern, ansBM);

    System.out.println("*** CNN breaking news (normal size): ***naive comps = " + compsNaive +
            ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBM);


    //test 3
    handle = "realDonaldTrump";
    pattern = "mexico";
    bot = new MatchBot(handle, 250);
    compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
    compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
    compsBM = bot.searchTweetsBoyerMoore(pattern, ansBM);

    System.out.println("*** Donald test  (small size): **: ***naive comps = " + compsNaive +
            ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBM);


    // test 4
    handle = "cnnbrk";
    pattern = "syria";
    bot = new MatchBot(handle, 250);
    compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
    compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
    compsBM = bot.searchTweetsBoyerMoore(pattern, ansBM);

    System.out.println("*** CNN breaking news (small size): ***naive comps = " + compsNaive +
            ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBM);


    // test 5
    handle = "cnnbrk";
    pattern = "syria";
    bot = new MatchBot(handle, 4000);
    compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
    compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
    compsBM = bot.searchTweetsBoyerMoore(pattern, ansBM);

    System.out.println("*** CNN breaking news (large size): ***naive comps = " + compsNaive +
            ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBM);

    // test 6
    handle = "realDonaldTrump";
    pattern = "mexico";
    bot = new MatchBot(handle, 4000);
    compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
    compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
    compsBM = bot.searchTweetsBoyerMoore(pattern, ansBM);

    System.out.println("*** Donald test  (large size): **: ***naive comps = " + compsNaive +
            ", KMP comps = " + compsKMP + ", Boyer-Moore comps = " + compsBM);

  }
}

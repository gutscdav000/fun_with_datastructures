import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 * There's nothing for you to do here.
 *
 * If needed, here's how to add the twitter API to your classpath:
 *   File > Project Structure...
 *   Project Settings > Modules > Dependencies
 *   Click on the "+" sign > JARs or directories...
 *   Select lib/twitter4j-core-4.0.4.jar
 *   Click OK.
 */

public interface Constants {
  public static final String TITLE = "Project 6: Tweet Tweet";
  public static final String DATA_DIR = "data";
  public static final String HASHTAG = "#C343Rocks";
  public static final int PAGE_SIZE = 20;
  public static final String PUNCTUATION = ".,'?!:;\"(){}^{}<>-“”’~";
  public static final String BORING_WORDS = DATA_DIR + "/boringWords.txt";
  // Assume alphabet consists of the basic ASCII code.
  public static final int SIGMA_SIZE = 128; 
  // The factory instance is re-useable and thread safe.
  public static Twitter TWITTER = TwitterFactory.getSingleton();
}

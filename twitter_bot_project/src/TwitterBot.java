import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * There's nothing for you to do here.
 *
 * This class is the super class for PopularityBot.
 */

public class TwitterBot {
  String user;
  List<String> tweets = new DoublyLinkedList<>();
  int numTweets;

  /**
   * Default constructor.
   */

  public TwitterBot() { }

  /**
   * Fetches the requested number of tweets from the given user.
   */

  public TwitterBot(String user, int numTweets) {
    this.user = user;
    this.numTweets = numTweets;
    if (numTweets > 0)
      fetch();
  }
  
  /**
   * Returns the user associated with this bot.
   */

  public String getUser() {
    return user;
  }
  
  /**
   * Returns the number of tweets retrieved from Twitter for the user by this
   * bot.
   */

  public int getNumTweets() {
    return tweets.size();
  }
  
  /** 
   * Fetches the most recent tweets from the user and stores them in tweets. 
   * For debugging purposes, the retrieved tweet data is also written to a file.
   */

  private void fetch() {
    try {
      String filename = Constants.DATA_DIR + "/" + user + "_tweets.txt";
      PrintStream out = new PrintStream(new FileOutputStream(filename));
      Paging page = new Paging(1, Constants.PAGE_SIZE);
      int n = (int) Math.ceil((double) numTweets / Constants.PAGE_SIZE);
      for (int p = 1; p <= n; p++) {
        page.setPage(p);
        for (Status status : Constants.TWITTER.getUserTimeline(user, page))
          tweets.add(status.getText());
      }     
      int numberTweets = tweets.size();
      out.println("Number of tweets = " + numberTweets);
      int i = 1;
      for (String tweet : tweets) 
        out.println(i++ + ".  " + tweet);
      out.close();
    }
    catch (IOException e) {
      System.out.println("Problem creating tweet file for " + user);
    }
    catch (TwitterException e) {
      System.out.println("Unable to retrieve tweets from Twitter for " +
          user + " at this time.");
      System.out.println(e.getMessage());
    }
  }   
}

import twitter4j.Query;
import twitter4j.GeoLocation;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * TODO #1
 *
 * Follow the instructions in the Canvas description of the assignment
 * to create your Twitter account and add your credentials to the
 * twitter4j.properties file included in the starter code.
 *
 * Enter your twitter username here: gucheman
 *
 * Follow the instructions in the Canvas description of the assignment
 * to update and run this class as an application.
 *
 * Copy your implementation of DoublyLinkedList from lab9 into the src
 * folder for this project. If you haven't already done so, modify
 * your List interface to include the sort() method. You will also
 * need to copy your AList and Assoc classes into the src folder.
 *
 * When you are done here, go to PopularityBot.
 *
 */

public class Driver {

  /** 
   * Tweets out the given message when forReal is true.
   */

  public static void sendTweet(String message, boolean forReal) {
    message += " " + Constants.HASHTAG;
    if (!forReal)
      System.out.println(String.format("Tweet = %s", message));
    else try {
      Constants.TWITTER.updateStatus(message);
    }
    catch (TwitterException e) {
      System.out.println("Unable to tweet at this time.");
      System.out.println(e.getMessage());
    }
  }

  /** 
   * This method shows what people have tweeted about the subject this month
   * if forReal is true.
   */

  public static void tweetsAbout(String subject, boolean forReal) {
    if (!forReal) 
      System.out.println(String.format("tweetsAbout(\"%s\", %b);",
          subject, forReal));
    else {
      Query query = new Query(subject);
      // This will limit the number of responses to 100.
      query.setCount(100);  
      // Could limit the responses to a geographical location, if we wanted.
      // query.setGeoCode(new GeoLocation(39.22031, -86.45824),
      //                                  50, Query.MILES);
      query.setSince("2018-4-1");
      try {
        QueryResult result = Constants.TWITTER.search(query);
        System.out.println("Number tweets about " + subject + ": " + 
            result.getTweets().size()) ;
        for (Status tweet : result.getTweets()) 
          System.out.println(String.format("@%s [%s]: %s",
              tweet.getUser().getScreenName(),
              tweet.getUser().getName(),
              tweet.getText()));
      } 
      catch (TwitterException e) {
        System.out.println("Problem retrieving tweets about " + subject);
        e.printStackTrace();
      } 
    }
  }

  /**
   * This method prints the top two favorite words of the given user when
   * forReal is true.
   */
  
  public static void favoriteWords(String handle, boolean forReal) {
    if (!forReal) 
      System.out.println(String.format("favoriteWords(\"%s\", %b);",
          handle, forReal));
    else {
      PopularityBot bot = new PopularityBot(handle, 200);
      System.out.println(bot.getTimeStamp());
      System.out.println(String.format("The most common words in the last " +
              "%d tweets from @%s are:\n   %s\n   %s\n",
          bot.getNumTweets(), 
          handle,
          bot.kthMostPopularWord(1),
          bot.kthMostPopularWord(2)));
    }
  }

  /**
   * TODO: See Canvas description.
   */

  public static void main(String... args) { 
    //sendTweet("\"Hello, world!\" from my Twitter bot...", false);
    sendTweet("\"Bot or not?", false);

    // What are people in my town saying about...?
    tweetsAbout(Constants.HASHTAG, true);

    //tweetsAbout("nasa", true);
    //tweetsAbout("Oprah", true);
    // Find and print the most common word recently tweeted by the president.
    favoriteWords("realDonaldTrump", true);
    favoriteWords("mike_pence", true);
    favoriteWords("taylorswift13", true);
    favoriteWords("billnye", true);
    favoriteWords("Oprah", true);
    favoriteWords("nasa", true);
  }
}


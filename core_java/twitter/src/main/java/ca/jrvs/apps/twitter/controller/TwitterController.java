package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.GeoLoc;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.Arrays;
import java.util.List;

public class TwitterController implements Controller {

  private TwitterService twitterService;

  // Default constructor
  public TwitterController() {
    twitterService = new TwitterService();
  }

  // Alternate constructor to provide your own TwitterService
  public TwitterController(TwitterService twitterService) {
    this.twitterService = twitterService;
  }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    Tweet tweetToPost = new Tweet();
    if (args.length < 1) {
      throw new IllegalArgumentException("At least 1 argument is required");
    }
    tweetToPost.setText(args[0]);
    if (args.length == 3) {
      String lon = args[1].replace(',', '.');
      String lat = args[2].replace(',', '.');
      try {
        GeoLoc location = new GeoLoc();
        location
            .setCoordinates(new float[]{Float.parseFloat(lon), Float.parseFloat(lat)});
        tweetToPost.setLocation(location);
      } catch (NumberFormatException nfex) {
        throw new IllegalArgumentException(
            "Second and third arguments must be floating-point numbers");
      }
    } else if (args.length == 2 || args.length > 3) {
      throw new IllegalArgumentException("Incorrect number of args given. Expected 1 or 3");
    }
    return twitterService.postTweet(tweetToPost);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    String tweetId;
    String[] fields = {};
    if (args.length >= 1) {
      tweetId = args[0];
      if (args.length >= 2) {
        fields = Arrays.copyOfRange(args, 1, args.length);
      }
      return twitterService.showTweet(tweetId, fields);
    } else {
      throw new IllegalArgumentException("At least one argument (Tweet ID) is required");
    }
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length < 1) {
      throw new IllegalArgumentException("Expected at least one Tweet ID");
    }
    return twitterService.deleteTweets(args);
  }
}

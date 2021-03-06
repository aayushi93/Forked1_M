package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.GeoLoc;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.utils.JsonUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Repository
public class TwitterDao implements CrdDao<Tweet, Long> {

  private static final String BASE_URL = "https://api.twitter.com/1.1/";
  private static final String POST_URL = BASE_URL + "statuses/update.json";
  private static final String SHOW_URL = BASE_URL + "statuses/show/";
  private static final String DELETE_URL = BASE_URL + "statuses/destroy/";

  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;
  private Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  public TwitterDao() {
    httpHelper = new TwitterHttpHelper(System.getenv("CONSUMER_TOKEN"),
        System.getenv("CONSUMER_SECRET"),
        System.getenv("ACCESS_TOKEN"),
        System.getenv("ACCESS_SECRET"));
  }

  @Autowired
  public TwitterDao(HttpHelper helper) {
    httpHelper = helper;
  }

  /**
   * Create an entity(Tweet) to the underlying storage.
   *
   * @param entity entity that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet entity) {
    HttpResponse response;
    String urlParams = "";
    String text = entity.getText();
    GeoLoc location = entity.getLocation();
    try {
      urlParams = "?status=" + URLEncoder.encode(text, StandardCharsets.UTF_8.name());
      if (location != null) {
        float longitude = entity.getLocation().getCoordinates()[0];
        float latitude = entity.getLocation().getCoordinates()[1];
        urlParams += "&long="
          + URLEncoder.encode(Float.toString(longitude), StandardCharsets.UTF_8.name())
          + "&lat=" + URLEncoder.encode(Float.toString(latitude), StandardCharsets.UTF_8.name());
      }
    } catch (UnsupportedEncodingException ueex) {
      logger.error("Encoding not supported: " + StandardCharsets.UTF_8.name());
      throw new RuntimeException(ueex.getMessage());
    }

    try {
      response = httpHelper.httpPost(new URI(POST_URL + urlParams));
      return JsonUtil.toObject(EntityUtils.toString(response.getEntity()), Tweet.class);
    } catch (URISyntaxException uriex) {
      logger.error("Malformed URI " + POST_URL);
      throw new IllegalArgumentException(uriex.getMessage());
    } catch (IOException ex) {
      logger.error("Failed to parse response\n" + ex.getMessage());
      throw new RuntimeException(ex.getMessage());
    }
  }

  /**
   * Find an entity(Tweet) by its id.
   *
   * @param tweetId entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(Long tweetId) {
    URI find;
    try {
      find = new URI(SHOW_URL + tweetId + ".json");
    } catch (URISyntaxException uriex) {
      throw new IllegalArgumentException(uriex.getMessage());
    }
    try {
      return JsonUtil.toObject(EntityUtils.toString(httpHelper.httpGet(find).getEntity()),
          Tweet.class);
    } catch (IOException ex) {
      throw new IllegalArgumentException("Tweet does not exist, or you may not have permission to "
          + "view it\nID: " + tweetId);
    }
  }

  /**
   * Delete an entity(Tweet) by its ID.
   *
   * @param tweetId of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(Long tweetId) {
    URI delete;
    try {
      delete = new URI(DELETE_URL + tweetId + ".json");
    } catch (URISyntaxException uriex) {
      throw new IllegalArgumentException(uriex.getMessage());
    }
    try {
      return JsonUtil
          .toObject(EntityUtils.toString(httpHelper.httpPost(delete).getEntity()), Tweet.class);
    } catch (IOException ex) {
      throw new IllegalArgumentException("You are not allowed to delete this tweet, or it doesn't "
          + "exist.\nID: " + tweetId);
    }
  }
}

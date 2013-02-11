package edu.rit.asksg.dataio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfiguration {


	//todo: configurations shouldn't be static, eg:. multiple accounts
  private static Twitter twitter;
  private static String consumerkey = "wY0Aft0Gz410RtOqOHd7Q";
  private static String consumersecret = "rMxrTP9nqPzwU6UHIQufKR23be4w4NHIqY7VbwfzU";
  private static String accesstoken = "15724679-FUz0huThLIpEzm66QySG7exllaV1CWV9VqXxXeTOw";
  private static String accesstokensecret = "rFTEFz8tNX71V2nCo6pDtF38LhDEfO2f692xxzQxaA";

  public TwitterConfiguration() {

    if (twitter == null) {
      twitter = new TwitterTemplate(consumerkey, consumersecret, accesstoken, accesstokensecret);
    }
  }

  /**
   * A proxy to a request-scoped object representing the simplest Twitter API
   * - one that doesn't need any authorization
   */
  @Bean
  @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
  public Twitter twitter() {
    return twitter;
  }

}
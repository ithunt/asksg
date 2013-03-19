package edu.rit.asksg.repository;

import edu.rit.asksg.domain.Facebook;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class FacebookRepositoryImpl implements FacebookRepository {

	@Override
	public String makeAccessTokenRequest(final Facebook facebook) {
		SpringSocialConfig config = (SpringSocialConfig) facebook.getConfig();
		final String url = "https://graph.facebook.com/oauth/access_token?client_id=" + config.getConsumerKey() +
				"&client_secret=" + config.getConsumerSecret() + "&code=" + config.getAccessToken() +
				"&redirect_uri=http://watchmen.se.rit.edu:8080/asksg/dashboard/index.html";
				//TODO: this is terribad, must fix
		final HttpClient httpClient = new DefaultHttpClient();
		try {
			final ResponseHandler<String> handler = new BasicResponseHandler();
			final String responseBody = httpClient.execute(new HttpGet(url), handler);
			// format: access_token=USER_ACCESS_TOKEN&expires=NUMBER_OF_SECONDS_UNTIL_TOKEN_EXPIRES
			final String accessToken = responseBody.substring(responseBody.indexOf("access_token=") + 13, responseBody.indexOf("&"));
			config.setAccessTokenSecret(accessToken);
			return accessToken;
		} catch (IOException e) {
			//TODO: handle Facebook's explosion properly :3
			e.printStackTrace();
		}
		return null;
	}
}

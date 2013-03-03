package edu.rit.asksg.analytics;

import com.mashape.client.http.MashapeCallback;
import com.mashape.client.http.MashapeResponse;
import edu.rit.asksg.analytics.client.SentimentAnalysisForSocialMedia;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.service.MessageService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.tostring.RooToString;


@RooJavaBean
@RooToString
@RooJpaEntity
public class Chatterbox {


	public final transient ProviderConfig config;
	public final transient MessageService messageService;

	@Autowired
	public Chatterbox(@Qualifier("chatterboxConfig") final ProviderConfig config, final MessageService messageService) {
		this.config = config;
		this.messageService = messageService;
	}

	public void handleMessage(final Message message) {
		SentimentAnalysisForSocialMedia client = new SentimentAnalysisForSocialMedia(config.getAuthenticationToken());
		Thread thread = client.classifytext(message.getContent(), "en", null, null, new MashapeCallback<JSONObject>() {
			@Override
			public void requestCompleted(MashapeResponse<JSONObject> jsonObjectMashapeResponse) {
				final JSONObject body = jsonObjectMashapeResponse.getBody();
				//todo log response
				try {
					final Double sentimentScore = Double.parseDouble(body.getString("value"));
					message.getAnalytics().setSentimentScore(sentimentScore);
					messageService.updateMessage(message);
					//todo: consider if messageservice is appropriate as implemented, or better to add analyticsservice
				} catch (JSONException e) {
					//todo log failure
					System.out.println(e.getStackTrace());

				}
			}

			@Override
			public void errorOccurred(Exception e) {
				System.out.println(e.getStackTrace());
				//todo log failure
			}
		});
	}


}

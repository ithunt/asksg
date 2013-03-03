package edu.rit.asksg.analytics;

import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.service.MessageService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Awaitility.fieldIn;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChatterboxIntegrationTest {

	final private String testAuthenticationToken = "4heJuZtO5Ji43xk1uc0edKmDVgrSdheK";
	final private Double testSentimentScore = 0.2903651077192131;
	private ProviderConfig config;
	private MessageService service;

	@Before
	public void setup() {
		config = mock(ProviderConfig.class);
		when(config.getAuthenticationToken()).thenReturn(testAuthenticationToken);
		service = mock(MessageService.class);
	}

	@Test
	public void testIntegrationWithChatterboxAPI() {
		Chatterbox chatterbox = new Chatterbox(config, null);
		Message message = new Message();
		message.setContent("Test query to chatterbox");
		when(service.updateMessage(eq(message))).thenReturn(message);
		chatterbox.handleMessage(message);
		await().atMost(4, TimeUnit.SECONDS).until(fieldIn(message.getAnalytics()).ofType(Double.class).andWithName("sentimentScore"), equalTo(testSentimentScore));
		assertNotNull(message.getAnalytics().getSentimentScore());
		assertEquals(testSentimentScore, message.getAnalytics().getSentimentScore());
	}


}
package edu.rit.asksg.analytics.client;

import com.mashape.client.authentication.Authentication;
import com.mashape.client.authentication.MashapeAuthentication;
import com.mashape.client.http.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SentimentAnalysisForSocialMedia {

	private final static String PUBLIC_DNS = "chatterbox-analytics-sentiment-analysis-free.p.mashape.com";
	private List<Authentication> authenticationHandlers;

	public SentimentAnalysisForSocialMedia (String mashapeKey) {
		authenticationHandlers = new LinkedList<Authentication>();
		authenticationHandlers.add(new MashapeAuthentication(mashapeKey));

	}

	/**
	 * Synchronous call with optional parameters.
	 */
	public MashapeResponse<JSONObject> classifytext(String text, String lang, String exclude, String detectlang) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		if (lang != null && !lang.equals("")) {
			parameters.put("lang", lang);
		}



		if (text != null && !text.equals("")) {
			parameters.put("text", text);
		}



		if (exclude != null && !exclude.equals("")) {
			parameters.put("exclude", exclude);
		}



		if (detectlang != null && !detectlang.equals("")) {
			parameters.put("detectlang", detectlang);
		}



		return (MashapeResponse<JSONObject>) HttpClient.doRequest(JSONObject.class,
				HttpMethod.POST,
				"https://" + PUBLIC_DNS + "/sentiment/current/classify_text/",
				parameters,
				ContentType.FORM,
				ResponseType.JSON,
				authenticationHandlers);
	}

	/**
	 * Synchronous call without optional parameters.
	 */
	public MashapeResponse<JSONObject> classifytext(String text) {
		return classifytext(text, "", "", "");
	}


	/**
	 * Asynchronous call with optional parameters.
	 */
	public Thread classifytext(String text, String lang, String exclude, String detectlang, MashapeCallback<JSONObject> callback) {
		Map<String, Object> parameters = new HashMap<String, Object>();


		if (lang != null && !lang.equals("")) {

			parameters.put("lang", lang);
		}



		if (text != null && !text.equals("")) {

			parameters.put("text", text);
		}



		if (exclude != null && !exclude.equals("")) {

			parameters.put("exclude", exclude);
		}



		if (detectlang != null && !detectlang.equals("")) {

			parameters.put("detectlang", detectlang);
		}


		return HttpClient.doRequest(JSONObject.class,
				HttpMethod.POST,
				"https://" + PUBLIC_DNS + "/sentiment/current/classify_text/",
				parameters,
				ContentType.FORM,
				ResponseType.JSON,
				authenticationHandlers,
				callback);
	}

	/**
	 * Asynchronous call without optional parameters.
	 */
	public Thread classifytext(String text, MashapeCallback<JSONObject> callback) {
		return classifytext(text, "", "", "", callback);
	}

}
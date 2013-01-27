package edu.rit.asksg.web;

import edu.rit.asksg.dataio.TwilioProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/twilios")
public class TwilioController {

	@Autowired
	TwilioProvider provider;

	@RequestMapping(value = "/receive", method = RequestMethod.POST)//, produces = "text/xml")
	//public ResponseEntity<String> receiveSMS(@RequestBody Map<String, String> request) {
	public @ResponseBody
	ResponseEntity<String> receiveSMS(@RequestBody Map<String, String> request) {
		provider.handleMessage(request);
		Logger logger = LoggerFactory.getLogger("");
		logger.error("TwilioController: receiveSMS called");

		//TODO: I'm not sure what this XML document will actually look like to Twilio.
		/*TwiMLResponse response = new TwiMLResponse();
		response.Sms = "Thank you for contacting ASKSG.";
		return response;*/
		HttpHeaders headers = new HttpHeaders();
		headers.add("content_type", "text/xml");

		return new ResponseEntity<String>("Please work, Twilio :(", headers, HttpStatus.OK);
	}

	/*
	 * Used to respond to Twilio.
	 */
	/*
	private class TwiMLResponse {
		public String Sms;
	}*/
}

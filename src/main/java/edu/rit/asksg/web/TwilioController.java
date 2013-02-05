package edu.rit.asksg.web;

import edu.rit.asksg.domain.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/twilios")
public class TwilioController {

	@Resource(name = "twilioProvider")
	Twilio twilio;

	private static final Logger logger = LoggerFactory.getLogger(TwilioController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/sms")
	public ResponseEntity<String> receiveSMS(@RequestParam(value = "body") String smsSid,
											 @RequestParam(value = "body") String accountSid,
											 @RequestParam(value = "body") String from,
											 @RequestParam(value = "body") String to,
											 @RequestParam(value = "from") String body) {

		logger.debug("Telling Twilio to handle a message, body '" + body + "'...");
		twilio.handleMessage(smsSid, accountSid, from, to, body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "test/plain");
		return new ResponseEntity<String>("Thanks for contacting RIT Student Government.", headers, HttpStatus.OK);
	}

}

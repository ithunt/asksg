package edu.rit.asksg.web;

import edu.rit.asksg.domain.Twilio;
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

	@RequestMapping(method = RequestMethod.POST, value = "/sms")
	public ResponseEntity<String> receiveSMS(@RequestParam(value = "SmsSid") String smsSid,
											 @RequestParam(value = "AccountSid") String accountSid,
											 @RequestParam(value = "From") String from,
											 @RequestParam(value = "To") String to,
											 @RequestParam(value = "Body") String body) {

		twilio.handleMessage(smsSid, accountSid, from, to, body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		return new ResponseEntity<String>("Thanks for contacting RIT Student Government.", headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/call")
	public ResponseEntity<String> bounceCall() {
		// Phone calls are out of scope, but this method would be where to handle them.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		return new ResponseEntity<String>("Thank you for contacting RIT Student Government. This phone number only" +
				"supports text messages at this time. Thank you.", headers, HttpStatus.OK);
	}

}

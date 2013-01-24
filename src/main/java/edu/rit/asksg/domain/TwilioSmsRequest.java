package edu.rit.asksg.domain;

/**
 * POJO for requests from Twilio made because of an incoming SMS message.
 * Fields are the parameters passed in by Twilio in its HTTP POST; Spring should fill them in.
 */
public class TwilioSmsRequest {
	public String smsSid;
	public String accountSid;
	public String from;
	public String to;
	public String body;
}

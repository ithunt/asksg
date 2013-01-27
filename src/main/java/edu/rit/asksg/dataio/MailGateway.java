package edu.rit.asksg.dataio;


import edu.rit.asksg.domain.Message;
import org.springframework.integration.annotation.Gateway;

public interface MailGateway {

    @Gateway
    public void sendMail(Message message);
}

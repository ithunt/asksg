package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.ProviderConfig;
import edu.rit.asksg.service.ConversationService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TwilioProvider implements ContentProvider {

    @Autowired
    ProviderConfig config;

    @Autowired
    ConversationService conversationService;

    @Override
    public List<Conversation> fetchNewContent() {
        return null;
    }

    @Override
    public List<Conversation> fetchContentSince(LocalDateTime datetime) {
        return null;
    }

    @Override
    public boolean postContent(Message message) {
        return false;
    }

    @Override
    public boolean authenticate() {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    public void handleMessage(String messageFromTwilio) {
        // process XML
        // make a message
        // put content in
        // wrap that in a conversation
        // save the conversation
        // wheeeeeeeeeee
    }

    public void setConfig(ProviderConfig config) {
        this.config = config;
    }
}

package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import org.joda.time.LocalDateTime;

import java.util.List;

public interface ContentProvider {

    public List<Conversation> getNewContent();

    public List<Conversation> getContentSince(LocalDateTime datetime);

    public boolean postContent(Message message);

    public boolean authenticate();

    public boolean isAuthenticated();

}

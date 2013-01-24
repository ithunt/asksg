package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import org.joda.time.LocalDateTime;

import java.util.List;

public interface ContentProvider {

    public List<Conversation> fetchNewContent();

    public List<Conversation> fetchContentSince(LocalDateTime datetime);

    public boolean postContent(Message message);

    public boolean authenticate();

    public boolean isAuthenticated();

}

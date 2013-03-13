package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.SocialSubscription;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.List;

public interface ContentProvider {

	public List<Conversation> getNewContent();

	public List<Conversation> getContentSince(LocalDateTime datetime);

	public boolean postContent(Message message);

	public boolean authenticate();

	public boolean isAuthenticated();

}

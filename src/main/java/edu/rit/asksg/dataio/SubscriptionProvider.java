package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.SocialSubscription;

import java.util.Collection;

public interface SubscriptionProvider {

    public Collection<Conversation> getContentFor(SocialSubscription socialSubscription);
}

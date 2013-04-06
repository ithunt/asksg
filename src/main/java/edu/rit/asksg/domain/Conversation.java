package edu.rit.asksg.domain;

import flexjson.JSON;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson(deepSerialize = true)
public class Conversation {

    @OrderBy("created")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Message> messages = new ArrayList<Message>();

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @DateTimeFormat(style = "M-")
    private LocalDateTime created = new LocalDateTime();

    @NotNull
    @DateTimeFormat(style = "M-")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime modified = new LocalDateTime();

    @ManyToOne(fetch = FetchType.EAGER)
    private Service service;

    private String externalId;

    private Boolean hidden = Boolean.FALSE;

    private Boolean isRead = Boolean.FALSE;

    public Conversation() {
    }

    public Conversation(Message m) {
        this.messages = new ArrayList<Message>();
        this.messages.add(m);
    }

    @JSON(include = true)
    public List<edu.rit.asksg.domain.Message> getMessagesSorted() {
        List<Message> sorted = new ArrayList(this.messages);
        Collections.sort(sorted, new Comparator<Message>() {

            @Override
            public int compare(Message message, Message message2) {
                return message.getCreated().compareTo(message2.getCreated());
            }
        });
        return sorted;
    }
}

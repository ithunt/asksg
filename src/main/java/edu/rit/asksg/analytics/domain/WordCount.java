package edu.rit.asksg.analytics.domain;

import edu.rit.asksg.domain.Service;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class WordCount {

    @ManyToOne(fetch = FetchType.EAGER)
    private Topic topic;

    @ManyToOne(fetch = FetchType.EAGER)
    private Service service;

    private Long wordCount;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    @DateTimeFormat(style = "M-")
    private LocalDateTime created;

}

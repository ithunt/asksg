package edu.rit.asksg.domain;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public class Message {

	@NotNull
	private String author;

	@NotNull
    @Column(length = 2000)
    @Size(max = 2000)
	private String content;

	@NotNull
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	@DateTimeFormat(style = "M-")
	private LocalDateTime created = new LocalDateTime();

	@NotNull
	@DateTimeFormat(style = "M-")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime modified = new LocalDateTime();

	@OneToOne(cascade = CascadeType.ALL)
	private Analytics analytics = new Analytics();

	@ManyToOne
	private Conversation conversation;

	private String url;

	@NotNull
	private Boolean posted = Boolean.FALSE;

	private String snippet;

	private String recipient;
}

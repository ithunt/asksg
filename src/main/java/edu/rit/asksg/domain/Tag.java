package edu.rit.asksg.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooJson
public abstract class Tag {
	private String name;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private AsksgUser createdBy;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Message> message;


}

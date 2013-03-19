package edu.rit.asksg.domain;

import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = {"findPeopleByEmailEquals", "findPeopleByNameEquals", "findPeopleByPhoneNumberEquals"})
public class Person extends Identity {

	@NotNull
	private String name;

	private String email;

	private String phoneNumber;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}

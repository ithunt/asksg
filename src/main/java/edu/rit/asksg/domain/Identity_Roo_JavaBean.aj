// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain;

import edu.rit.asksg.domain.Identity;

privileged aspect Identity_Roo_JavaBean {

	public boolean Identity.isEnabled() {
		return this.enabled;
	}

	public void Identity.setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String Identity.getName() {
		return this.name;
	}

	public void Identity.setName(String name) {
		this.name = name;
	}

	public String Identity.getEmail() {
		return this.email;
	}

	public void Identity.setEmail(String email) {
		this.email = email;
	}

	public String Identity.getPhoneNumber() {
		return this.phoneNumber;
	}

	public void Identity.setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}

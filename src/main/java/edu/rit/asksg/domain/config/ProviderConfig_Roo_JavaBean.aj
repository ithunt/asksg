// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.domain.config;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.config.ProviderConfig;

privileged aspect ProviderConfig_Roo_JavaBean {

	public String ProviderConfig.getAuthenticationToken() {
		return this.authenticationToken;
	}

	public void ProviderConfig.setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String ProviderConfig.getIdentifier() {
		return this.identifier;
	}

	public void ProviderConfig.setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String ProviderConfig.getUsername() {
		return this.username;
	}

	public void ProviderConfig.setUsername(String username) {
		this.username = username;
	}

	public String ProviderConfig.getPassword() {
		return this.password;
	}

	public void ProviderConfig.setPassword(String password) {
		this.password = password;
	}

	public String ProviderConfig.getHost() {
		return this.host;
	}

	public void ProviderConfig.setHost(String host) {
		this.host = host;
	}

	public int ProviderConfig.getPort() {
		return this.port;
	}

	public void ProviderConfig.setPort(int port) {
		this.port = port;
	}

	public AsksgUser ProviderConfig.getCreatedBy() {
		return this.createdBy;
	}

	public void ProviderConfig.setCreatedBy(AsksgUser createdBy) {
		this.createdBy = createdBy;
	}

}

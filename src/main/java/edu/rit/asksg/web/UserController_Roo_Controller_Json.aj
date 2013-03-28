// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.web;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.web.UserController;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect UserController_Roo_Controller_Json {

	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> UserController.showJson(@PathVariable("id") Long id) {
		AsksgUser asksgUser = userService.findAsksgUser(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (asksgUser == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(asksgUser.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> UserController.listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<AsksgUser> result = userService.findAllAsksgUsers();
		return new ResponseEntity<String>(AsksgUser.toJsonArray(result), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> UserController.createFromJsonArray(@RequestBody String json) {
		for (AsksgUser asksgUser : AsksgUser.fromJsonArrayToAsksgUsers(json)) {
			userService.saveAsksgUser(asksgUser);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> UserController.updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		for (AsksgUser asksgUser : AsksgUser.fromJsonArrayToAsksgUsers(json)) {
			if (userService.updateAsksgUser(asksgUser) == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> UserController.deleteFromJson(@PathVariable("id") Long id) {
		AsksgUser asksgUser = userService.findAsksgUser(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (asksgUser == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		userService.deleteAsksgUser(asksgUser);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}

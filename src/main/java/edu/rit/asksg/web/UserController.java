package edu.rit.asksg.web;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.UserRole;
import edu.rit.asksg.service.RoleService;
import edu.rit.asksg.service.UserService;
import edu.rit.asksg.service.UserServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RooWebJson(jsonObject = AsksgUser.class)
@Controller
@RequestMapping("/users")
public class UserController {

	@Resource(name = "userDetailsService")
	UserServiceImpl userService;

	@Autowired
	RoleService roleService;
	@Log
	Logger log;


	@RequestMapping(value = "/current")
	public ResponseEntity<String> currentUser(Principal principal) {
		AsksgUser user = null;
		if (principal != null) {
			String current = principal.getName();
			if (current != null && current != "") {
				user = (AsksgUser) userService.loadUserByUsername(current);
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		if (user == null) user = new AsksgUser();

		return new ResponseEntity<String>(user.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		AsksgUser asksgUser = AsksgUser.fromJsonToAsksgUser(json);
		asksgUser.setRole(roleService.findUserRole(asksgUser.getRole().getName()));
		userService.saveAsksgUser(asksgUser);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		AsksgUser asksgUser = AsksgUser.fromJsonToAsksgUser(json);
		AsksgUser dbUser = userService.findAsksgUser(asksgUser.getId());
		dbUser.setEnabled(asksgUser.isEnabled());
		dbUser.setEmail(asksgUser.getEmail());
		dbUser.setName(asksgUser.getName());
		dbUser.setPhoneNumber(asksgUser.getPhoneNumber());
		dbUser.setUserName(asksgUser.getUserName());
		//password not provided by asksguser, skip
		//changing roles not supported
		if (userService.updateAsksgUser(dbUser) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}

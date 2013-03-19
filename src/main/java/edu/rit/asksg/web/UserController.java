package edu.rit.asksg.web;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.UserRole;
import edu.rit.asksg.service.RoleService;
import edu.rit.asksg.service.UserService;
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
import java.util.HashSet;
import java.util.Set;

@RooWebJson(jsonObject = AsksgUser.class)
@Controller
@RequestMapping("/users")
public class UserController {

	//todo: remvoe and autoinject by roo
    @Resource(name = "userDetailsService")
    UserService userService;

	@Autowired
	RoleService roleService;

    @RequestMapping(value = "/seed")
    public ResponseEntity<String> seed() {


        AsksgUser ian = new AsksgUser();
        ian.setName("ian");
        ian.setUserName("ian");
        ian.setPassword("ian");
	    ian.setRole(roleService.findUserRole("Admin"));
        userService.saveAsksgUser(ian);

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("Created user ian", headers, HttpStatus.OK);

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
}

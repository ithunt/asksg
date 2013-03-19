package edu.rit.asksg.web;

import edu.rit.asksg.domain.UserRole;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@RooWebJson(jsonObject = UserRole.class)
@Controller
@RequestMapping("/roles")
public class RoleController {
	@RequestMapping(value = "/seed")
	public ResponseEntity<String> seed() {

		UserRole role1 = new UserRole();
		role1.setName("Admin");
		UserRole role2 = new UserRole();
		role2.setName("Senator");
		roleService.saveUserRole(role1);
		roleService.saveUserRole(role2);

		HttpHeaders headers = new HttpHeaders();
		headers.add("content_type", "text/plain");

		return new ResponseEntity<String>("Created user roles", headers, HttpStatus.OK);

	}
}

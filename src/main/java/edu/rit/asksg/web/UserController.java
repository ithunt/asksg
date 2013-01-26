package edu.rit.asksg.web;

import edu.rit.asksg.domain.AsksgUser;
import edu.rit.asksg.domain.UserRole;
import edu.rit.asksg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@RooWebJson(jsonObject = AsksgUser.class)
@Controller
@RequestMapping("/users")
public class UserController {


    @Resource(name = "userDetailsService")
    UserService userService;

    @RequestMapping(value = "/seed")
    public ResponseEntity<String> seed() {


        AsksgUser ian = new AsksgUser();
        ian.setName("ian");
        ian.setUserName("ian");
        ian.setPassword("ian");

        UserRole role = new UserRole();
        role.setName("ROLE_admin");
        Set<UserRole> roles = new HashSet<UserRole>();
        roles.add(role);

        ian.setRoles(roles);

        userService.saveAsksgUser(ian);

        HttpHeaders headers = new HttpHeaders();
        headers.add("content_type", "text/plain");

        return new ResponseEntity<String>("Created user ian", headers, HttpStatus.OK);

    }

}

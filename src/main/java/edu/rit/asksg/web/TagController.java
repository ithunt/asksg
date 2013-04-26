package edu.rit.asksg.web;

import com.google.common.base.Optional;
import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Identity;
import edu.rit.asksg.domain.IdentityTag;
import edu.rit.asksg.domain.Message;
import edu.rit.asksg.domain.Tag;
import edu.rit.asksg.domain.TopicTag;
import edu.rit.asksg.service.IdentityService;
import edu.rit.asksg.service.MessageService;
import edu.rit.asksg.service.TagService;
import edu.rit.asksg.service.UserService;
import edu.rit.asksg.service.UserServiceImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;

@RooWebJson(jsonObject = Tag.class)
@Controller
@RequestMapping("/tags")
public class TagController {

	@Resource(name = "userDetailsService")
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private IdentityService identityService;

	@Log
	Logger log;

	@RequestMapping(method = RequestMethod.POST, value = "create")
	public ResponseEntity<String> createFromJson(@RequestParam("tagName") String tagName, @RequestParam("messageId") Long messageId, Principal principal) {
		Message message = messageService.findMessage(messageId);
		Tag tag;
		//check if tag already exists
		Tag existingTag = tagService.findByName(tagName);
		if (existingTag == null) {
			Optional<Identity> identity = identityService.searchIdentity(tagName);
			if (identity.isPresent()) {
				tag = new IdentityTag();
				tag.setName(tagName);
			} else if (identityService.isIdentity(tagName)) {
				identityService.findOrCreate(tagName);
				tag = new IdentityTag();
				tag.setName(tagName);
			} else {
				tag = new TopicTag();
				tag.setName(tagName);
			}
		} else {
			tag = existingTag;
		}
		tag.getCreatedBy().add(userService.loadUserByUsername(principal.getName()));
		message.getTags().add(tag);
		tagService.saveTag(tag);
		messageService.updateMessage(message);
		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(tag);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(Tag.toJsonArray(tags), headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "remove")
	public ResponseEntity<String> deleteFromJson(@RequestParam("tagid") String tagid, @RequestParam("messageId") String messageId) {
		Tag tag = tagService.findTag(Long.parseLong(tagid));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (tag == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		Message message = messageService.findMessage(Long.parseLong(messageId));
		message.getTags().remove(tag);
		messageService.updateMessage(message);
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
}

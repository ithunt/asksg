package edu.rit.asksg.web;

import edu.rit.asksg.domain.Facebook;
import edu.rit.asksg.domain.Service;
import edu.rit.asksg.domain.SocialSubscription;
import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.domain.config.SpringSocialConfig;
import edu.rit.asksg.service.FacebookService;
import edu.rit.asksg.service.ProviderService;
import edu.rit.asksg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@RooWebJson(jsonObject = Service.class)
@Controller
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    FacebookService facebookService;

    @Autowired
    ProviderService providerService;

    @Resource(name = "userDetailsService")
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "facebookToken")
    public ResponseEntity<String> facebookToken(@RequestParam("id") final String id,
                                                @RequestParam("code") final String code) {
        //TODO: type safety
        final Facebook facebook = (Facebook) providerService.findService(Long.parseLong(id));
        final SpringSocialConfig fbConfig = ((SpringSocialConfig) facebook.getConfig());
        fbConfig.setAccessToken(code);
        SocialSubscription ritsg = new SocialSubscription();
        ritsg.setHandle("ritstudentgov"); // TODO: still hard-coded
        fbConfig.getSubscriptions().add(ritsg);
        facebookService.makeAccessTokenRequest(facebook);
        providerService.updateService(facebook);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if(principal == null || !userService.isAdmin(principal.getName()))
            return new ResponseEntity<String>("not authorized", headers, HttpStatus.FORBIDDEN);

        Service service = Service.fromJsonToService(json);
        providerService.saveService(service);
        return new ResponseEntity<String>(service.toJson(), headers, HttpStatus.CREATED);

    }


    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public ResponseEntity<String> subscribe(@RequestParam("id") final String id,
                                            @RequestParam("name") final String name,
                                            @RequestParam("handle") final String handle) {

        providerService.addSubscriptionToService(Long.parseLong(id), name, handle);

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


    /**
     * Overrides to add security
     */
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        Service service = providerService.findService(id);

        if (service == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        } else if (principal == null || !userService.isAdmin(principal.getName())) {
            service.setConfig(new ProviderConfig());
        }
        return new ResponseEntity<String>(service.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Service> result = providerService.findAllServices();
        if(principal == null || !userService.isAdmin(principal.getName())) {
            for(Service s : result) {
                s.setConfig(new ProviderConfig());
            }
        }
        return new ResponseEntity<String>(Service.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if(principal == null || !userService.isAdmin(principal.getName()))
            return new ResponseEntity<String>("not authorized", headers, HttpStatus.FORBIDDEN);

        for (Service service: Service.fromJsonArrayToServices(json)) {
            providerService.saveService(service);
        }
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if(principal == null || !userService.isAdmin(principal.getName()))
            return new ResponseEntity<String>("not authorized", headers, HttpStatus.FORBIDDEN);

        Service service = Service.fromJsonToService(json);
        if (providerService.updateService(service) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if(principal == null || !userService.isAdmin(principal.getName()))
            return new ResponseEntity<String>("not authorized", headers, HttpStatus.FORBIDDEN);

        for (Service service: Service.fromJsonArrayToServices(json)) {
            if (providerService.updateService(service) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id, Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        if(principal == null || !userService.isAdmin(principal.getName()))
            return new ResponseEntity<String>("not authorized", headers, HttpStatus.FORBIDDEN);

        Service service = providerService.findService(id);
        if (service == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        providerService.deleteService(service);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }


}

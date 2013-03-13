// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.rit.asksg.web;

import edu.rit.asksg.domain.config.ProviderConfig;
import edu.rit.asksg.service.ConfigService;
<<<<<<< HEAD
=======
import edu.rit.asksg.web.ConfigController;
import java.util.List;
>>>>>>> origin/master
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

privileged aspect ConfigController_Roo_Controller_Json {
    
    @Autowired
    ConfigService ConfigController.configService;
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ConfigController.showJson(@PathVariable("id") Long id) {
        ProviderConfig providerConfig = configService.findProviderConfig(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (providerConfig == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(providerConfig.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ConfigController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProviderConfig> result = configService.findAllProviderConfigs();
        return new ResponseEntity<String>(ProviderConfig.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ConfigController.createFromJson(@RequestBody String json) {
        ProviderConfig providerConfig = ProviderConfig.fromJsonToProviderConfig(json);
        configService.saveProviderConfig(providerConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ConfigController.createFromJsonArray(@RequestBody String json) {
        for (ProviderConfig providerConfig: ProviderConfig.fromJsonArrayToProviderConfigs(json)) {
            configService.saveProviderConfig(providerConfig);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ConfigController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ProviderConfig providerConfig = ProviderConfig.fromJsonToProviderConfig(json);
        if (configService.updateProviderConfig(providerConfig) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ConfigController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ProviderConfig providerConfig: ProviderConfig.fromJsonArrayToProviderConfigs(json)) {
            if (configService.updateProviderConfig(providerConfig) == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> ConfigController.deleteFromJson(@PathVariable("id") Long id) {
        ProviderConfig providerConfig = configService.findProviderConfig(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (providerConfig == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        configService.deleteProviderConfig(providerConfig);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}

package edu.rit.asksg.service;

import edu.rit.asksg.analytics.domain.Topic;
import edu.rit.asksg.domain.config.ProviderConfig;
import org.springframework.roo.addon.layers.service.RooService;

import java.util.List;
import java.util.Map;

@RooService(domainTypes = {ProviderConfig.class})
public interface ConfigService {
}

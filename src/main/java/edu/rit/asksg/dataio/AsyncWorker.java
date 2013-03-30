package edu.rit.asksg.dataio;

import edu.rit.asksg.common.Log;
import edu.rit.asksg.domain.Service;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

public interface AsyncWorker {

	public void work(final Service service);

}

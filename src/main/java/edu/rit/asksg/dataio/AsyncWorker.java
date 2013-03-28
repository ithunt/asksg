package edu.rit.asksg.dataio;

import edu.rit.asksg.domain.Service;

public interface AsyncWorker {
	public void work(final Service service);
}

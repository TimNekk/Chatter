package timnekk.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputHandler {
    private static final Logger logger = LoggerFactory.getLogger(OutputHandler.class);

    public void showMessage(String message) {
        logger.info("{}", message);
    }

    public void close() throws IOException {
        // Do nothing
    }
}
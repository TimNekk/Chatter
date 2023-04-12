package timnekk.client;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timnekk.handlers.OutputHandler;

public class ClientOutput extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ClientOutput.class);

    private final Client client;
    private final OutputHandler outputHandler;

    public ClientOutput(Client client, OutputHandler outputHandler) {
        this.client = client;
        this.outputHandler = outputHandler;
    }

    @Override
    public void run() {
        logger.debug("Client output started");

        while (!isInterrupted()) {
            Optional<String> message = readFromServer();
            message.ifPresent(outputHandler::showMessage);
        }
    }

    private Optional<String> readFromServer() {
        String message;

        try {
            while (!isInterrupted() && (message = client.readFromServer().orElse(null)) != null) {
                return Optional.of(message);
            }
        } catch (IOException e) {
            logger.error("Error while running client: {}", e.getMessage());
        } catch (NullPointerException e) {
            logger.warn("Output handler closed unexpectedly");
        }

        return Optional.empty();
    }
}
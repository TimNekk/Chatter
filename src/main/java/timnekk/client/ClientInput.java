package timnekk.client;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timnekk.handlers.InputHandler;

public class ClientInput extends Thread implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ClientInput.class);

    private final Client client;
    private final InputHandler inputHandler;

    public ClientInput(Client client, InputHandler inputHandler) {
        this.client = client;
        this.inputHandler = inputHandler;
    }

    @Override
    public void run() {
        logger.debug("Client input started");

        while (!isInterrupted()) {
            Optional<String> message = getInput();
            message.ifPresent(client::sendMessageToServer);
        }
    }

    private Optional<String> getInput() {
        try {
            return Optional.of(inputHandler.getInput());
        } catch (IOException e) {
            logger.error("Error while reading input: {}", e.getMessage());
        } catch (NullPointerException e) {
            logger.warn("Input handler closed unexpectedly");
        }
        
        return Optional.empty();
    }

    @Override
    public void close() {
        try {
            logger.info("Press enter to exit");
            inputHandler.close();
        } catch (IOException e) {
            logger.error("Error while closing input handler: {}", e.getMessage());
        }
    }
}
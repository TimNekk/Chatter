package timnekk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputHandler implements Runnable, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(InputHandler.class);

    private final Client client;
    private final BufferedReader in;

    public InputHandler(Client client, InputStream inputStream) {
        this.client = client;
        in = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void close() {
        client.sendMessage("quit");

        try {
            in.close();
        } catch (IOException e) {
            logger.error("Error while closing input: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (client.isRunning()) {
                String message = in.readLine();
                client.sendMessage(message);
            }
        } catch (IOException e) {
            logger.error("Error while reading input: {}", e.getMessage());
        } finally {
            close();
        }
    }

    public void printMessage(String message) {
        logger.info("{}", message);
    }

    private boolean isValidMessage(String message) {
        return message != null && !message.isBlank();
    }
}
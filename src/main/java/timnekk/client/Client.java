package timnekk.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timnekk.handlers.InputHandler;
import timnekk.handlers.OutputHandler;
import timnekk.misc.MessageValidator;

public final class Client implements Runnable, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final Socket socket;
    private final BufferedReader serverIn;
    private final PrintWriter serverOut;

    private final ClientInput clientInput;
    private final ClientOutput clientOutput;

    private boolean running = true;

    public Client(String host, int port, InputHandler inputHandler, OutputHandler outputHandler) throws IOException {
        this.socket = new Socket(host, port);
        this.serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.serverOut = new PrintWriter(socket.getOutputStream(), true);

        this.clientInput = new ClientInput(this, inputHandler);
        this.clientOutput = new ClientOutput(this, outputHandler);

        logger.debug("Client initialized");
    }

    @Override
    public void run() {
        clientInput.start();
        clientOutput.start();

        try {
            clientInput.join();
            clientOutput.join();
        } catch (InterruptedException e) {
            logger.error("Error while running client: {}", e.getMessage());
        }
    }

    public Optional<String> readFromServer() throws IOException {
        String message;

        try {
            message = serverIn.readLine();
        } catch (IOException e) {
            logger.error("Error while reading from server: {}", e.getMessage());
            close();
            return Optional.empty();
        }

        if (message == null) {
            logger.info("Server closed connection");
            close();
            return Optional.empty();
        }

        logger.debug("Received message from server: {}", message);
        return Optional.ofNullable(message);
    }

    public void sendMessageToServer(String message) {
        if (!MessageValidator.isValid(message)) {
            return;
        }
        message = MessageValidator.prepare(message);

        logger.debug("Sending message to server: {}", message);
        serverOut.println(message);
    }

    @Override
    public void close() {
        if (!running) {
            return;
        }
        running = false;

        logger.debug("Closing client");
        
        clientInput.close();
        clientInput.interrupt();
        clientOutput.interrupt();

        try {
            serverIn.close();
            serverOut.close();

            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("Error while closing client: {}", e.getMessage());
        }

        logger.debug("Client closed");
    }
}
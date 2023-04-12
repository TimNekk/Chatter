package timnekk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements Runnable, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private InputHandler inputHandler;
    private boolean running;

    public Client(String host, int port, InputStream inputStream) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        this.inputHandler = new InputHandler(this, inputStream);
    }

    @Override
    public void close() throws Exception {
        running = false;

        try {
            in.close();
            out.close();
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("Error while closing client: {}", e.getMessage());
        }
    }

    @Override
    public void run() {
        running = true;

        try {
            Thread thread = new Thread(inputHandler);
            thread.start();

            String message;
            while (running && (message = in.readLine()) != null) {
                inputHandler.printMessage(message);
            }
        } catch (IOException e) {
            logger.error("Error while running client: {}", e.getMessage());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
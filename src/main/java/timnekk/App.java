package timnekk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;

import timnekk.client.Client;
import timnekk.handlers.InputHandler;
import timnekk.handlers.OutputHandler;

@Command(name = "chatter", mixinStandardHelpOptions = true, description = "Chatter app", requiredOptionMarker = '*', abbreviateSynopsis = true)
public class App implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Option(names = { "-H", "--host" }, description = "Host to connect to.", required = true)
    private String host;

    @Option(names = { "-P", "--port" }, description = "Port to connect to.", required = true)
    private int port;

    @Override
    public void run() {
        logger.debug("Starting chatter");
        try (Client client = new Client(host, port, new InputHandler(System.in), new OutputHandler())) {
            client.run();
        } catch (Exception e) {
            logger.error("Error while running chatter: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new App());

        try {
            cmd.parseArgs(args);
            cmd.execute(args);
        } catch (ParameterException e) {
            logger.error("Parsing command line arguments failed: {}", e.getMessage());
        }
    }
}

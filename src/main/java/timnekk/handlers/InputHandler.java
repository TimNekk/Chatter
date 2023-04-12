package timnekk.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputHandler implements AutoCloseable {
    private final BufferedReader in;

    public InputHandler(InputStream inputStream) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
    }

    public String getInput() throws IOException {
        return in.readLine();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
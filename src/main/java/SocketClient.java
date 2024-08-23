package main.java;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class SocketClient extends WebSocketClient {
    public SocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Opened connection with the server!");
        System.out.println(serverHandshake.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String s) {
        System.out.println("Received a message!");
        System.out.println("MESSAGE: " + s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed.");
        System.out.println("Exit code: " + i + ", reason: " + s);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error: " + e.getMessage());
    }

    public static void main(String[] args)
    {
        try {
            // Replace with your WebSocket server URI
            URI serverUri = new URI(""); // <- add API key here for testing
            SocketClient client = new SocketClient(serverUri);
            client.connect();

            // Wait for the connection to be established
            Thread.sleep(2000);

            // Send a message to the WebSocket server
            client.send("{\"type\":\"subscribe\",\"symbol\":\"AAPL\"}");

            // Keep the client open for a while to receive messages
            Thread.sleep(10000);

            // Close the connection
            client.close();

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

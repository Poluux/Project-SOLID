package ClientServer_Communication;

import ClientServer_Communication.Request.RequestProcessor;
import roadnetwork.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Manages communication with a single client.
 * Reads client requests, processes them using a RequestProcessor, and sends responses.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket; // Socket for client communication.
    private final int clientId; // Unique identifier for the client.
    private final RequestProcessor requestProcessor; // Processor for handling client requests.
    private final Graph graph; // Shared graph instance used for requests.

    /**
     * Constructor to initialize the client handler.
     *
     * @param socket           The socket for client communication.
     * @param clientId         The unique ID of the client.
     * @param requestProcessor The processor for handling client requests.
     * @param graph            The graph instance used for operations.
     */
    public ClientHandler(Socket socket, int clientId, RequestProcessor requestProcessor, Graph graph) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.requestProcessor = requestProcessor;
        this.graph = graph;
    }

    /**
     * The main logic for handling client communication.
     * Reads requests from the client, processes them, and sends responses.
     */
    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Send a welcome message to the client.
            out.println("Welcome! You are Client-" + clientId);
            out.println("END_OF_RESPONSE");

            System.out.println("Client-" + clientId + " is now active.");

            String request;
            while ((request = in.readLine()) != null) {
                // Handle "EXIT" command to disconnect the client.
                if ("EXIT".equalsIgnoreCase(request)) {
                    out.println("Deconnexion. Thanks for utilizing our service.");
                    break;
                }

                // Process the client request and send the response.
                String response = requestProcessor.process(request);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the client socket.
                clientSocket.close();
                System.out.println("Client-" + clientId + " has been disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
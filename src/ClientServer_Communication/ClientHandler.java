package ClientServer_Communication;

import ClientServer_Communication.Request.GraphRequestProcessor;
import ClientServer_Communication.Request.RequestHandler;
import ClientServer_Communication.Request.RequestHandlerFactory;
import ClientServer_Communication.Request.RequestProcessor;
import roadnetwork.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles the communication between the server and a single client.
 * Each client is assigned a unique thread managed by an instance of this class.
 */
public class ClientHandler implements Runnable{
    /**
     * The socket associated with the connected client.
     */
    private final Socket clientSocket;
    /**
     * The unique identifier for the client.
     */
    private final int clientId;
    /**
     * The request processor used to handle client requests.
     */
    private final RequestProcessor requestProcessor;
    /**
     * The graph object shared by all clients, representing the road network.
     */
    private Graph graph;

    /**
     * Constructs a new ClientHandler to manage a specific client's communication.
     */
    public ClientHandler(Socket socket, int clientId,RequestProcessor requestProcessor , Graph graph) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.requestProcessor = requestProcessor;
        this.graph = graph;
    }

    /**
     * The main execution method for handling client requests.
     * Reads requests from the client, processes them, and sends back resposnes.
     * Handles special commands like "EXIT" for terminating session.
     */
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Send welcome message to the client
            out.println("Welcome! You are Client-" + clientId);
            out.println("END_OF_RESPONSE");

            System.out.println("Client-" + clientId + " is now active.");

            String request;
            // Continuously read and process client requests until "EXIT" is received
            while ((request = in.readLine()) != null) {
                if ("EXIT".equalsIgnoreCase(request)) {
                    out.println("Deconnexion. Thanks for utilizing our service.");
                    break;
                }

                // Process the client request and send the response
                String response = requestProcessor.process(request);

                // Send response to the client
                out.println(response);
            }
        } catch (IOException e) {
            // Handle exceptions during client communication
            e.printStackTrace();
        } finally {
            try {
                // Ensure the client socket is closed when the session ends
                clientSocket.close();
                System.out.println("Client-" + clientId + " has been disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package ClientServer_Communication;

import ClientServer_Communication.Request.GraphRequestProcessor;
import ClientServer_Communication.Request.RequestProcessor;
import roadnetwork.Graph;
import roadnetwork.GraphInitializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main server class for managing client connections.
 * Listens for client connections, assigns unique IDs, and starts ClientHandler threads.
 */
public class Server {
    private static final AtomicInteger clientCounter = new AtomicInteger(1); // Counter for assigning client IDs.
    public static Graph graph = GraphInitializer.initializeGraph(); // Shared graph instance.

    /**
     * The main method to start the server and listen for connections.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(45000); // Open server socket on port 45000.
            System.out.println("Server is listening on port 45000");

            while (true) {
                // Accept a new client connection.
                Socket clientSocket = server.accept();
                int clientId = clientCounter.getAndIncrement(); // Assign a unique ID to the client.
                System.out.println("New client connected: Client-" + clientId);

                // Create a request processor for the client.
                RequestProcessor requestProcessor = new GraphRequestProcessor(graph);

                // Start a new thread to handle the client.
                new Thread(new ClientHandler(clientSocket, clientId, requestProcessor, graph)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
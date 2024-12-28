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
 * The main server class that listens for client connections and handles their requests.
 * It uses a multi-threaded model to allow simultaneous communication with multiple clients.
 */
public class Server {
    /**
     * Atomic counter to keep track of connected clients
     */
    private static final AtomicInteger clientCounter = new AtomicInteger(1);
    /**
     * The graph representing the road network, initialized at server startup.
     */
    private static Graph graph = GraphInitializer.initializeGraph();

    /**
     * The main entry point for the server.
     * Listens for client connections on port 450000, creates a handler for each client,
     * and starts a new thread to manage their communication.
     */
    public static void main(String[] args) {
        try {
            // Create a ServerSocket to Listen for incoming client connections on port 45000
            ServerSocket server = new ServerSocket(45000);
            System.out.println("Server is listening on port 45000");

            // Infinite loop to handle multiple client connections
            while (true) {
                // Accept a new client connection
                Socket clientSocket = server.accept();
                int clientId = clientCounter.getAndIncrement(); // Generate a unique ID for each client
                System.out.println("New client connected: Client-" + clientId);

                // Create a request processor for this client
                RequestProcessor requestProcessor = new GraphRequestProcessor(graph);

                // Create and start a thread to handle communication with the client
                new Thread(new ClientHandler(clientSocket, clientId, requestProcessor, graph)).start();
            }
        } catch (IOException e) {
            // Handle I/O errors (e.g., if the port is already in use)
            e.printStackTrace();
        }
    }
}

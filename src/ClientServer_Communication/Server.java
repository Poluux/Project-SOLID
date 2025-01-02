package ClientServer_Communication;

import ClientServer_Communication.Request.GraphRequestProcessor;
import ClientServer_Communication.Request.RequestProcessor;
import roadnetwork.Graph;
import roadnetwork.GraphInitializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private static final AtomicInteger clientCounter = new AtomicInteger(1);
    public static Graph graph = GraphInitializer.initializeGraph();
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(45000);
            System.out.println("Server is listening on port 45000");
            while (true) {
                Socket clientSocket = server.accept();
                int clientId = clientCounter.getAndIncrement();
                System.out.println("New client connected: Client-" + clientId);

                RequestProcessor requestProcessor = new GraphRequestProcessor(graph);

                // Passes the ID to ClientHandler for personalization
                new Thread(new ClientHandler(clientSocket, clientId, requestProcessor, graph)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

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

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final int clientId;
    private final RequestProcessor requestProcessor;
    private Graph graph;

    public ClientHandler(Socket socket, int clientId,RequestProcessor requestProcessor , Graph graph) {
        this.clientSocket = socket;
        this.clientId = clientId;
        this.requestProcessor = requestProcessor;
        this.graph = graph;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Welcome! You are Client-" + clientId);
            out.println("END_OF_RESPONSE");

            System.out.println("Client-" + clientId + " is now active.");

            String request;
            while ((request = in.readLine()) != null) {
                if ("EXIT".equalsIgnoreCase(request)) {
                    out.println("Deconnexion. Thanks for utilizing our service.");
                    break;
                }

                String response = requestProcessor.process(request);

                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close client socket
                clientSocket.close();
                System.out.println("Client-" + clientId + " has been disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

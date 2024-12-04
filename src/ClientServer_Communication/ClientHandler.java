package ClientServer_Communication;

import ClientServer_Communication.Request.RequestHandler;
import ClientServer_Communication.Request.RequestHandlerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final int clientId;

    public ClientHandler(Socket socket, int clientId) {
        this.clientSocket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println("Welcome! You are Client-" + clientId);

            System.out.println("Client-" + clientId + " is now active.");

            String clientInput;
            while ((clientInput = in.readLine()) != null) {
                String [] parts = clientInput.split(" ",2);
                String requestType = parts[0];
                String requestBody = parts.length > 1 ? parts[1] : "";
                RequestHandler handler = RequestHandlerFactory.getHandler(requestType);
                String response = handler.handle(requestBody);
                out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

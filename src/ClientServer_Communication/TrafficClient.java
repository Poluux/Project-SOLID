package ClientServer_Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TrafficClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 45000);
             BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverIn.readLine()) != null) {
                        if (serverMessage.equals("END_OF_RESPONSE")) {
                            // Fin de la réponse, affichez l'invite
                            System.out.print("Enter command: ");
                        } else {
                            // Affiche le message du serveur
                            System.out.println("Server: " + serverMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Connected to traffic management server.");

            String userInput;
            while ((userInput = userInputReader.readLine()) != null) {
                serverOut.println(userInput); // Send command to server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

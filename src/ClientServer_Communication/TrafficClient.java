package ClientServer_Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A client application for communicating with the traffic management server.
 * Sends user commands to the server and displays the server's responses
 */
public class TrafficClient {
    /**
     * The main entry point of the client application.
     * Establishes a connection to the server, listens for server messages, and allows
     * the user to send commands interactively.
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 45000);
             BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Start a separate thread to continuously listen to server responses
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverIn.readLine()) != null) {
                        if (serverMessage.equals("END_OF_RESPONSE")) {
                            // Marks the end of the server response, prompts user for input
                            System.out.print("Enter command: ");
                        } else {
                            // Display the server message
                            System.out.println("Server: " + serverMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("Connected to traffic management server.");

            String userInput;
            // Read user commands and send them to the server
            while ((userInput = userInputReader.readLine()) != null) {
                // If the client sends "EXIT" to the server, terminate the connection
                if ("EXIT".equalsIgnoreCase(userInput)) {
                    serverOut.println(userInput);

                    String serverResponse = serverIn.readLine();
                    if (serverResponse != null)
                        System.out.println("Server: " + serverResponse);
                    break;  // Exit the loop
                }
                serverOut.println(userInput); // Send command to server
            }

            // Close the socket and terminate the client
            socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            // Handle exceptions related to input/output and socket communication
            e.printStackTrace();
        }
    }
}

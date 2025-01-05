package ClientServer_Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A client application that connects to the traffic management server.
 * Allows the user to send commands and receive responses.
 */
public class TrafficClient {
    /**
     * The main method for the client application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 45000); // Connect to the server at localhost:45000.
                BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Start a thread to read server responses.
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverIn.readLine()) != null) {
                        if (serverMessage.equals("END_OF_RESPONSE")) {
                            System.out.println("------------------------------");
                            System.out.println("Here are the possible commands and their format:");
                            System.out.println("1. PATH CityOne:CityTwo");
                            System.out.println("2. UPDATE CityOne:CityTwo:Time reason (reason is optional)");
                            System.out.println("3. EXIT");
                            System.out.println("Available cities: Genève, Lausanne, Berne, Neuchatel, Montreux");
                            System.out.println("------------------------------");
                            System.out.print("Enter command: ");
                        } else {
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
                // Handle "EXIT" command to close the connection.
                if ("EXIT".equalsIgnoreCase(userInput)) {
                    serverOut.println(userInput);

                    String serverResponse = serverIn.readLine();
                    if (serverResponse != null) {
                        System.out.println("Server: " + serverResponse);
                    }
                    break;
                }
                serverOut.println(userInput); // Send the user command to the server.
            }

            socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
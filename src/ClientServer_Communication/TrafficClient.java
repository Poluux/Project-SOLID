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
                            System.out.println("------------------------------");
                            System.out.println("Here are the possible commands and there format");
                            System.out.println("1. PATH CityOne:CityTwo");
                            System.out.println("2. UPDATE CityOne:CityTwo:Time reason (reason is optional)");
                            System.out.println("3. EXIT");
                            System.out.println("Here are the cities available, Genève, Lausanne, Berne, Neuchatel, Montreux");
                            System.out.println("------------------------------");
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
                // If the client enters "EXIT", send this command to the server
                if ("EXIT".equalsIgnoreCase(userInput)) {
                    serverOut.println(userInput);

                    String serverResponse = serverIn.readLine();
                    if (serverResponse != null)
                        System.out.println("Server: " + serverResponse);
                    break;
                }
                serverOut.println(userInput); // Send command to server
            }

            socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

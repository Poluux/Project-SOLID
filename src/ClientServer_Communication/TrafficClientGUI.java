package ClientServer_Communication;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TrafficClientGUI extends JFrame {
    private JLabel clientLabel;
    private JButton pathButton;
    private JButton updateButton;
    private Socket socket;
    private BufferedReader serverIn;
    private PrintWriter serverOut;

    // Liste des villes (à mettre à jour selon votre graphe)
    private static final String[] CITIES = {"Genève", "Lausanne", "Neuchatel", "Montreux", "Berne"};

    public TrafficClientGUI() {
        // Configuration de la fenêtre principale
        setTitle("Client Interface");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Ajout du label pour afficher le client connecté
        clientLabel = new JLabel("Client Connected", SwingConstants.CENTER);
        clientLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(clientLabel, BorderLayout.NORTH);

        // Panneau central avec les boutons PATH et UPDATE
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        pathButton = new JButton("PATH");
        updateButton = new JButton("UPDATE");
        buttonPanel.add(pathButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Gestionnaires d'événements pour les boutons
        pathButton.addActionListener(e -> openPathFrame());
        updateButton.addActionListener(e -> openUpdateFrame());

        // Ajout d'un écouteur pour fermer le socket proprement à la fermeture de la fenêtre
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeConnection();
            }
        });
    }

    public void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverOut = new PrintWriter(socket.getOutputStream(), true);

            // Thread pour écouter les messages du serveur
            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverIn.readLine()) != null) {
                        if (serverMessage.equals("END_OF_RESPONSE")) {
                            // Terminer la lecture
                            System.out.println("End of response received.");
                        } else {
                            // Afficher le message serveur dans la console (debug)
                            System.out.println("Server: " + serverMessage);
                        }
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to the server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPathFrame() {
        JFrame pathFrame = new JFrame("PATH Functionality");
        pathFrame.setSize(400, 400);
        pathFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pathFrame.setLayout(new BorderLayout());

        JLabel pathLabel = new JLabel("Select start and end cities:", SwingConstants.CENTER);
        pathFrame.add(pathLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JComboBox<String> startCity = new JComboBox<>(CITIES);
        JComboBox<String> endCity = new JComboBox<>(CITIES);
        inputPanel.add(new JLabel("Start City:"));
        inputPanel.add(startCity);
        inputPanel.add(new JLabel("End City:"));
        inputPanel.add(endCity);
        pathFrame.add(inputPanel, BorderLayout.CENTER);

        JTextArea pathResponseArea = new JTextArea();
        pathResponseArea.setEditable(false);
        pathFrame.add(new JScrollPane(pathResponseArea), BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton pathSendButton = new JButton("Send");
        JButton returnButton = new JButton("Return");
        buttonPanel.add(pathSendButton);
        buttonPanel.add(returnButton);
        pathFrame.add(buttonPanel, BorderLayout.PAGE_END);

        pathSendButton.addActionListener(e -> {
            String start = (String) startCity.getSelectedItem();
            String end = (String) endCity.getSelectedItem();
            String command = "PATH " + start + ":" + end;

            if (!command.isEmpty()) {
                serverOut.println(command);
                pathResponseArea.append("You: " + command + "\n");

                new Thread(() -> {
                    try {
                        String serverResponse;
                        StringBuilder responseBuilder = new StringBuilder();
                        while ((serverResponse = serverIn.readLine()) != null) {
                            if (serverResponse.equals("END_OF_RESPONSE")) {
                                break;
                            }
                            responseBuilder.append(serverResponse).append("\n");
                        }
                        SwingUtilities.invokeLater(() -> pathResponseArea.append("Server: " + responseBuilder.toString()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });

        returnButton.addActionListener(e -> pathFrame.dispose());

        pathFrame.setVisible(true);
    }

    private void openUpdateFrame() {
        JFrame updateFrame = new JFrame("UPDATE Functionality");
        updateFrame.setSize(400, 400);
        updateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateFrame.setLayout(new BorderLayout());

        JLabel updateLabel = new JLabel("Select cities and provide new time:", SwingConstants.CENTER);
        updateFrame.add(updateLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JComboBox<String> startCity = new JComboBox<>(CITIES);
        JComboBox<String> endCity = new JComboBox<>(CITIES);
        JTextField newTimeField = new JTextField();
        inputPanel.add(new JLabel("Start City:"));
        inputPanel.add(startCity);
        inputPanel.add(new JLabel("End City:"));
        inputPanel.add(endCity);
        inputPanel.add(new JLabel("New Time (min):"));
        inputPanel.add(newTimeField);
        updateFrame.add(inputPanel, BorderLayout.CENTER);

        JTextArea updateResponseArea = new JTextArea();
        updateResponseArea.setEditable(false);
        updateFrame.add(new JScrollPane(updateResponseArea), BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton updateSendButton = new JButton("Send");
        JButton returnButton = new JButton("Return");
        buttonPanel.add(updateSendButton);
        buttonPanel.add(returnButton);
        updateFrame.add(buttonPanel, BorderLayout.PAGE_END);

        updateSendButton.addActionListener(e -> {
            String start = (String) startCity.getSelectedItem();
            String end = (String) endCity.getSelectedItem();
            String newTime = newTimeField.getText();
            String command = "UPDATE " + start + ":" + end + ":" + newTime;

            if (!command.isEmpty()) {
                serverOut.println(command);
                updateResponseArea.append("You: " + command + "\n");

                new Thread(() -> {
                    try {
                        String serverResponse;
                        StringBuilder responseBuilder = new StringBuilder();
                        while ((serverResponse = serverIn.readLine()) != null) {
                            if (serverResponse.equals("END_OF_RESPONSE")) {
                                break;
                            }
                            responseBuilder.append(serverResponse).append("\n");
                        }
                        SwingUtilities.invokeLater(() -> updateResponseArea.append("Server: " + responseBuilder.toString()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });

        returnButton.addActionListener(e -> updateFrame.dispose());

        updateFrame.setVisible(true);
    }

    private void closeConnection() {
        try {
            if (serverOut != null) serverOut.close();
            if (serverIn != null) serverIn.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrafficClientGUI clientGUI = new TrafficClientGUI();
            clientGUI.setVisible(true);
            clientGUI.connectToServer("localhost", 45000);
        });
    }
}

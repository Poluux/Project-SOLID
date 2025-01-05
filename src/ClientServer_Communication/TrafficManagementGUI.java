package ClientServer_Communication;

import roadnetwork.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A graphical user interface (GUI) for managing traffic operations.
 * Provides functionality for path calculation and travel time updates.
 */
public class TrafficManagementGUI extends JFrame {
    private JTextArea pathResultArea; // Displays results for path calculations and updates.
    private GraphPanel graphPanel; // Custom panel for visualizing the graph.
    private Socket socket; // Socket for communicating with the server.
    private PrintWriter serverOut; // Output stream for sending commands to the server.
    private BufferedReader serverIn; // Input stream for receiving responses from the server.
    private String lastUpdateStart; // Stores the start node for the last update request.
    private String lastUpdateEnd; // Stores the end node for the last update request.
    private String lastUpdateDistance; // Stores the travel time for the last update request.

    /**
     * Constructs the TrafficManagementGUI with the specified graph.
     *
     * @param graph The graph to be displayed and managed in the GUI.
     */
    public TrafficManagementGUI(Graph graph) {
        setTitle("Traffic Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add a panel for path calculation controls.
        JPanel pathPanel = createPathPanel(graph);
        add(pathPanel, BorderLayout.WEST);

        // Add a panel for updating travel time.
        JPanel updatePanel = createUpdatePanel(graph);
        add(updatePanel, BorderLayout.EAST);

        // Add a text area to display results of commands.
        pathResultArea = new JTextArea();
        pathResultArea.setEditable(false);
        pathResultArea.setFont(new Font("Arial", Font.BOLD, 16));
        pathResultArea.setLineWrap(true);
        pathResultArea.setWrapStyleWord(true);
        pathResultArea.setBorder(BorderFactory.createTitledBorder("Result"));
        add(new JScrollPane(pathResultArea), BorderLayout.CENTER);

        // Add a custom panel to visualize the graph.
        graphPanel = new GraphPanel(graph);
        add(graphPanel, BorderLayout.SOUTH);
        graphPanel.repaint();

        // Connect to the server.
        connectToServer("localhost", 45000);

        // Ensure the connection is closed properly when the window is closed.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });
    }

    /**
     * Connects to the server for communication.
     *
     * @param host The server's hostname.
     * @param port The port number to connect to on the server.
     */
    private void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            serverOut = new PrintWriter(socket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Start a thread to handle server responses.
            new Thread(() -> {
                try {
                    List<String> responseBuffer = new ArrayList<>();
                    String line;
                    while ((line = serverIn.readLine()) != null) {
                        if (line.startsWith("Welcome! You are Client-")) {
                            continue;
                        }
                        if ("END_OF_RESPONSE".equals(line)) {
                            processServerResponse(responseBuffer);
                            responseBuffer.clear();
                        } else {
                            responseBuffer.add(line);
                        }
                    }
                } catch (IOException ex) {
                    appendToResultArea("Connection closed by server.");
                }
            }).start();
        } catch (IOException e) {
            appendToResultArea("Error connecting to server: " + e.getMessage());
        }
    }

    /**
     * Closes the connection to the server.
     */
    private void closeConnection() {
        if (serverOut != null) {
            serverOut.println("EXIT");
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignore exceptions during socket closure.
        }
    }

    /**
     * Processes the server's response and updates the result area.
     *
     * @param lines The lines received from the server.
     */
    private void processServerResponse(List<String> lines) {
        String shortestPathLine = null;
        String totalDistanceLine = null;
        boolean updateSuccess = false;

        // Parse the server's response for specific details.
        for (String l : lines) {
            if (l.startsWith("Shortest path from ")) {
                shortestPathLine = l;
            }
            if (l.startsWith("Total distance:")) {
                totalDistanceLine = l;
            }
            if (l.contains("Travel time updated successfully.")) {
                updateSuccess = true;
            }
        }

        // Display the parsed response in the result area.
        if (shortestPathLine != null && totalDistanceLine != null) {
            String formatted = formatPathResponse(shortestPathLine, totalDistanceLine);
            appendToResultArea(formatted);
        } else if (updateSuccess) {
            appendToResultArea("UPDATE from " + lastUpdateStart + " to " + lastUpdateEnd);
            appendToResultArea("New distance: " + lastUpdateDistance + " min");
        } else {
            for (String l : lines) {
                appendToResultArea(l);
            }
        }
    }

    /**
     * Formats the response for a path calculation into a readable string.
     *
     * @param shortestPathLine The line describing the shortest path.
     * @param totalDistanceLine The line describing the total distance.
     * @return A formatted string containing the path details.
     */
    private String formatPathResponse(String shortestPathLine, String totalDistanceLine) {
        String prefix = "Shortest path from ";
        if (!shortestPathLine.startsWith(prefix)) {
            return shortestPathLine + "\n" + totalDistanceLine;
        }
        String remainder = shortestPathLine.substring(prefix.length());
        int idxColon = remainder.indexOf(':');
        if (idxColon < 0) {
            return shortestPathLine + "\n" + totalDistanceLine;
        }
        String fromTo = remainder.substring(0, idxColon).trim();
        String pathList = remainder.substring(idxColon + 1).trim();
        String[] nodes = pathList.split("\\s+");
        StringBuilder sbPath = new StringBuilder();
        for (int i = 0; i < nodes.length; i++) {
            sbPath.append(nodes[i]);
            if (i < nodes.length - 1) {
                sbPath.append("  →  ");
            }
        }
        return "PATH from " + fromTo + "\n" + sbPath + "\n" + totalDistanceLine;
    }

    /**
     * Creates a panel for path calculation controls.
     *
     * @param graph The graph instance.
     * @return The created JPanel for path options.
     */
    private JPanel createPathPanel(Graph graph) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("PATH Options"));

        JLabel startCityLabel = new JLabel("Start City:");
        JComboBox<String> startCityBox = new JComboBox<>(getCities(graph));
        JLabel endCityLabel = new JLabel("End City:");
        JComboBox<String> endCityBox = new JComboBox<>(getCities(graph));
        JButton sendPathButton = new JButton("Send PATH");
        sendPathButton.addActionListener(e -> {
            pathResultArea.setText("");
            String start = (String) startCityBox.getSelectedItem();
            String end = (String) endCityBox.getSelectedItem();
            if (start != null && end != null) {
                String command = "PATH " + start + ":" + end;
                serverOut.println(command);
            }
        });

        panel.add(startCityLabel);
        panel.add(startCityBox);
        panel.add(endCityLabel);
        panel.add(endCityBox);
        panel.add(sendPathButton);
        return panel;
    }

    /**
     * Creates a panel for travel time update controls.
     *
     * @param graph The graph instance.
     * @return The created JPanel for update options.
     */
    private JPanel createUpdatePanel(Graph graph) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("UPDATE Options"));

        JLabel startCityLabel = new JLabel("Start City:");
        JComboBox<String> startCityBox = new JComboBox<>(getCities(graph));
        JLabel endCityLabel = new JLabel("End City:");
        JComboBox<String> endCityBox = new JComboBox<>(getCities(graph));
        JLabel timeLabel = new JLabel("New Time (minutes):");
        JTextField timeField = new JTextField();
        JButton sendUpdateButton = new JButton("Send UPDATE");
        sendUpdateButton.addActionListener(e -> {
            pathResultArea.setText("");
            String start = (String) startCityBox.getSelectedItem();
            String end = (String) endCityBox.getSelectedItem();
            String time = timeField.getText();
            if (start != null && end != null && !time.isEmpty()) {
                try {
                    Double.parseDouble(time);
                    lastUpdateStart = start;
                    lastUpdateEnd = end;
                    lastUpdateDistance = time;
                    String command = "UPDATE " + start + ":" + end + ":" + time + " GUI";
                    serverOut.println(command);
                    graphPanel.repaint();
                } catch (NumberFormatException ex) {
                    pathResultArea.setText("Invalid time value. Please enter a number.");
                }
            }
            timeField.setText("");
        });

        panel.add(startCityLabel);
        panel.add(startCityBox);
        panel.add(endCityLabel);
        panel.add(endCityBox);
        panel.add(timeLabel);
        panel.add(timeField);
        panel.add(sendUpdateButton);
        return panel;
    }

    /**
     * Retrieves a list of city names from the graph.
     *
     * @param graph The graph instance.
     * @return An array of city names.
     */
    private String[] getCities(Graph graph) {
        return graph.getNodes().stream().map(Node::getId).toArray(String[]::new);
    }

    /**
     * Appends a message to the result area in the GUI.
     *
     * @param msg The message to append.
     */
    private void appendToResultArea(String msg) {
        SwingUtilities.invokeLater(() -> {
            pathResultArea.append(msg + "\n");
        });
    }

    /**
     * The main method to start the GUI application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Graph sharedGraph = Server.graph;
        SwingUtilities.invokeLater(() -> {
            TrafficManagementGUI gui = new TrafficManagementGUI(sharedGraph);
            gui.setVisible(true);
        });
    }
}

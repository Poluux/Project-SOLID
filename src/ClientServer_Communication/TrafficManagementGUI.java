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

public class TrafficManagementGUI extends JFrame {
    private JTextArea pathResultArea;
    private GraphPanel graphPanel;
    private Socket socket;
    private PrintWriter serverOut;
    private BufferedReader serverIn;
    private String lastUpdateStart;
    private String lastUpdateEnd;
    private String lastUpdateDistance;

    public TrafficManagementGUI(Graph graph) {
        setTitle("Traffic Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel pathPanel = createPathPanel(graph);
        add(pathPanel, BorderLayout.WEST);

        JPanel updatePanel = createUpdatePanel(graph);
        add(updatePanel, BorderLayout.EAST);

        pathResultArea = new JTextArea();
        pathResultArea.setEditable(false);
        pathResultArea.setFont(new Font("Arial", Font.BOLD, 16));
        pathResultArea.setLineWrap(true);
        pathResultArea.setWrapStyleWord(true);
        pathResultArea.setBorder(BorderFactory.createTitledBorder("Result"));
        add(new JScrollPane(pathResultArea), BorderLayout.CENTER);

        graphPanel = new GraphPanel(graph);
        add(graphPanel, BorderLayout.SOUTH);
        graphPanel.repaint();

        connectToServer("localhost", 45000);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });
    }

    private void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            serverOut = new PrintWriter(socket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

    private void closeConnection() {
        if (serverOut != null) {
            serverOut.println("EXIT");
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {}
    }

    private void processServerResponse(List<String> lines) {
        String shortestPathLine = null;
        String totalDistanceLine = null;
        boolean updateSuccess = false;

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

    private String[] getCities(Graph graph) {
        return graph.getNodes().stream().map(Node::getId).toArray(String[]::new);
    }

    private void appendToResultArea(String msg) {
        SwingUtilities.invokeLater(() -> {
            pathResultArea.append(msg + "\n");
        });
    }

    public static void main(String[] args) {
        Graph sharedGraph = Server.graph;
        SwingUtilities.invokeLater(() -> {
            TrafficManagementGUI gui = new TrafficManagementGUI(sharedGraph);
            gui.setVisible(true);
        });
    }
}

class GraphPanel extends JPanel {
    private Graph graph;
    private final int nodeRadius = 20;
    private final Map<String, Point> nodePositions;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(800, 400));
        this.nodePositions = initializeNodePositions();
    }

    private Map<String, Point> initializeNodePositions() {
        Map<String, Point> positions = new HashMap<>();
        positions.put("Genève", new Point(50, 200));
        positions.put("Lausanne", new Point(250, 100));
        positions.put("Berne", new Point(550, 100));
        positions.put("Neuchatel", new Point(250, 300));
        positions.put("Montreux", new Point(550, 300));
        return positions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.GRAY);
        for (Node from : graph.getNodes()) {
            Point fromPos = nodePositions.get(from.getId());
            for (Edge edge : graph.getEdges(from)) {
                Node to = edge.getTo();
                Point toPos = nodePositions.get(to.getId());
                if (fromPos != null && toPos != null) {
                    g2d.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
                }
            }
        }

        for (Node node : graph.getNodes()) {
            Point pos = nodePositions.get(node.getId());
            if (pos != null) {
                g2d.setColor(Color.BLUE);
                g2d.fillOval(pos.x - nodeRadius / 2, pos.y - nodeRadius / 2, nodeRadius, nodeRadius);
                g2d.setColor(Color.BLACK);
                g2d.drawString(node.getId(), pos.x - nodeRadius, pos.y - nodeRadius);
            }
        }
    }
}
package ClientServer_Communication;

import roadnetwork.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficManagementGUI extends JFrame {
    private JTextArea pathResultArea; // Zone pour afficher le résultat du PATH
    private GraphPanel graphPanel;   // Panneau pour dessiner le graphe

    public TrafficManagementGUI(Graph graph) {
        setTitle("Traffic Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel gauche pour PATH
        JPanel pathPanel = createPathPanel(graph);
        add(pathPanel, BorderLayout.WEST);

        // Panel droit pour UPDATE
        JPanel updatePanel = createUpdatePanel(graph);
        add(updatePanel, BorderLayout.EAST);

        // Zone centrale pour le résultat du PATH
        pathResultArea = new JTextArea();
        pathResultArea.setEditable(false);
        pathResultArea.setFont(new Font("Arial", Font.BOLD, 16));
        pathResultArea.setLineWrap(true);
        pathResultArea.setWrapStyleWord(true);
        pathResultArea.setBorder(BorderFactory.createTitledBorder("PATH Result"));
        add(new JScrollPane(pathResultArea), BorderLayout.CENTER);

        // Zone inférieure pour dessiner le graphe
        graphPanel = new GraphPanel(graph);
        add(graphPanel, BorderLayout.SOUTH);

        // Initialisation du graphe
        graphPanel.repaint();
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
            String start = (String) startCityBox.getSelectedItem();
            String end = (String) endCityBox.getSelectedItem();
            if (start != null && end != null) {
                Node startNode = findNodeByName(graph, start);
                Node endNode = findNodeByName(graph, end);
                if (startNode != null && endNode != null) {
                    String pathResult = findPath(graph, startNode, endNode);
                    pathResultArea.setText(pathResult); // Afficher le résultat dans la zone centrale
                } else {
                    pathResultArea.setText("Invalid nodes selected for PATH.");
                }
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
            String start = (String) startCityBox.getSelectedItem();
            String end = (String) endCityBox.getSelectedItem();
            String time = timeField.getText();
            if (start != null && end != null && !time.isEmpty()) {
                try {
                    double newTime = Double.parseDouble(time);
                    Node startNode = findNodeByName(graph, start);
                    Node endNode = findNodeByName(graph, end);
                    if (startNode != null && endNode != null) {
                        boolean updated = graph.updateTravelTime(startNode, endNode, newTime, "Updated via GUI");
                        if (updated) {
                            pathResultArea.setText("UPDATE Success: Travel time updated for " + start + " → " + end);
                            graphPanel.repaint(); // Re-dessiner le graphe avec les nouvelles valeurs
                        } else {
                            pathResultArea.setText("UPDATE Failed: No edge found between " + start + " and " + end);
                        }
                    } else {
                        pathResultArea.setText("Invalid nodes for UPDATE.");
                    }
                } catch (NumberFormatException ex) {
                    pathResultArea.setText("Invalid time value. Please enter a number.");
                }
            }
            timeField.setText(""); // Réinitialiser le champ de texte après validation
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

    private Node findNodeByName(Graph graph, String name) {
        return graph.getNodes().stream().filter(node -> node.getId().equals(name)).findFirst().orElse(null);
    }

    private String findPath(Graph graph, Node start, Node end) {
        DijkstraPathFindingStrategy pathFinder = new DijkstraPathFindingStrategy();
        String result = pathFinder.findPath(graph, start, end);

        // Reformatage du résultat pour un affichage plus esthétique
        String[] lines = result.split("\n");
        if (lines.length > 1 && lines[0].startsWith("Shortest path")) {
            String[] pathParts = lines[0].split(": ");
            String path = pathParts.length > 1 ? pathParts[1] : "Unknown Path";
            String[] cities = path.split(" ");

            // Construire le chemin sans flèche à la fin
            StringBuilder formattedPath = new StringBuilder();
            for (int i = 0; i < cities.length; i++) {
                formattedPath.append(cities[i]);
                if (i < cities.length - 1) { // Ajouter une flèche sauf après la dernière ville
                    formattedPath.append(" --> ");
                }
            }

            String totalDistance = lines[1].replace("Total distance:", "").trim();
            return "The shortest path from " + start.getId() + " to " + end.getId() + " is:\n" +
                    formattedPath + "\n" +
                    "The total distance is: " + totalDistance;
        }
        return "No path found from " + start.getId() + " to " + end.getId() + ".";
    }

    public static void main(String[] args) {
        Graph graph = GraphInitializer.initializeGraph();
        SwingUtilities.invokeLater(() -> {
            TrafficManagementGUI gui = new TrafficManagementGUI(graph);
            gui.setVisible(true);
        });
    }
}

class GraphPanel extends JPanel {
    private Graph graph;
    private final int nodeRadius = 20;
    private final Map<String, Point> nodePositions; // Positions statiques des nœuds

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(800, 400)); // Taille ajustée pour la disposition
        this.nodePositions = initializeNodePositions();
    }

    private Map<String, Point> initializeNodePositions() {
        // Disposer les villes selon votre plan
        Map<String, Point> positions = new HashMap<>();
        positions.put("Genève", new Point(50, 200));      // À gauche
        positions.put("Lausanne", new Point(250, 100));   // En haut à gauche
        positions.put("Berne", new Point(550, 100));      // En haut à droite
        positions.put("Neuchatel", new Point(250, 300));  // En bas à gauche
        positions.put("Montreux", new Point(550, 300));   // En bas à droite
        return positions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner les arêtes (liens)
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.GRAY);
        for (Node from : graph.getNodes()) {
            Point fromPos = nodePositions.get(from.getId());
            for (Edge edge : graph.getEdges(from)) {
                Node to = edge.getTo();
                Point toPos = nodePositions.get(to.getId());
                if (fromPos != null && toPos != null) {
                    // Dessiner une ligne entre les deux villes connectées
                    g2d.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
                }
            }
        }

        // Dessiner les nœuds (villes)
        for (Node node : graph.getNodes()) {
            Point pos = nodePositions.get(node.getId());
            if (pos != null) {
                // Cercle pour la ville
                g2d.setColor(Color.BLUE);
                g2d.fillOval(pos.x - nodeRadius / 2, pos.y - nodeRadius / 2, nodeRadius, nodeRadius);

                // Nom de la ville
                g2d.setColor(Color.BLACK);
                g2d.drawString(node.getId(), pos.x - nodeRadius, pos.y - nodeRadius);
            }
        }
    }
}

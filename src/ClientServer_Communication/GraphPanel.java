package ClientServer_Communication;

import roadnetwork.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom panel for visualizing a graph, including its nodes and edges.
 * The nodes are represented as circles, and edges are drawn as lines connecting them.
 */
public class GraphPanel extends JPanel {
    private final Graph graph; // The graph instance to be visualized.
    private final int nodeRadius = 20; // The radius of each node in the visualization.
    private final Map<String, Point> nodePositions; // Predefined positions for the graph's nodes.

    /**
     * Constructs a GraphPanel with the specified graph.
     *
     * @param graph The graph to be visualized in the panel.
     */
    public GraphPanel(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(800, 400)); // Set the panel's preferred size.
        this.nodePositions = initializeNodePositions(); // Initialize predefined positions for nodes.
    }

    /**
     * Initializes predefined positions for the graph's nodes.
     *
     * @return A map where each node's ID is mapped to a specific position.
     */
    private Map<String, Point> initializeNodePositions() {
        Map<String, Point> positions = new HashMap<>();

        // Define the positions for each node in the graph.
        positions.put("Genève", new Point(50, 200));
        positions.put("Lausanne", new Point(250, 100));
        positions.put("Berne", new Point(550, 100));
        positions.put("Neuchatel", new Point(250, 300));
        positions.put("Montreux", new Point(550, 300));

        return positions;
    }

    /**
     * Paints the graph's nodes and edges on the panel.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ensure the panel's background is painted.
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother rendering of shapes and lines.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw edges between nodes.
        g2d.setStroke(new BasicStroke(2)); // Set the stroke width for edges.
        g2d.setColor(Color.GRAY); // Set the color for edges.
        for (Node from : graph.getNodes()) {
            Point fromPos = nodePositions.get(from.getId()); // Get the position of the source node.
            for (Edge edge : graph.getEdges(from)) {
                Node to = edge.getTo(); // Get the destination node.
                Point toPos = nodePositions.get(to.getId()); // Get the position of the destination node.
                if (fromPos != null && toPos != null) {
                    // Draw a line representing the edge between the two nodes.
                    g2d.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
                }
            }
        }

        // Draw nodes.
        for (Node node : graph.getNodes()) {
            Point pos = nodePositions.get(node.getId()); // Get the position of the node.
            if (pos != null) {
                // Draw the node as a filled circle.
                g2d.setColor(Color.BLUE);
                g2d.fillOval(pos.x - nodeRadius / 2, pos.y - nodeRadius / 2, nodeRadius, nodeRadius);

                // Draw the node's ID next to it.
                g2d.setColor(Color.BLACK);
                g2d.drawString(node.getId(), pos.x - nodeRadius, pos.y - nodeRadius);
            }
        }
    }
}

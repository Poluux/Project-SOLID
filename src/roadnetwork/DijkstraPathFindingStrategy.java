package roadnetwork;

import java.util.*;

/**
 * Implements the Dijkstra algorithm for finding the shortest path in a graph.
 */
public class DijkstraPathFindingStrategy implements PathFindingStrategy {

    /**
     * Finds the shortest path between two nodes in a graph using Dijkstra's algorithm.
     *
     * @param graph The graph instance containing the nodes and edges.
     * @param start The starting node.
     * @param end   The destination node.
     * @return A string representing the shortest path and its total distance.
     */
    @Override
    public String findPath(Graph graph, Node start, Node end) {
        Map<Node, Double> distances = new HashMap<>(); // Tracks the shortest distance to each node.
        Map<Node, Node> previousNodes = new HashMap<>(); // Tracks the previous node in the shortest path.
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get)); // Priority queue for node exploration.

        // Initialize distances to infinity for all nodes except the start node.
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0); // Distance to the start node is zero.
        queue.add(start);

        // Explore the graph to find the shortest paths.
        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get the node with the smallest distance.

            // Stop if we reach the destination node.
            if (current.equals(end)) break;

            // Update distances to neighboring nodes.
            for (Edge edge : graph.getEdges(current)) {
                Node neighbor = edge.getTo();
                double newDist = distances.get(current) + edge.getTravelTime();

                // Update the shortest distance if a better path is found.
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruct the shortest path from the start to the end.
        List<Node> path = new ArrayList<>();
        Node currentNode = end;

        while (currentNode != null && previousNodes.containsKey(currentNode)) {
            path.add(0, currentNode); // Add the node at the start of the path.
            currentNode = previousNodes.get(currentNode);
        }

        // Add the start node if the path is valid.
        if (start.equals(currentNode)) {
            path.add(0, start);
        }

        // Construct the result string.
        StringBuilder result = new StringBuilder();
        if (path.isEmpty() || !path.get(0).equals(start)) {
            result.append("No path found from ").append(start.getId()).append(" to ").append(end.getId());
        } else {
            result.append("Shortest path from ").append(start.getId()).append(" to ").append(end.getId()).append(": ");
            for (Node node : path) {
                result.append(node.getId()).append(" ");
            }
            result.append("\nTotal distance: ").append(distances.get(end)).append(" min");
        }

        // Append the end-of-response marker.
        result.append("\nEND_OF_RESPONSE");

        return result.toString();
    }
}
package roadnetwork;

import java.util.*;

// Implémentation de la stratégie de calcul de chemin basée sur Dijkstra
public class DijkstraPathFindingStrategy implements PathFindingStrategy {

    // Calcule le plus court chemin entre deux nœuds dans le graphe et retourne une description sous forme de chaîne
    @Override
    public String findPath(Graph graph, Node start, Node end) {
        // Map pour stocker la distance minimale de chaque nœud au nœud de départ
        Map<Node, Double> distances = new HashMap<>();
        // Map pour stocker le prédécesseur de chaque nœud dans le chemin
        Map<Node, Node> previousNodes = new HashMap<>();
        // File de priorité pour explorer les nœuds avec la distance minimale
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        // Initialisation des distances : infini pour tous sauf le nœud de départ
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);

        queue.add(start);

        // Exploration des nœuds
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Si le nœud final est atteint, on arrête
            if (current.equals(end)) break;

            // Mise à jour des voisins
            for (Edge edge : graph.getEdges(current)) {
                Node neighbor = edge.getTo();
                double newDist = distances.get(current) + edge.getTravelTime();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruction du chemin le plus court
        List<Node> path = new ArrayList<>();
        Node currentNode = end;

        while (currentNode != null && previousNodes.containsKey(currentNode)) {
            path.add(0, currentNode); // Ajouter le nœud au début
            currentNode = previousNodes.get(currentNode);
        }

        if (start.equals(currentNode)) {
            path.add(0, start);
        }

        // Construire le résultat sous forme de chaîne
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

        // Retourner le chemin comme chaîne
        return result.toString();
    }
}
package roadnetwork;

import java.util.*;

// Implémentation de l'algorithme de Dijkstra
public class DijkstraPathFindingStrategy implements PathFindingStrategy {

    @Override
    public void findPath(Graph graph, Node start, Node end) {
        // Priorité pour choisir le nœud avec la distance minimale
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> previousNodes = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        // Initialisation des distances
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0); // Distance au point de départ est 0

        queue.add(start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Si on atteint le nœud final, on arrête
            if (current.equals(end)) break;

            // Parcours des voisins
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

        // Construction du chemin le plus court
        List<Node> path = new ArrayList<>();
        Node currentNode = end;

        while (currentNode != null && previousNodes.containsKey(currentNode)) {
            path.add(0, currentNode); // Ajout au début de la liste
            currentNode = previousNodes.get(currentNode);
        }

        // Ajout du point de départ s'il fait partie du chemin
        if (start.equals(currentNode)) {
            path.add(0, start);
        }

        // Affichage du chemin ou message d'erreur
        if (path.isEmpty() || !path.get(0).equals(start)) {
            System.out.println("No path found from " + start + " to " + end);
        } else {
            System.out.println("Shortest path from " + start + " to " + end + ": " + path);
            System.out.println("Total distance: " + distances.get(end));
        }
    }
}
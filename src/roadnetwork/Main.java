package roadnetwork;

// Classe principale avec logique de démonstration
public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Création des nœuds
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");

        // Ajout des nœuds au graphe
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Ajout des arêtes
        graph.addEdge(nodeA, nodeB, 10.0);
        graph.addEdge(nodeA, nodeC, 15.0);
        graph.addEdge(nodeB, nodeD, 20.0);
        graph.addEdge(nodeC, nodeD, 25.0);

        // Utilisation de Dijkstra pour trouver le plus court chemin
        PathFindingStrategy dijkstra = new DijkstraPathFindingStrategy();
        dijkstra.findPath(graph, nodeA, nodeD);
    }
}
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

        // Ajout des arêtes via la Factory
        graph.addEdge(nodeA, nodeB, 10.0); // A -> B
        graph.addEdge(nodeA, nodeC, 15.0); // A -> C
        graph.addEdge(nodeB, nodeD, 20.0); // B -> D
        graph.addEdge(nodeC, nodeD, 25.0); // C -> D

        // Afficher le graphe complet
        System.out.println(graph);

        // Afficher les connexions spécifiques
        System.out.println("Edges from A: " + graph.getEdges(nodeA));
        System.out.println("Edges from B: " + graph.getEdges(nodeB));
        System.out.println("Edges from C: " + graph.getEdges(nodeC));
        System.out.println("Edges from D: " + graph.getEdges(nodeD));

        // Exemple d'utilisation de la stratégie
        PathFindingStrategy pathFinding = new DijkstraPathFindingStrategy();
        pathFinding.findPath(graph, nodeA, nodeD);
    }
}
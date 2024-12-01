public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Création des nœuds
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");

        // Ajout des nœuds au graphe
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);

        // Ajout des arêtes
        graph.addEdge(nodeA, nodeB, 10.5);
        graph.addEdge(nodeB, nodeC, 5.0);
        graph.addEdge(nodeA, nodeC, 20.0);

        // Afficher le graphe
        System.out.println(graph);

        // Récupérer les connexions
        System.out.println("Edges from A: " + graph.getEdges(nodeA));
    }
}
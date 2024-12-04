package roadnetwork;

// Classe responsable de l'initialisation des nœuds et des arêtes dans un graphe
public class GraphInitializer {

    // Méthode pour créer et retourner un graphe préconfiguré
    public static Graph initializeGraph() {
        Graph graph = new Graph();

        // Création des nœuds
        Node nodeGeneve = new Node("Genève");
        Node nodeLausanne = new Node("Lausanne");
        Node nodeNeuchatel = new Node("Neuchatel");
        Node nodeMontreux = new Node("Montreux");
        Node nodeBerne = new Node("Berne");

        // Ajout des nœuds au graphe
        graph.addNode(nodeGeneve);
        graph.addNode(nodeLausanne);
        graph.addNode(nodeNeuchatel);
        graph.addNode(nodeMontreux);
        graph.addNode(nodeBerne);

        // Ajout des arêtes entre les nœuds
        graph.addEdge(nodeGeneve, nodeNeuchatel, 90.0);    // Genève → Neuchâtel
        graph.addEdge(nodeGeneve, nodeLausanne, 35.0);     // Genève → Lausanne

        graph.addEdge(nodeLausanne, nodeNeuchatel, 60.0);  // Lausanne → Neuchâtel
        graph.addEdge(nodeLausanne, nodeBerne, 60.0);      // Lausanne → Berne
        graph.addEdge(nodeLausanne, nodeMontreux, 30.0);   // Lausanne → Montreux
        graph.addEdge(nodeLausanne, nodeGeneve, 35.0);     // Lausanne → Genève

        graph.addEdge(nodeNeuchatel, nodeMontreux, 80.0);  // Neuchâtel → Montreux
        graph.addEdge(nodeNeuchatel, nodeLausanne, 60.0);  // Neuchâtel → Lausanne
        graph.addEdge(nodeNeuchatel, nodeGeneve, 90.0);    // Neuchâtel → Genève

        graph.addEdge(nodeBerne, nodeMontreux, 90.0);      // Berne → Montreux
        graph.addEdge(nodeBerne, nodeLausanne, 60.0);      // Berne → Lausanne

        graph.addEdge(nodeMontreux, nodeBerne, 90.0);      // Montreux → Berne
        graph.addEdge(nodeMontreux, nodeNeuchatel, 80.0);  // Montreux → Neuchâtel
        graph.addEdge(nodeMontreux, nodeLausanne, 30.0);   // Montreux → Lausanne

        // Retourne le graphe initialisé
        return graph;
    }
}

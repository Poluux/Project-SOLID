package roadnetwork;

/**
 * Responsible for initializing nodes and edges in a graph.
 */
public class GraphInitializer {

    /**
     * Creates and returns a preconfigured graph.
     *
     * @return A graph with predefined nodes and edges.
     */
    public static Graph initializeGraph() {
        Graph graph = new Graph();

        // Create nodes.
        Node nodeGeneve = new Node("Genève");
        Node nodeLausanne = new Node("Lausanne");
        Node nodeNeuchatel = new Node("Neuchatel");
        Node nodeMontreux = new Node("Montreux");
        Node nodeBerne = new Node("Berne");

        // Add nodes to the graph.
        graph.addNode(nodeGeneve);
        graph.addNode(nodeLausanne);
        graph.addNode(nodeNeuchatel);
        graph.addNode(nodeMontreux);
        graph.addNode(nodeBerne);

        // Add edges between nodes with travel times.
        graph.addEdge(nodeGeneve, nodeNeuchatel, 90.0);
        graph.addEdge(nodeGeneve, nodeLausanne, 35.0);

        graph.addEdge(nodeLausanne, nodeNeuchatel, 60.0);
        graph.addEdge(nodeLausanne, nodeBerne, 60.0);
        graph.addEdge(nodeLausanne, nodeMontreux, 30.0);
        graph.addEdge(nodeLausanne, nodeGeneve, 35.0);

        graph.addEdge(nodeNeuchatel, nodeMontreux, 80.0);
        graph.addEdge(nodeNeuchatel, nodeLausanne, 60.0);
        graph.addEdge(nodeNeuchatel, nodeGeneve, 90.0);

        graph.addEdge(nodeBerne, nodeMontreux, 90.0);
        graph.addEdge(nodeBerne, nodeLausanne, 60.0);

        graph.addEdge(nodeMontreux, nodeBerne, 90.0);
        graph.addEdge(nodeMontreux, nodeNeuchatel, 80.0);
        graph.addEdge(nodeMontreux, nodeLausanne, 30.0);

        return graph; // Return the initialized graph.
    }
}
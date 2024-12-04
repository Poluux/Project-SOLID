package roadnetwork;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialisation du graphe via la classe GraphInitializer
        Graph graph = GraphInitializer.initializeGraph();

        // Affichage des informations du graphe
        System.out.println("Voici les nœuds disponibles dans le graphe :");
        for (Node node : graph.getNodes()) {
            System.out.println("- " + node.getId());
        }

        // Scanner pour récupérer les entrées utilisateur
        Scanner scanner = new Scanner(System.in);

        // Demander le point de départ
        System.out.print("\nVeuillez entrer le point de départ : ");
        String startName = scanner.nextLine();

        // Demander le point d'arrivée
        System.out.print("Veuillez entrer le point d'arrivée : ");
        String endName = scanner.nextLine();

        // Vérifier si les nœuds existent dans le graphe
        Node startNode = null;
        Node endNode = null;

        for (Node node : graph.getNodes()) {
            if (node.getId().equalsIgnoreCase(startName)) {
                startNode = node;
            }
            if (node.getId().equalsIgnoreCase(endName)) {
                endNode = node;
            }
        }

        // Si un des nœuds n'existe pas, afficher un message d'erreur
        if (startNode == null || endNode == null) {
            System.out.println("\nErreur : Le point de départ ou d'arrivée n'existe pas dans le graphe.");
            System.out.println("Veuillez vérifier les nœuds disponibles et réessayer.");
        } else {
            // Utilisation de Dijkstra pour trouver le plus court chemin
            PathFindingStrategy dijkstra = new DijkstraPathFindingStrategy();
            System.out.println("\nCalcul du chemin le plus court...");
            dijkstra.findPath(graph, startNode, endNode);
        }

        // Fermeture du scanner
        scanner.close();
    }
}
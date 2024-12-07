package roadnetwork;
import com.sun.security.jgss.GSSUtil;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialisation du graphe via la classe GraphInitializer
        Graph graph = GraphInitializer.initializeGraph();

        // Scanner pour récupérer les entrées utilisateur
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        graph.printGraph();

        // Menu principal
        while (running) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Trouver le chemin le plus court entre deux villes");
            System.out.println("2. Modifier le temps de trajet entre deux villes");
            System.out.println("3. Réinitialiser les temps de trajet par défaut");
            System.out.println("4. Quitter");
            System.out.print("Votre choix : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consomme la ligne restante

            switch (choice) {
                case 1:
                    // Trouver le chemin le plus court
                    System.out.print("\nEntrez le point de départ : ");
                    String startName = scanner.nextLine();

                    System.out.print("Entrez le point d'arrivée : ");
                    String endName = scanner.nextLine();

                    Node startNode = getNodeByName(graph, startName);
                    Node endNode = getNodeByName(graph, endName);

                    if (startNode == null || endNode == null) {
                        System.out.println("Erreur : L'une des villes saisies n'existe pas.");
                    } else {
                        PathFindingStrategy dijkstra = new DijkstraPathFindingStrategy();
                        // Appeler findPath et récupérer la chaîne de résultat
                        String result = dijkstra.findPath(graph, startNode, endNode);
                        // Afficher le résultat
                        System.out.println(result);
                    }
                    break;

                case 2:
                    // Modifier le temps de trajet
                    System.out.print("\nEntrez la ville de départ : ");
                    String fromName = scanner.nextLine();

                    System.out.print("Entrez la ville d'arrivée : ");
                    String toName = scanner.nextLine();

                    System.out.print("Entrez le nouveau temps de trajet (en minutes) : ");
                    double newTravelTime = scanner.nextDouble();

                    Node fromNode = getNodeByName(graph, fromName);
                    Node toNode = getNodeByName(graph, toName);

                    if (fromNode == null || toNode == null) {
                        System.out.println("Erreur : L'une des villes saisies n'existe pas.");
                    } else {
                        boolean updated = graph.updateTravelTime(fromNode, toNode, newTravelTime);
                        if (updated) {
                            System.out.println("Le temps de trajet a été mis à jour avec succès.");
                        } else {
                            System.out.println("Erreur : Le trajet entre ces villes n'existe pas.");
                        }
                    }
                    break;

                case 3:
                    // Réinitialiser les temps de trajet
                    graph.resetTravelTimes();
                    System.out.println("Tous les temps de trajet ont été réinitialisés à leurs valeurs par défaut.");
                    break;

                case 4:
                    // Quitter le programme
                    running = false;
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        // Fermeture du scanner
        scanner.close();
    }

    // Méthode utilitaire pour récupérer un nœud par son nom
    private static Node getNodeByName(Graph graph, String nodeName) {
        for (Node node : graph.getNodes()) {
            if (node.getId().equalsIgnoreCase(nodeName)) {
                return node;
            }
        }
        return null;
    }
}
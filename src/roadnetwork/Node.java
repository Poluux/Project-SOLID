package roadnetwork;

import java.util.Objects;

// Classe représentant un nœud dans le graphe
public class Node {
    private final String id; // Identifiant unique du nœud

    // Constructeur : Initialise un nœud avec un identifiant
    public Node(String id) {
        this.id = id;
    }

    // Retourne l'identifiant du nœud
    public String getId() {
        return id;
    }

    // Vérifie si deux nœuds sont identiques en comparant leurs identifiants
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return Objects.equals(id, node.id);
    }

    // Génère un code de hachage unique basé sur l'identifiant du nœud
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Représentation en chaîne de caractères du nœud (utile pour le débogage)
    @Override
    public String toString() {
        return "Node{" + "id='" + id + '\'' + '}';
    }
}
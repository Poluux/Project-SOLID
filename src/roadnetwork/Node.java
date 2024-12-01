package roadnetwork;

import java.util.Objects;

// Classe représentant un nœud
public class Node {
    private final String id; // Identifiant unique du nœud

    public Node(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "roadnetwork.Node{" + "id='" + id + '\'' + '}';
    }
}
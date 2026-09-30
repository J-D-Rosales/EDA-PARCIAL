package Problems.plantillas;

public class PersistentQueue {
    public static class Node {
        public int val;
        public Node[] up = new Node[20]; // Punteros a saltos de potencia de 2 (Binary Lifting)
        public int depth;

        public Node(int val, Node parent) {
            this.val = val;
            this.up[0] = parent;
            this.depth = (parent == null ? 0 : parent.depth) + 1;

            // Llenamos la tabla de saltos binarios para O(log N)
            for (int i = 1; i < 20; i++) {
                if (up[i - 1] != null) {
                    up[i] = up[i - 1].up[i - 1];
                }
            }
        }
    }

    public Node[] roots;

    public PersistentQueue(int maxVersions) {
        this.roots = new Node[maxVersions + 2];
        this.roots[0] = null;
    }

    // Enqueue: agregar al final en O(log N)
    public void enqueue(int version, int prevVersion, int val) {
        roots[version] = new Node(val, roots[prevVersion]);
    }

    // Front: ver el primero que entró en O(log N)
    public int front(int version) {
        Node curr = roots[version];
        if (curr == null) return -1;

        // Saltamos hasta el nodo más profundo (el más antiguo)
        for (int i = 19; i >= 0; i--) {
            if (curr.up[i] != null) {
                curr = curr.up[i];
            }
        }
        return curr.val;
    }
}
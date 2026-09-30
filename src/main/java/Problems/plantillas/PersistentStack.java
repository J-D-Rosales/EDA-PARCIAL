package Problems.plantillas;

public class PersistentStack<T> {
    public static class Node<T> {
        public T val;
        public Node<T> next; // Apunta al elemento que estaba abajo
        public int size;

        public Node(T val, Node<T> next) {
            this.val = val;
            this.next = next;
            this.size = (next == null ? 0 : next.size) + 1;
        }
    }

    public Node<T>[] roots;

    @SuppressWarnings("unchecked")
    public PersistentStack(int maxVersions) {
        this.roots = new Node[maxVersions + 2];
        this.roots[0] = null; // Pila vacía en versión 0
    }

    // Push: O(1)
    public void push(int version, int prevVersion, T val) {
        roots[version] = new Node<>(val, roots[prevVersion]);
    }

    // Pop: O(1)
    public void pop(int version, int prevVersion) {
        if (roots[prevVersion] != null) {
            roots[version] = roots[prevVersion].next;
        } else {
            roots[version] = null;
        }
    }

    // Top: O(1)
    public T peek(int version) {
        return roots[version] != null ? roots[version].val : null;
    }
}
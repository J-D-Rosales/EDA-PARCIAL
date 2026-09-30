package Problems.plantillas;

public class PersistentSegmentTree {
    public static class Node {
        public Node left, right;
        public long sum; // O val, min, max según el problema

        public Node() {}

        public Node(Node old) {
            if (old != null) {
                this.left = old.left;
                this.right = old.right;
                this.sum = old.sum;
            }
        }
    }

    public Node[] roots;
    private final int N;

    public PersistentSegmentTree(int maxVersions, int n) {
        this.N = n;
        this.roots = new Node[maxVersions + 2];
        this.roots[0] = build(1, N); // Versión 0 vacía/inicial
    }

    private Node build(int l, int r) {
        Node curr = new Node();
        if (l == r) return curr;
        int mid = l + (r - l) / 2;
        curr.left = build(l, mid);
        curr.right = build(mid + 1, r);
        return curr;
    }

    // Actualización puntual: modifica la posición 'pos' sumando 'val'
    public void update(int version, int prevVersion, int pos, long val) {
        roots[version] = update(roots[prevVersion], 1, N, pos, val);
    }

    private Node update(Node prev, int l, int r, int pos, long val) {
        Node curr = new Node(prev);
        curr.sum += val;

        if (l == r) return curr;

        int mid = l + (r - l) / 2;
        if (pos <= mid) {
            curr.left = update(prev != null ? prev.left : null, l, mid, pos, val);
        } else {
            curr.right = update(prev != null ? prev.right : null, mid + 1, r, pos, val);
        }
        return curr;
    }

    // Consulta en rango [ql, qr] en una versión específica
    public long query(int version, int ql, int qr) {
        return query(roots[version], 1, N, ql, qr);
    }

    private long query(Node curr, int l, int r, int ql, int qr) {
        if (curr == null || ql > r || qr < l) return 0;
        if (ql <= l && r <= qr) return curr.sum;

        int mid = l + (r - l) / 2;
        return query(curr.left, l, mid, ql, qr) + query(curr.right, mid + 1, r, ql, qr);
    }
}
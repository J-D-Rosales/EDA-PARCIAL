package Problems;

import java.util.ArrayList;
import java.util.List;

public class SegmentTreePersistance {

    public List<SegmentTreeNode> roots;
    public int N; // tamaño del arreglo original

    public SegmentTreePersistance(int[] originalArray) {
        if (originalArray == null || originalArray.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o vacío.");
        }

        this.N = originalArray.length;
        this.roots = new ArrayList<>();

        SegmentTreeNode rootVersion0 = buildRecursivo(0, N - 1, originalArray);
        this.roots.add(rootVersion0);
    }

    static public class SegmentTreeNode {
        int value;
        SegmentTreeNode leftChild, rightChild;

        public SegmentTreeNode(int value, SegmentTreeNode leftChild, SegmentTreeNode rightChild) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public SegmentTreeNode(SegmentTreeNode leftChild, SegmentTreeNode rightChild) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;

            int leftVal = (leftChild != null) ? leftChild.value : 0;
            int rightVal = (rightChild != null) ? rightChild.value : 0;
            this.value = leftVal + rightVal;
        }
    }

    private SegmentTreeNode buildRecursivo(int left, int right, int[] originalArray) {
        // Caso Hoja: left == right
        if (left == right) {
            return new SegmentTreeNode(originalArray[left], null, null);
        }

        int mitad = (left + right) / 2;

        // Caso Recursivo
        SegmentTreeNode leftChild = buildRecursivo(left, mitad, originalArray);
        SegmentTreeNode rightChild = buildRecursivo(mitad + 1, right, originalArray);

        return new SegmentTreeNode(leftChild.value + rightChild.value, leftChild, rightChild);
    }

    public int update(int posicion, int newValue, int version) {
        if (version < 0 || version >= roots.size()) {
            throw new IndexOutOfBoundsException("La versión " + version + " no existe.");
        }

        SegmentTreeNode rootPrev = this.roots.get(version);
        SegmentTreeNode newRoot = updateRecursivo(rootPrev, 0, N-1, posicion, newValue);
        this.roots.add(newRoot);

        return this.roots.size() - 1; // Retorna el índice de la versión recién creada
    }

    private SegmentTreeNode updateRecursivo(SegmentTreeNode prevNode, int left, int right, int pos, int newValue){
        // Aqui se asegura que la posicion que se quiere actualizar este dentro del rango del arreglo
        if (left == right) {
            return new SegmentTreeNode(newValue, null, null);
        }
        int mid = (left + right) / 2;

        SegmentTreeNode newLeftChild;
        SegmentTreeNode newRightChild;

        if (pos <= mid) {
            newLeftChild = updateRecursivo(prevNode.leftChild, left, mid, pos, newValue);
            newRightChild = prevNode.rightChild;
        } else {
            newLeftChild = prevNode.leftChild;
            newRightChild = updateRecursivo(prevNode.rightChild, mid + 1, right, pos, newValue);
        }

        return  new SegmentTreeNode(newLeftChild, newRightChild);
    }

    public int query(int qL, int qR, int version){
        // 3 casos posibles
        if (version < 0 || version > this.roots.size() - 1) {
            throw new IndexOutOfBoundsException("La versión " + version + " no existe.");
        }
        SegmentTreeNode root = this.roots.get(version);
        if (root == null) {
            return 0; // Si la raíz fuera nula, la suma es 0 (no -1)
        }

        return queryRecursivo(root, 0, N - 1, qL, qR);
    }
    private int queryRecursivo(SegmentTreeNode root, int L, int R, int qL, int qR){

        if (root == null) {
            return 0;
        }
        if (qL > R || qR < L) {
            return 0;
        }
        if (qL <= L && R <= qR) {
            return root.value;
        }
        int mid = (L + R) / 2;
        int sumaIzq = queryRecursivo(root.leftChild, L, mid, qL, qR);
        int sumaDer = queryRecursivo(root.rightChild, mid + 1, R, qL, qR);
        return sumaIzq + sumaDer;
    }



    // Códigos de colores ANSI
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_AMARILLO = "\u001B[33m";
    public static final String ANSI_VERDE = "\u001B[32m";

    public void imprimir(int version) {
        if (version < 0 || version >= roots.size()) {
            System.out.println("La versión " + version + " no existe.");
            return;
        }

        System.out.println("=== ÁRBOL SEGMENT TREE (VERSIÓN " + version + ") ===");
        SegmentTreeNode root = roots.get(version);
        imprimirRecursivo(root, 0, N - 1, 0);
    }

    private void imprimirRecursivo(SegmentTreeNode nodo, int L, int R, int depth) {
        if (nodo == null) return;

        StringBuilder indent = new StringBuilder();
        indent.repeat("   ", Math.max(0, depth));

        if (L == R) {
            // Hoja en VERDE
            System.out.println(indent + ANSI_VERDE + "└── Hoja [" + L + "]: valor = " + nodo.value + ANSI_RESET);
        } else {
            // Nodo Interno en AMARILLO
            System.out.println(indent + ANSI_AMARILLO + "├── Rango [" + L + ".." + R + "]: suma = " + nodo.value + ANSI_RESET);
        }

        int mid = (L + R) / 2;
        imprimirRecursivo(nodo.leftChild, L, mid, depth + 1);
        imprimirRecursivo(nodo.rightChild, mid + 1, R, depth + 1);
    }

}


import java.util.ArrayList;
import java.util.List;
public class Main{

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

public static class SegmentTreePersistance {

    private List<SegmentTreeNode> roots;
    private int N; // tamaño del arreglo original

    public SegmentTreePersistance(int[] originalArray) {
        if (originalArray == null || originalArray.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo o vacío.");
        }

        this.N = originalArray.length;
        this.roots = new ArrayList<>();

        SegmentTreeNode rootVersion0 = buildRecursivo(0, N - 1, originalArray);
        this.roots.add(rootVersion0);
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
}
public static void problema2() throws IOException {
BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
String line = reader.readLine();
if (line == null) return;

        int n = Integer.parseInt(line.trim());
        int[] array = new int[n];

        line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line);

        for (int i = 0; i < n; i++) {
            array[i] = Integer.parseInt(tokenizer.nextToken());
        }

        SegmentTreePersistance persistance = new SegmentTreePersistance(array);

        // Usamos StringBuilder para la salida rápida en Java
        StringBuilder sb = new StringBuilder();

        line = reader.readLine();
        if (line == null) return;
        int m = Integer.parseInt(line.trim());

        for (int i = 0; i < m; i++) {
            line = reader.readLine();
            if (line == null) break;
            tokenizer = new StringTokenizer(line);
            String texto = tokenizer.nextToken();

            if (texto.equals("create")) {
                int version = Integer.parseInt(tokenizer.nextToken()) - 1;  // Ajuste Base 0
                int posicion = Integer.parseInt(tokenizer.nextToken()) - 1; // Ajuste Base 0
                int valor = Integer.parseInt(tokenizer.nextToken());

                persistance.update(posicion, valor, version);
            }
            else if (texto.equals("get")) {
                int version = Integer.parseInt(tokenizer.nextToken()) - 1;  // Ajuste Base 0
                int posicion = Integer.parseInt(tokenizer.nextToken()) - 1; // Ajuste Base 0

                // Consultamos la posición en la versión indicada
                int resultado = persistance.query(posicion, posicion, version);
                sb.append(resultado).append("\n");
            }
        }

        System.out.print(sb);
    }

    public static void main(String[] args) throws IOException {
        problema2();
    }
}
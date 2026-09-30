package Problems.rangeTree;

// Static for method build, dinamic with  method add(),
// For this implmentation we've done the static one.


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RangeTree {
    private RangeNode root;
    private int currentDim;
    private int totalDims; // maxima dimensión dle arbol

    public RangeTree(List<Pointd> points, int currentDim, int totalDims) {
        this.currentDim = currentDim;
        this.totalDims = totalDims;
        this.root = buildTree(points, currentDim, totalDims);
    }

    // done
    public RangeNode buildTree(List<Pointd> points, int currentDim, int totalDims) {
        // Para la dimensión X
        if (points.isEmpty()) {
            return null;
        }
        // Caso donde es una hoja
        if (points.size() == 1) {
            return new RangeNode(currentDim, points.getFirst());
        }


        // Ordenamos los puntos dados por la dimension dada
        List<Pointd> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort(Comparator.comparingInt(a -> a.getDimension(currentDim)));

        // Mediana de particion
        int mid = (sortedPoints.size() - 1) / 2;
        RangeNode mediana = new  RangeNode(currentDim, sortedPoints.get(mid));
        // Hacemos los left y right para la mediana

        mediana.left = buildTree(sortedPoints.subList(0,mid+1), currentDim, totalDims);
        mediana.right = buildTree(sortedPoints.subList(mid+1, sortedPoints.size()), currentDim, totalDims);

        if (currentDim + 1 < totalDims) {
            mediana.associatedTree = new RangeTree(sortedPoints, currentDim+1, totalDims); // toma a build denuevo
        }
        return mediana;
    }

    public List<Pointd> query(List<Integer> queryPointA, List<Integer> queryPointB) {
        List<List<Integer>> ranges = new ArrayList<>();
        ranges.add(queryPointA);
        ranges.add(queryPointB);
        return queryRecursivo(ranges, root, currentDim);
    }

    public List<Pointd> queryRecursivo(List<List<Integer>> rangos, RangeNode nodo, int currentDim ){
        if(rangos == null  || rangos.isEmpty() || nodo == null){
            return new ArrayList<>();
        }
        // Stage one, Encontrar el SplitNode
        RangeNode splitNode = encontrarSplitNode(
                rangos.get(currentDim).get(0),
                rangos.get(currentDim).get(1),
                nodo,
                currentDim
        );
        if (splitNode == null) {
            return new ArrayList<>();
        }
        // Stage 2 - Traversing
        List<Pointd> resultado = new ArrayList<>();

        // EL splitNode puede ser una hoja
        if (isLeaf(splitNode)) {
            if (isInRange(splitNode,rangos)) {
                resultado.add(splitNode.punto);
            }
            return resultado;
        }

        int min = rangos.get(currentDim).get(0);
        int max = rangos.get(currentDim).get(1);

        // Traversing for Left
        RangeNode current = splitNode.left;
        while (current != null){
            // Es una hoja?
            if (isLeaf(current)){
                if (isInRange(current,rangos)) {
                    resultado.add(current.punto);
                }
                break;
            }
            if (current.punto.getDimension(currentDim) >= min){
                // Canonical node
                if (current.right!=null) {
                    resultado.addAll(reportSubTree(rangos, current.right, currentDim)); // irá por el associated tree, o todas las hojas, en todo caso
                }
                current = current.left;
            }
            else {
                current = current.right;
            }
        }

        // Traversing for right
        current =  splitNode.right;
        while (current != null){
            if (isLeaf(current)){
                if (isInRange(current,rangos)) {
                    resultado.add(current.punto);
                }
                break;
            }
            if (current.punto.getDimension(currentDim) <= max){
                // Canonical node
                if (current.left!=null) {
                    resultado.addAll(reportSubTree(rangos, current.left, currentDim));
                }
                current = current.right;
            }
            else {
                current = current.left;
            }
        }
        return resultado;
    }

    private List<Pointd> reportSubTree(List<List<Integer>> rangos, RangeNode nodo, int currentDim){
        if (nodo == null) return new ArrayList<>();

        if(currentDim + 1 < totalDims && nodo.associatedTree!=null){
            // hacemos una query al associated tree
            return nodo.associatedTree.queryRecursivo(rangos, nodo.associatedTree.root, currentDim + 1);
        }
        else{
            return queryAllLeafs(nodo,rangos);
        }
    }
    private List<Pointd> queryAllLeafs(RangeNode nodo, List<List<Integer>> rangos) {
        List<Pointd> resultado = new ArrayList<>();
        queryAllLeafsRecursivo(nodo, rangos, resultado);
        return resultado;
    }

    private void queryAllLeafsRecursivo(RangeNode nodo, List<List<Integer>> rangos, List<Pointd> resultado) {
        if (nodo == null) {
            return;
        }
        if (isLeaf(nodo)) {
            // Validar la hoja directamente
            if (isInRange(nodo, rangos)) {
                resultado.add(nodo.punto);
            }
        } else {
            queryAllLeafsRecursivo(nodo.left, rangos, resultado);
            queryAllLeafsRecursivo(nodo.right, rangos, resultado);
        }
    }
    private boolean isInRange(RangeNode nodo, List<List<Integer>> rangos){
        if (nodo == null || nodo.punto == null) return false;

        for (int i = 0; i < rangos.size(); i++){
            int min = rangos.get(i).get(0);
            int max = rangos.get(i).get(1);
            int val = nodo.punto.getDimension(i);
            if (val < min || val > max) {
                return false;
            }
        }
        return true;
    }

    private boolean isLeaf(RangeNode node){
        return node != null && node.left == null && node.right == null;
    }
    private RangeNode encontrarSplitNode(int start, int end, RangeNode nodo, int dimension) {
        if (nodo == null){
            return null;
        }

        if (nodo.left == null &&  nodo.right == null){
            return nodo;
        }

        // en caso sea split node
        int mid = nodo.punto.getDimension(dimension);
        if (start <= mid &&  mid <= end) {  // SPLIT NODE!!
            return nodo;
        }
        else if (end < mid) {
            return  encontrarSplitNode(start, end, nodo.left, dimension);
        }
        else {
            return  encontrarSplitNode(start, end, nodo.right, dimension);
        }
    }

    // Códigos de color ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";    // Nodos internos
    private static final String GREEN = "\u001B[32m";   // Hojas (Puntos)
    private static final String YELLOW = "\u001B[33m";  // Árboles asociados

    public void print() {
        printTree(this.root, "", true, "DIM " + this.currentDim);
    }

    private void printTree(RangeNode node, String prefix, boolean isTail, String contextLabel) {
        if (node == null) return;

        // Distinguir si es Hoja (sin hijos) o Nodo Interno
        boolean isLeaf = (node.left == null && node.right == null);

        // Formatear texto según el tipo de nodo
        String nodeText;
        if (isLeaf) {
            nodeText = GREEN + "[HOJA: " + node.punto + "]" + RESET;
        } else {
            nodeText = CYAN + "[PIVOTE Dim " + node.currentDim + ": " + node.punto.getDimension(node.currentDim) + "]" + RESET;
        }

        System.out.println(prefix + (isTail ? "└── " : "├── ") + nodeText + " (" + contextLabel + ")");

        // Imprimir subárbol asociado si existe
        if (node.associatedTree != null) {
            String assocPrefix = prefix + (isTail ? "    " : "│   ");
            System.out.println(assocPrefix + YELLOW + "└── ↳ [ÁRBOL ASOCIADO Dim " + (node.currentDim + 1) + "]:" + RESET);
            node.associatedTree.printTree(
                    node.associatedTree.root,
                    assocPrefix + "    ",
                    true,
                    "Sub-Dim " + (node.currentDim + 1)
            );
        }

        // Recorrer hijos en la misma dimensión
        List<RangeNode> children = new ArrayList<>();
        if (node.left != null) children.add(node.left);
        if (node.right != null) children.add(node.right);

        for (int i = 0; i < children.size(); i++) {
            boolean last = (i == children.size() - 1);
            printTree(children.get(i), prefix + (isTail ? "    " : "│   "), last, "Dim " + node.currentDim);
        }
    }
}

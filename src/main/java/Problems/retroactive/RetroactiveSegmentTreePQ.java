package Problems.retroactive;

import Problems.Operation;

public class RetroactiveSegmentTreePQ {
    SegmentNode root;
    private final int maxTime;
    private final Operation[] history;


    public RetroactiveSegmentTreePQ(int max) {
        maxTime = max;
        this.root = buildTree(0, maxTime);
        this.history = new Operation[max + 1];
    }

    public static class SegmentNode {
        int tmin;
        int tmax;
        int minInserted;
        int netInsertions; // INSERT - DELETE_MIN
        int unmatchedDeletes;   // DELETE_MINs que no tuvieron un INSERT en su propio rango
        SegmentNode left, right;

        public SegmentNode(int tmin, int tmax) {
            this.tmin = tmin;
            this.tmax = tmax;
            this.minInserted = Integer.MAX_VALUE;
            this.netInsertions = 0;
        }
    }

    // Methods for SEgment Tree
    private SegmentNode buildTree(int tMin, int tMax) {
        SegmentNode node = new SegmentNode(tMin, tMax);
        if (tMin == tMax) {
            // Es una hoja: nace vacía
            return node;
        }
        int mid = tMin + (tMax - tMin) / 2;
        node.left = buildTree(tMin, mid);
        node.right = buildTree(mid + 1, tMax);
        return node;
    }

    private void update(Operation op, SegmentNode nodo){
        if (nodo == null) return;

        // Caso hoja
        if (nodo.tmin == nodo.tmax) {
            // Validar si opType es null antes del switch
            if (op.opType == null) {
                nodo.netInsertions = 0;
                nodo.minInserted = Integer.MAX_VALUE;
                nodo.unmatchedDeletes = 0;
            } else {
                switch (op.opType) {
                    case ADD:
                        nodo.minInserted = op.value;
                        nodo.netInsertions = 1;
                        nodo.unmatchedDeletes = 0;
                        break;
                    case DELETE_MIN:
                        nodo.minInserted = Integer.MAX_VALUE;
                        nodo.netInsertions = -1;
                        nodo.unmatchedDeletes = 1;
                        break;
                }
            }
            return; // Detener recursión
        }

        int mitad = (nodo.tmin + nodo.tmax) / 2;
        if (op.timestamp <= mitad){
            update(op, nodo.left);
        }
        else{
            update(op, nodo.right);
        }
        // REcorremos la linea temporal
        combine(nodo);
    }

    // Tu método combine con unmatchedDeletes
    private void combine(SegmentNode nodo) {
        SegmentNode L = nodo.left;
        SegmentNode R = nodo.right;

        nodo.netInsertions = L.netInsertions + R.netInsertions;

        int elementosDeL_Devorados = 0;
        if (L.netInsertions > 0) {
            elementosDeL_Devorados = Math.min(L.netInsertions, R.unmatchedDeletes);
        }

        nodo.unmatchedDeletes = L.unmatchedDeletes + (R.unmatchedDeletes - elementosDeL_Devorados);

        if (L.netInsertions <= R.unmatchedDeletes) {
            nodo.minInserted = R.minInserted;
        } else {
            nodo.minInserted = Math.min(L.minInserted, R.minInserted);
        }
    }


    public void insertOp(Operation op){
        if (op == null || op.timestamp < 0 || op.timestamp > maxTime) return;
        update(op, root);
        history[op.timestamp] = op;
    }

    public void eliminarOp(int t){
        if (t < 0 || t > maxTime) return;

        // 1. Borramos del historial
        history[t] = null;

        // 2. Reseteamos en el Segment Tree (O(log N))
        update(new Operation(t, null, null), root);
    }

    public Integer queryMin() {
        return root.minInserted;
    }

    public boolean isValidHistory() {
        // Si las inserciones netas globales son menores a 0,
        // significa que hubo más DELETE_MINs que INSERTs en algún punto.
        return root.netInsertions >= 0;
    }

    public void imprimir(int cantidad_historial){
        System.out.println("=== Historial de Operaciones ===");
        boolean hayOperaciones = false;

        for (int t = 0; t <= cantidad_historial; t++) {
            if (history[t] != null && history[t].opType != null) {
                hayOperaciones = true;
                System.out.println("t = " + t + " -> " + history[t].opType + " " + history[t].value);
                System.out.println("===============================");
            }
        }

        if (!hayOperaciones) {
            System.out.println("El historial está vacío.");
        }
        System.out.println("===============================");
        System.out.println("Minimo: " + queryMin() + " Actual");
    }
}

package Problems.hollowHeap;

import java.util.*;

public class HollowHeap {

    HollowNode root;

    public HollowHeap() {
        this.root = null;
    }

    public HollowItem findMin() {
        if (this.root == null) {
            return null;
        }
        return root.item;
    }

    public void insert(HollowItem item, int key) {
        HollowNode nuevo = new HollowNode(key,item);
        item.nodo = nuevo;

        if (root == null) {
            root = nuevo;
        }
        else {
            root = link(nuevo, root);
        }
    }

    public HollowHeap meld(HollowHeap heap){
        if (heap == null || heap.root == null) {
            return this;
        }
        if (this.root == null) {
            this.root = heap.root;
            return this;
        }
        this.root = link(this.root, heap.root);
        return this;
    }
    public void decreaseKey(HollowItem item, int key) {
        // Buscamos el nodo del item para ddecrementar su key
        HollowNode u = item.nodo;

        if (u == this.root){
            u.key = key;
            return;
        }

        // creamos nuevo nodo v (lazy)
        HollowNode v = new HollowNode(key,item);
        v.rank = Math.max(0, u.rank-2);
        item.nodo = v;

        // U se convierte en hollow
        u.item = null;

        this.root = link(this.root, v);
        if (this.root != v){
            u.ep = v;
        }
    }

    public HollowItem deleteMin() {
        // Caso base: Heap vacío
        if (this.root == null) {
            return null;
        }

        HollowItem result = this.root.item;
        this.root.item = null; // Vaciar la raíz actual (se vuelve hollow)

        // ====================== FASE 1: Destrucción en cascada ======================
        Queue<HollowNode> L = new LinkedList<>();
        List<HollowNode> R = new ArrayList<>();
        L.add(this.root);

        while (!L.isEmpty()) {
            HollowNode remover = L.poll();
            HollowNode child = remover.child;
            remover.child = null; // Desconectar hijos del nodo destruido

            while (child != null) {
                HollowNode nextChild = child.next; // 1. Guardar referencia al siguiente hermano
                child.next = null;                // 2. Aislar el nodo actual

                if (child.ep != null) {
                    // Perdió un padre, pero aún conserva el otro. No se destruye.
                    child.ep = null;
                } else {
                    // Le quedaba 1 solo padre y acaba de perderlo
                    if (child.item == null) {
                        L.add(child); // Es hollow huérfano -> procesar sus hijos
                    } else {
                        R.add(child); // Es full huérfano -> raíz candidata
                    }
                }
                child = nextChild; // Avanzar al siguiente hermano guardado previamente
            }
        }

        // ====================== FASE 2: Consolidación por rangos ======================
        // Un arreglo de tamaño 64 soporta hasta 2^64 elementos
        int MAX_RANK = 64;
        HollowNode[] A = new HollowNode[MAX_RANK];

        for (HollowNode u : R) {
            while (A[u.rank] != null) {
                HollowNode v = A[u.rank];
                A[u.rank] = null;
                u = link(u, v);    // link devuelve el ganador entre u y v
                u.rank = u.rank + 1; // Incrementar el rango del ganador
            }
            A[u.rank] = u;
        }

        // ====================== FASE 3: Enlazar raíces restantes ======================
        this.root = null;
        for (int i = 0; i < MAX_RANK; i++) {
            if (A[i] != null) {
                if (this.root == null) {
                    this.root = A[i];
                } else {
                    this.root = link(this.root, A[i]);
                }
            }
        }

        return result;
    }

    // Auxiliar functions
    private HollowNode link(HollowNode u, HollowNode v) {
        if (u == null) {
            return v;
        }
        if (v == null) {
            return u;
        }

        // Queremos que v siempre sea la clave ganadora
        if (u.key > v.key) {
            HollowNode tmp = u;
            u = v;
            v = tmp;
        }

        v.next = u.child;
        u.child = v;

        return u;
    }

    public void printHeap() {
        System.out.println("================ ESTADO DEL HOLLOW HEAP ================");
        if (this.root == null) {
            System.out.println("(Heap vacío)");
        } else {
            printNode(this.root, "", true);
        }
        System.out.println("========================================================");
    }

    private void printNode(HollowNode node, String indent, boolean isLast) {
        if (node == null) return;

        // Prefijo gráfico para la consola
        System.out.print(indent);
        if (isLast) {
            System.out.print("└── ");
            indent += "    ";
        } else {
            System.out.print("├── ");
            indent += "│   ";
        }

        // Identificar si el nodo es FULL o HOLLOW
        String estado = (node.item != null) ? "FULL [Item: " + node.item + "]" : "HOLLOW";
        String extraParent = (node.ep != null) ? " (EP -> Key: " + node.ep.key + ")" : "";

        // Imprimir detalles del nodo actual
        System.out.println(estado + " (Key: " + node.key + ", Rank: " + node.rank + ")" + extraParent);

        // Contar cuántos hijos tiene este nodo para dar el formato gráfico adecuado
        int childCount = 0;
        HollowNode curr = node.child;
        while (curr != null) {
            childCount++;
            curr = curr.next;
        }

        // Recorrer la lista de hijos
        curr = node.child;
        int index = 0;
        while (curr != null) {
            index++;
            boolean lastChild = (index == childCount);
            printNode(curr, indent, lastChild);
            curr = curr.next; // Avanzar al siguiente hermano
        }
    }
}

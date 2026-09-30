package Problems.splayTree;

import java.util.ArrayList;
import java.util.List;

public class SplayTree {

    public NodoSplay root;

    public SplayTree(NodoSplay root) {
        this.root = root;
    }

    private void splay(NodoSplay node) {
        if (node == null) {
            return;
        }
        while (node.parent != null) {
            NodoSplay padre = node.parent;
            NodoSplay abuelo = node.parent.parent;

            if (abuelo == null) {
                if (node.parent.valor > node.valor) {
                    zig(node,0);
                }
                else{
                    zig(node,1);
                }
            } else {
                // Int: 0 -> caso izquierdo, 1 -> caso derecho
                if (node.valor < padre.valor && padre.valor < abuelo.valor) {
                    zigzig(node, 0);
                }
                if (node.valor > padre.valor && padre.valor > abuelo.valor) {
                    zigzig(node, 1);
                }
                if (node.valor > padre.valor && padre.valor < abuelo.valor) {
                    zigzag(node, 0);
                }
                if (node.valor < padre.valor && padre.valor > abuelo.valor) {
                    zigzag(node, 1);
                }
            }
        }
        this.root = node;
    }
    private void rotarDerecha(NodoSplay x) {
        NodoSplay y = x.left;
        if (y == null) return;

        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    private void rotarIzquierda(NodoSplay x) {
        NodoSplay y = x.right;
        if (y == null) return;

        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }

        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    private void zig(NodoSplay node, int i) {
        NodoSplay padre = node.parent;
        if (i == 0) {
            rotarDerecha(padre);
        } else {
            rotarIzquierda(padre);
        }
    }
    private void zigzig(NodoSplay node, int i) {
        NodoSplay padre = node.parent;
        NodoSplay abuelo = padre.parent;

        if (i == 0) { // Ambos izquierdos
            rotarDerecha(abuelo);
            rotarDerecha(padre);
        } else {
            rotarIzquierda(abuelo);
            rotarIzquierda(padre);
        }
    }
    private void zigzag(NodoSplay node, int i) {
        NodoSplay padre = node.parent;

        if (i == 0) {
            rotarIzquierda(padre);
            rotarDerecha(node.parent);
        } else {
            rotarDerecha(padre);
            rotarIzquierda(node.parent);
        }
    }

    public NodoSplay buscar(int x){
        if (this.root == null) {
            return null;
        }
        // Busqueda recursiva normal (traemos al mas cercano) :9
        NodoSplay actual = this.root;
        NodoSplay ultimoValido = actual;

        while (actual != null) {
            ultimoValido = actual;
            if (x == actual.valor) {
                break;
            } else if (x < actual.valor) {
                actual = actual.left;
            } else {
                actual = actual.right;
            }
        }

        splay(ultimoValido);
        return ultimoValido;
    }
    public List<NodoSplay> Split(int k) {
        NodoSplay aux = buscar(k);

        // Si el árbol está vacío
        if (aux == null) {
            List<NodoSplay> vacia = new ArrayList<>();
            vacia.add(null);
            vacia.add(null);
            return vacia;
        }

        List<NodoSplay> lista = new ArrayList<>();

        if (aux.valor <= k) {
            NodoSplay I = aux;
            NodoSplay D = aux.right;

            I.right = null;
            if (D != null) {
                D.parent = null;
            }

            lista.add(I);
            lista.add(D);
        } else { // aux.valor > k
            NodoSplay D = aux;
            NodoSplay I = aux.left;

            D.left = null;
            if (I != null) {
                I.parent = null;
            }

            lista.add(I);
            lista.add(D);
        }
        this.root = null;

        return lista;
    }

    public void insertar(int x) {
        if (this.root == null) {
            this.root = new NodoSplay(x);
            return;
        }

        List<NodoSplay> lista = Split(x);
        NodoSplay I = lista.get(0);
        NodoSplay D = lista.get(1);

        if (I != null && I.valor == x) {
            // Reconstruimos la conexión original sin duplicar el nodo
            I.right = D;
            if (D != null) {
                D.parent = I;
            }
            this.root = I; // x ya quedó en la raíz gracias al Splay previo
            return;
        }

        // Si no existía duplicado, insertamos el nuevo nodo U
        NodoSplay U = new NodoSplay(x);
        U.left = I;
        U.right = D;
        U.parent = null;

        if (I != null) I.parent = U;
        if (D != null) D.parent = U;

        this.root = U;
    }

    public void imprimir() {
        if (this.root == null) {
            System.out.println("\u001B[31m[Árbol Vacío]\u001B[0m");
            return;
        }
        System.out.println("\n--- Estructura Actual del Splay Tree ---");
        imprimirRec(this.root, "", true, true);
        System.out.println("----------------------------------------\n");
    }

    private void imprimirRec(NodoSplay nodo, String prefijo, boolean esUltimo, boolean esRaiz) {
        if (nodo != null) {
            // Códigos de colores ANSI
            String RESET = "\u001B[0m";
            String VERDE = "\u001B[32m";
            String AZUL = "\u001B[34m";
            String AMARILLO = "\u001B[33m";
            String CYAN = "\u001B[36m";

            System.out.print(prefijo);

            if (esRaiz) {
                System.out.print(AMARILLO + "└── (RAÍZ) " + nodo.valor + RESET);
            } else {
                System.out.print(esUltimo ? "└── " : "├── ");
                String colorRama = (nodo.parent != null && nodo.parent.left == nodo) ? VERDE + "[I] " : AZUL + "[D] ";
                System.out.print(colorRama + nodo.valor + RESET);
            }

            // Muestra información útil de depuración: el padre según el nodo
            if (nodo.parent != null) {
                System.out.println(CYAN + " (p: " + nodo.parent.valor + ")" + RESET);
            } else if (!esRaiz) {
                System.out.println(" \u001B[31m(p: NULL! ERROR)\u001B[0m");
            } else {
                System.out.println();
            }

            // Construcción del prefijo para los hijos
            String nuevoPrefijo = prefijo + (esUltimo ? "    " : "│   ");

            boolean tieneHijoIzq = nodo.left != null;
            boolean tieneHijoDer = nodo.right != null;

            if (tieneHijoIzq || tieneHijoDer) {
                if (nodo.left != null) {
                    imprimirRec(nodo.left, nuevoPrefijo, !tieneHijoDer, false);
                }
                if (nodo.right != null) {
                    imprimirRec(nodo.right, nuevoPrefijo, true, false);
                }
            }
        }
    }

}

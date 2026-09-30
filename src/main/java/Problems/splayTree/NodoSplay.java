package Problems.splayTree;

public class NodoSplay {
    int valor;
    NodoSplay left;
    NodoSplay right;
    NodoSplay parent;

    public NodoSplay(int valor) {
        this.valor = valor;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}

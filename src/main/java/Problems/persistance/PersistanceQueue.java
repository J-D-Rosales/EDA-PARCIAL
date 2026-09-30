package Problems.persistance;

import java.util.ArrayList;
import java.util.List;

public class PersistanceQueue {
    List<Integer> offset;
    List<Version> versiones;

    public PersistanceQueue() {
        offset = new ArrayList<>();
        versiones = new ArrayList<>();
    }

    // CLASES AUXILIARES CONSTRUCTORES
    // NODOS
    public static class Node {
        int val;
        int depth;
        Node[] up; // se guarda la inforamcion con 2^{n} para la tecnica de binary lifting

        public Node(int val, Node parent) {
            this.val = val;
            this.up  = new Node[20]; // para tener 200 000 op

            if (parent != null) {
                this.depth = parent.depth + 1;
                this.up[0] = parent;

                for (int i = 0; i < 20; i++) {
                    if (this.up[i-1] != null) {
                        this.up[i] =  this.up[i-1].up[i-1];
                    }
                }
            }
            else{
                this.depth = 0;
            }
        }
    }

    // VERSION
    public static class Version {
        Version tail;
        int offset;
        public Version(Version tail) {
            this.tail = tail;
            this.offset = 0;
        }
    }

    public void insertar(int version, int valor){
        if (version > versiones.size()) {
            throw new IllegalArgumentException("version out of range");
        }

    }


}

package Problems;

import java.util.ArrayList;
import java.util.List;

public class StackPersistence {

    // El versionamiento se hace con array dinámico
    List<StackPersistenceNode> roots;

    //CONSTRUCTOR
    public StackPersistence() {
        this.roots = new ArrayList<>();
        roots.add(null);
    }

    //NODE CLASS
    static public class StackPersistenceNode {
        int value;
        StackPersistenceNode next;
        long totalMass;

        public StackPersistenceNode(int value, StackPersistenceNode next) {
            this.value = value;
            this.next = next;
            if (next == null) {
                this.totalMass = value;
            } else {
                this.totalMass = value + next.totalMass;
            }
        }
    }

    public long getVersionTotalMass(int version){
        StackPersistenceNode actual = roots.get(version);
        if (actual == null) return 0;
        return actual.totalMass;
    }

    public int push(int value, int version) {
        // Validar versión base existente
        if (version < 0 || version >= roots.size()) {
            throw new IndexOutOfBoundsException("La versión base " + version + " no existe.");
        }

        else {
            StackPersistenceNode node = new StackPersistenceNode(value, roots.get(version));
            this.roots.add(node); // Version crece de manera continua

            return roots.size() - 1;
        }
    }

    public int pop(int version) {
        if (version < 0 || version >= roots.size()) {
            throw new IndexOutOfBoundsException("La versión base " + version + " no existe.");
        }

        if (roots.get(version) == null) {
            return -1; // ERROR NO HAY ELEMENTOS
        }

        StackPersistenceNode nuevoTop = this.roots.get(version).next;

        this.roots.add(nuevoTop);

        return roots.size() - 1;
    }

    public void imprimir(int version){
        StackPersistenceNode actual = roots.get(version);

        while (actual != null) {
            System.out.print(actual.value + " -> ");
            actual = actual.next;
        }
        System.out.println("null");
    }


}


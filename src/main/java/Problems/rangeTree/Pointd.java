package Problems.rangeTree;

import java.util.List;

public class Pointd {
    public int d; // dimensión
    public List<Integer> dimensiones;

    public Pointd(List<Integer> dimensiones) {
        this.dimensiones = dimensiones; // preferiblemente un arrayList
        this.d = dimensiones.size();
    }
    public int getDimension(int i) {
        return dimensiones.get(i);
    }

    @Override
    public String toString() {
        return dimensiones.toString(); // Imprimirá [2, 3] en lugar de la referencia
    }
}

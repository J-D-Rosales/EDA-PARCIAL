package Problems.rangeTree;

public class RangeNode {
    RangeNode left;
    RangeNode right;
    Pointd punto; // generalmente solo en las hojas
    RangeTree associatedTree; // Not a node, but a RAnge tree
    int currentDim; // We are orderin in a x, y or z or more dimesion?

    public RangeNode(int currentDim, Pointd punto) {
        this.currentDim = currentDim;
        this.punto = punto;
        this.associatedTree = null;
        this.left = null;
        this.right = null;
    }
}

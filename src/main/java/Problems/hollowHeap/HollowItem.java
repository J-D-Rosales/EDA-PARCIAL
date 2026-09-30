package Problems.hollowHeap;

public class HollowItem {

    public String value;
    public HollowNode nodo;

    public HollowItem(String value) {
        this.value = value;
        this.nodo = null;
    }

    @Override
    public String toString() {
        return value;
    }
}

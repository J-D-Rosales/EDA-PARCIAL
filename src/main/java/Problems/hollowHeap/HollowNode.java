package Problems.hollowHeap;

public class HollowNode {
    HollowItem item;
    int key;
    int rank;
    HollowNode child;
    HollowNode next;
    HollowNode ep;

    public HollowNode(int key, HollowItem item) {
        this.key = key;
        this.item = item;
        this.child = null;
        this.next = null;
        this.ep = null;
        this.rank = 0;
    }


}

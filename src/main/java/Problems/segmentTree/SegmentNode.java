package Problems.segmentTree;

import Problems.SegmentTreePersistance;

public class SegmentNode {

    public long val;
    public SegmentTreePersistance.SegmentTreeNode left;
    public SegmentTreePersistance.SegmentTreeNode right;
    long max;
    public SegmentNode(int val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}

package Problems;

public class Operation {
    public int timestamp;
    public OpType opType;
    public Integer value;

    public Operation(int timestamp, OpType opType, Integer value) {
        this.timestamp = timestamp;
        this.opType = opType;
        this.value = value;
    }
}

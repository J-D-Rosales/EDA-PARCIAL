package Problems;

import java.util.TreeMap;

public class PartialRetroactivityCounter {
    TreeMap<Integer,Operation> history;
    int currentValue; // Partial retroactivity just allows for current value

    public PartialRetroactivityCounter() {
        this.currentValue = 0;
        this.history = new TreeMap<>();
    }

    public void insertOp(Operation op){
        if (op == null) return;
        history.put(op.timestamp, op); // clave = t, valor = op
        // reevaluamos con tecnica de rollback
        reevaluate();
    }

    public void deleteOp(int timestamp) {
        if (history.containsKey(timestamp)) {
            history.remove(timestamp);
            reevaluate();
        }
    }
    public int query(int timestamp){
        int dummy = 0;
        // headMap(timestamp, true) devuelve solo los eventos desde t_min hasta timestamp
        for (Operation op : history.headMap(timestamp, true).values()) {
            if (op.opType.equals(OpType.ADD)) {
                dummy += op.value;
            } else if (op.opType.equals(OpType.SUB)) {
                dummy -= op.value;
            }
        }
        return dummy;
    }

    private void reevaluate(){
        currentValue = 0;
        for (Operation op : history.values()) {
            if (op.opType.equals(OpType.ADD)){
                currentValue += op.value;
            }
            else if (op.opType.equals(OpType.SUB)){
                currentValue -= op.value;
            }
        }
    }
}

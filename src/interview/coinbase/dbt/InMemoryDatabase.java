package interview.coinbase.dbt;

import java.util.*;

public class InMemoryDatabase {
    private Map<String, Object> store;
    private Stack<Map<String, Object>> transactionStack;
    private static final Object KEY_NOT_FOUND = new Object();

    public InMemoryDatabase(){
        this.store = new HashMap<>();
        this.transactionStack = new Stack<>();
    }

    public void begin(){
        transactionStack.push(new HashMap<>());
        System.out.println(String.format("Begin transaction [Depth: %s]", transactionStack.size()));
    }

    public Object get(String key){
        if(!store.containsKey(key))
            throw new IllegalArgumentException(String.format("Key=%s not found!", key));

        return this.store.get(key);
    }

    public void set(String key, Object value){
        if(inTransaction()){
            recordTxnChange(key);
            System.out.println(String.format("Set %s = %s in transaction", key, value));
        }

        this.store.put(key, value);
        System.out.println(String.format("Set %s = %s in database", key, value));
    }

    public void unset(String key){
        if(store.containsKey(key)){
            if(inTransaction()){
                recordTxnChange(key);
                System.out.println(String.format("Unset %s in transaction", key));
            }

            this.store.remove(key);
            System.out.println(String.format("Unset %s in database", key));
        }
    }

    public void commit(){
        if(!inTransaction()){
            throw new RuntimeException("No transaction to commit");
        }

        Map<String, Object> changes = transactionStack.pop();

        if(inTransaction()){
            Map<String, Object> parentTxn = transactionStack.peek();
            for(Map.Entry<String, Object> entry : changes.entrySet()){
                parentTxn.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    public void abort(){
        rollback();
    }
    public void rollback(){
        if(!inTransaction()){
            throw new RuntimeException("No transaction to abort/rollback");
        }

        Map<String, Object> changes = transactionStack.pop();

        for(Map.Entry<String, Object> entry : changes.entrySet()){
            String key = entry.getKey();
            Object oldValue = entry.getValue();

            if(key == KEY_NOT_FOUND){
                store.remove(KEY_NOT_FOUND);
            }else{
                store.put(key, oldValue);
            }
        }
    }

    public int getTransactionDepth(){
        return transactionStack.size();
    }

    private boolean inTransaction(){
        return !transactionStack.isEmpty();
    }

    private void recordTxnChange(String key){
        Map<String, Object> currentTxn = transactionStack.peek();

        if(!currentTxn.containsKey(key)){
            if(store.containsKey(key)){
                currentTxn.put(key, store.get(key));
            }else{
                currentTxn.put(key, KEY_NOT_FOUND);
            }
        }
    }

    public static void main(String args[]){
        System.out.println("=== Basic Operations ===");
        InMemoryDatabase db = new InMemoryDatabase();
        db.set("x", 10);
        System.out.println("After set('x', 10): get('x') = " + db.get("x"));
        db.set("x", 20);
        System.out.println("After set('x', 20): get('x') = " + db.get("x"));
        db.unset("x");
        try {
            db.get("x");
        } catch (IllegalArgumentException e) {
            System.out.println("After unset('x'): " + e.getMessage());
        }
        
        System.out.println("\n=== Transaction Abort ===");
        db = new InMemoryDatabase();
        db.set("x", 10);
        System.out.println("Initial: get('x') = " + db.get("x"));
        db.begin();
        db.set("x", 20);
        System.out.println("During transaction: get('x') = " + db.get("x"));
        db.abort();
        System.out.println("After abort: get('x') = " + db.get("x"));
        
        System.out.println("\n=== Transaction Commit ===");
        db = new InMemoryDatabase();
        db.set("y", 100);
        System.out.println("Initial: get('y') = " + db.get("y"));
        db.begin();
        db.set("y", 200);
        System.out.println("During transaction: get('y') = " + db.get("y"));
        db.commit();
        System.out.println("After commit: get('y') = " + db.get("y"));
        
        System.out.println("\n=== Nested Transactions ===");
        db = new InMemoryDatabase();
        db.set("z", 1);
        System.out.println("Initial: get('z') = " + db.get("z"));
        db.begin();
        db.set("z", 2);
        System.out.println("Outer transaction: get('z') = " + db.get("z"));
        db.begin();
        db.set("z", 3);
        System.out.println("Inner transaction: get('z') = " + db.get("z"));
        db.commit();  // Commit inner
        System.out.println("After inner commit: get('z') = " + db.get("z"));
        db.abort();  // Abort outer
        System.out.println("After outer abort: get('z') = " + db.get("z"));
    }
}

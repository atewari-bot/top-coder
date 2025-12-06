package dsa;

import java.util.*;

public class PriorityQueue<T> {
    /**
     * Min-Heap/Max-Heap Implementation
     */
    private Object[] heap;
    private int size;
    private int capacity;
    private static final int DEFAULT_CAPACITY = 8;
    private Comparator<? super T> comparator;


    public PriorityQueue() {
        this(DEFAULT_CAPACITY, null);
    }

    public PriorityQueue(int capacity) {
        this(capacity, null);
    }
    
    public PriorityQueue(Comparator<? super T> comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public PriorityQueue(int capacity, Comparator<? super T> comparator) {
        this.capacity = capacity;
        this.heap = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    public boolean offer(T val) {
        if(this.size == this.capacity){
            resize();
        }

        if(isEmpty()){
            this.heap[0] = val;
            this.size++;
            return true;
        }else {
            percolateUp(val, this.size);
            this.size++;
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public T poll(){
        if(isEmpty()){
            return null;
        }

        T val = (T) this.heap[0];
        T lastVal = (T) this.heap[this.size - 1];
        this.heap[this.size - 1] = null;
        this.size--;

        if(this.size > 0){
            percolateDown(lastVal, 0);
        }
        return val;
    }

    @SuppressWarnings("unchecked")
    public T peek(){
        if(isEmpty()){
            return null;
        }

        return (T) this.heap[0];
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b){
        if(this.comparator != null){
            return this.comparator.compare(a, b);
        }else{
            return ((Comparable<T>) a).compareTo(b);
        }
    }

    @SuppressWarnings("unchecked")
    private void percolateUp(T val, int index){
        while(index > 0){
            int parentIndex = (index - 1) / 2;
            T parentVal = this.heap[parentIndex] != null ? (T) this.heap[parentIndex] : null;

            // If parent is less than or equal to current value, we are done
            if(compare(val, parentVal) >= 0){
                break;
            }

            this.heap[index] = parentVal;
            index = parentIndex;
        }

        this.heap[index] = val;
    }

    @SuppressWarnings("unchecked")
    private void percolateDown(T val, int index){
        int half = this.size >>> 1;

        while(index < half){
            int childIndex = (index << 1) + 1;
            int rightChildIndex = childIndex + 1;
            T parentVal = (T) this.heap[index];
            T childVal = (T) this.heap[childIndex];
            T rightChildVal = rightChildIndex < this.size ? (T) this.heap[rightChildIndex] : null;

            // If right child is smaller than left child, use the right child instead
            if(rightChildVal != null && compare(rightChildVal, childVal) < 0){
                childVal = (T) this.heap[childIndex = rightChildIndex];
            }

            // If parent is less than or equal to child, we are done
            if(compare(parentVal, childVal) <= 0){
                break;
            }

            this.heap[index] = childVal;
            index = childIndex;
        }

        this.heap[index] = val;
    }

    private void resize(){
        this.capacity *= 2;
        Object[] newHeap = new Object[this.capacity];
        System.arraycopy(this.heap, 0, newHeap, 0, this.size);
        this.heap = newHeap;
    }

     public int size(){
        return this.size;
    }

    public boolean isEmpty(){
        return this.size == 0;
    }

    public void clear(){
        this.size = 0;
        this.heap = new Object[this.capacity];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < this.size; i++){
            sb.append(this.heap[i]);
            if(this.size -i > 1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(5);
        pq.offer(3);
        pq.offer(8);
        pq.offer(1);
        pq.poll();
        pq.offer(15);
        pq.offer(30);
        pq.offer(0);
        pq.offer(-1);

        System.out.println(pq.toString());

        System.out.println("Max Heap:");
        PriorityQueue<Integer> maxPQ = new PriorityQueue<>(new Comparator<>(){
            @Override
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });

        maxPQ.offer(5);
        maxPQ.offer(3);
        maxPQ.offer(8);
        maxPQ.offer(1);
        maxPQ.poll();
        maxPQ.offer(15);
        maxPQ.offer(30);
        maxPQ.offer(0);
        maxPQ.offer(-1);

        System.out.println(maxPQ.toString());
    }
}
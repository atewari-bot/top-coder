package dsa;

import java.util.*;

class ArrayDeque<T> {
    private T array[];
    private int capacity;
    private int size;
    private int front, back;
    
    public ArrayDeque(){
        this(8);
    }   

    @SuppressWarnings("unchecked")
    public ArrayDeque(int capacity){
        this.capacity = capacity;
        this.array = (T[]) new Object[capacity];
        boolean isEvenCapacity = capacity % 2 == 0;
        int mid = capacity / 2;
        this.front = isEvenCapacity ? mid - 1 : mid;
        this.back = mid;
        this.size = 0;
    }
    
    public boolean insertFront(T val){
        if(this.front >= 0){
            this.array[this.front--] = val;
        }else {
            resize();
            this.insertFront(val);
        }
        this.size++;
        return true;
    }

    public boolean insertBack(T val){
        if(this.back < this.capacity){
            this.array[this.back++] = val;
        }else {
            resize();
            this.insertBack(val);
        }
        this.size++;
        return true;
    }

    public T removeFront(){
        this.front++;
        T val = this.array[this.front];
        this.array[this.front] = null;
        this.size--;
        return val;
    }
    
    public T removeBack(){
        this.back--;
        T val = this.array[this.back];
        this.array[this.back] = null;
        this.size--;
        return val;
    }
    
    public T get(int index){
        if(this.size == 0)
            return null;
        return this.array[this.front + index + 1];
    }
    
    @SuppressWarnings("unchecked")
    private void resize(){
        T arrayCopy[] = this.array.clone();
        this.capacity *= 2;
        this.array = (T[]) new Object[this.capacity];
        int startPrev = 0;
        int endPrev = this.size - 1;
        int midCurr = this.capacity / 2;
        boolean isEvenSize = this.capacity % 2 == 0;
        int startCurr = midCurr - this.size / 2;
        int endCurr = isEvenSize ? midCurr + (this.size / 2) - 1 : midCurr + this.size / 2;
        this.front = startCurr - 1;
        this.back = endCurr + 1;
        
        while(startPrev <= endPrev){
            this.array[startCurr++] = arrayCopy[startPrev++];
            this.array[endCurr--] = arrayCopy[endPrev--];
        }
    }

    private void printArray(){
        for(int i = 0; i < this.capacity; i++){
            System.out.print(this.array[i] + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        ArrayDeque<Integer> sol = new ArrayDeque<>(8);
        sol.insertFront(1);
        sol.insertBack(2);
        sol.insertFront(3);
        sol.insertBack(4);
        sol.insertFront(5);
        sol.insertBack(6);
        sol.insertFront(7);
        sol.insertBack(8);
        
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            result.add(sol.get(i));
        }
        System.out.println("Current Deque elements: " + result);
        
        sol.printArray();
        System.out.println("Inserting more elements to trigger resize:");
        sol.insertBack(9);
        sol.insertFront(10);
        sol.printArray();

        System.out.println("Removing elements:");
        sol.removeBack();
        sol.removeFront();
        sol.printArray();

        sol.insertBack(11);
        sol.insertFront(12);
        sol.printArray();
    }
}
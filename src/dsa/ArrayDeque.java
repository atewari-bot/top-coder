package dsa;

import java.util.*;

class ArrayDeque {
    private int array[];
    private int capacity;
    private int size;
    private int left, right;
    
    public ArrayDeque(int capacity){
        this.capacity = capacity;
        this.array = new int[capacity];
        boolean isEvenCapacity = capacity % 2 == 0;
        int mid = capacity / 2;
        this.left = isEvenCapacity ? mid - 1 : mid;
        this.right = mid;
        this.size = 0;
    }
    
    public boolean insertFront(int val){
        if(this.left >= 0){
            this.array[this.left--] = val;
        }else {
            resize();
            this.insertFront(val);
        }
        this.size++;
        return true;
    }

    public boolean insertBack(int val){
        if(this.right < this.capacity){
            this.array[this.right++] = val;
        }else {
            resize();
            this.insertBack(val);
        }
        this.size++;
        return true;
    }

    public int removeFront(){
        this.left++;
        int val = this.array[this.left];
        this.array[this.left] = 0;
        this.size--;
        return val;
    }
    
    public int removeBack(){
        this.right--;
        int val = this.array[this.right];
        this.array[this.right] = 0;
        this.size--;
        return val;
    }
    
    public int get(int index){
        if(this.size == 0)
            return -1;
        return this.array[this.left + index + 1];
    }
    
    private void resize(){
        int arrayCopy[] = this.array.clone();
        this.capacity *= 2;
        this.array = new int[this.capacity];
        int startPrev = 0;
        int endPrev = this.size - 1;
        int midCurr = this.capacity / 2;
        boolean isEvenSize = this.capacity % 2 == 0;
        int startCurr = midCurr - this.size / 2;
        int endCurr = isEvenSize ? midCurr + (this.size / 2) - 1 : midCurr + this.size / 2;
        this.left = startCurr - 1;
        this.right = endCurr + 1;
        
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
        ArrayDeque sol = new ArrayDeque(8);
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
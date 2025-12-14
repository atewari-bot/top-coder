package dsa;

import com.google.common.hash.Hashing;
import java.util.*;
import com.google.common.hash.HashCode;

public class HashMap<K, V> {
    static class Node<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        Node<K, V> next;
        Node(K key, V value){
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
    
    Node<K, V>[] table;
    private int capacity;
    private int size;
    private static final double LOAD_FACTOR = 0.75;
}

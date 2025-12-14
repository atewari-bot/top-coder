package leetcode;

import java.util.*;

public class Leetcode2799 {
    Map<Integer, List<Integer>> graph;
    Map<Integer, Long> pathNodesParity;

    public long countPalindromePaths(List<Integer> parent, String s) {
        graph = new HashMap<>();
        pathNodesParity = new HashMap<>();

        for(int i = 0; i < parent.size(); i++){
            graph.putIfAbsent(parent.get(i), new ArrayList<>());
            graph.get(parent.get(i)).add(i);
        }

        dfs(0, 0, s);
        long ans = 0;
        System.out.println(pathNodesParity);
        for(Map.Entry<Integer, Long> entry : pathNodesParity.entrySet()){
            int mask = entry.getKey();
            long currFreq = entry.getValue();

            ans += currFreq * (currFreq - 1);

            for(int i = 0; i < 26; i++){
                ans += currFreq * pathNodesParity.getOrDefault(mask ^ (1 << i), 0L);
            }
        }

        return ans / 2;
    }

    public void dfs(int node, int mask, String s){
        pathNodesParity.put(mask, pathNodesParity.getOrDefault(mask, 0L) + 1);

        for(int nextNode : graph.getOrDefault(node, new ArrayList<>())){
            int newMask = mask ^ (1 << (s.charAt(nextNode) - 'a'));
            dfs(nextNode, newMask, s);
        }
    }

    public static void main(String[] args) {
        Leetcode2799 obj = new Leetcode2799();
        List<Integer> parent = Arrays.asList(-1,0,0,1,1,2);
        String s = "abacbe";
        long result = obj.countPalindromePaths(parent, s);
        System.out.println("Number of palindrome paths: " + result);

        // Additional test case requested: s = "abacbedzx"
        // Provide a parent list of length 9 (indices 0..8) that forms a valid tree.
        List<Integer> parent2 = Arrays.asList(-1,0,0,1,1,2,2,3,4);
        String s2 = "abacbedzx";
        long result2 = obj.countPalindromePaths(parent2, s2);
        System.out.println("Number of palindrome paths (s=\"" + s2 + "\"): " + result2);
    }
}
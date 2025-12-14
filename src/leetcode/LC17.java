package leetcode;

import java.io.*;
import java.util.*;

class LC17 {
    // [2, 2, 8] -> ["act", "cat", "bat", "..."]
    public static List<String> getWords(List<Integer> phone) {
        // Placeholder for the actual implementation
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        backtrack(phone, 0, sb, res);

        return res;
    }

    public static void backtrack(List<Integer> phone, int digitIndex, StringBuilder sb, List<String> res){
      if(phone.size() == digitIndex){
        // System.out.println(sb.toString());
        if(isWord(sb.toString())){
          res.add(sb.toString());
        }
        return;
      }

      List<Character> letters = getLetters(phone.get(digitIndex));
      for(int j = 0; j < letters.size(); j++){
        sb.append(letters.get(j));
        backtrack(phone, digitIndex + 1, sb, res);
        sb.deleteCharAt(sb.length() - 1);
      }

    }
    public static void main(String[] args) {
        List<Integer> input = Arrays.asList(2, 2, 8);
        // List<Integer> input = Arrays.asList(2, 3);

        System.out.print("Words in ");
        input.forEach(System.out::print);
        System.out.println();

        for (String word : getWords(input)) {
            System.out.println(word);
        }
    }

    private static Set<String> words = null;

    public static boolean isWord(String word) {
      if (words == null) {
        words = new HashSet<String>();
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/leetcode/data/dictionary.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      }
        return words.contains(word);
    }

    public static List<Character> getLetters(int digit) {
        Map<Integer, List<Character>> lettersMap = new HashMap<>();
        lettersMap.put(2, Arrays.asList('a', 'b', 'c'));
        lettersMap.put(3, Arrays.asList('d', 'e', 'f'));
        lettersMap.put(4, Arrays.asList('g', 'h', 'i'));
        lettersMap.put(5, Arrays.asList('j', 'k', 'l'));
        lettersMap.put(6, Arrays.asList('m', 'n', 'o'));
        lettersMap.put(7, Arrays.asList('p', 'q', 'r', 's'));
        lettersMap.put(8, Arrays.asList('t', 'u', 'v'));
        lettersMap.put(9, Arrays.asList('w', 'x', 'y', 'z'));

        return lettersMap.getOrDefault(digit, Collections.emptyList());
    }
}
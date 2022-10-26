package ascii_art;

import java.util.HashSet;

public class Algorithms {
    private static final String[] morseCodeABC = new String[] {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-",
            ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--",
            "--.."};

    /**
     * an implementation of floyds algorithem to find a repeating number in a list of integers ranging from 1-n
     * @param numList list of n+1 integers in range 1-n
     * @return the number that repeats
     */
    public static int findDuplicate(int[] numList){
        int  fast = 0, slow = 0 ,newSlow = 0;
        do {
            slow = numList[slow];
            fast = numList[numList[fast]];
        } while (slow != fast);

        while (true){
            slow = numList[slow];
            newSlow = numList[newSlow];
            if(slow == newSlow){
                return slow;
            }
        }

    }


    /**
     * returnds number of unique Representations in Morse code for the given words
     * @param words list of words
     * @return number of unique Morse code Representations
     */
    public static int uniqueMorseRepresentations(String[] words) {
//        String[] morseCodeABC = new String[]{".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-",
//                ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--",
//                "--.."};
        String morseVal;
        HashSet<String> wordsUsed = new HashSet<>();
        for (int i = 0; i < words.length; i++) {
            StringBuilder cur = new StringBuilder();
            for (char letter : words[i].toLowerCase().toCharArray()) {
                morseVal = morseCodeABC[letter - 97];
                cur.append(morseVal);
            }
            wordsUsed.add(cur.toString());
        }
        return wordsUsed.size();
    }
}

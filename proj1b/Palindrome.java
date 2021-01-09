public class Palindrome {

    /**
     * Returns a deque where the characters appear in the same order as in the String.
     * @param word The word needs to be transferred into a deque.
     * @return The transferred deque.
     */
    public Deque<Character> wordToDeque(String word) {
        if (word.length() == 0) {
            return null;
        }
        Deque<Character> deque = new LinkedListDeque<>();
        int length = word.length();
        for (int i = 0; i < length; i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    /**
     * The isPalindrome method should return true if the given word is a palindrome,
     * and false otherwise. A palindrome is defined as a word that is the same whether
     * it is read forwards or backwards.
     * @param word The word need to be judge.
     * @return If the word is a palindrome.
     */
    public boolean isPalindrome(String word) {
        if (word.length() == 0 | word.length() == 1) {
            return true;
        }
        String wordReversed = getWordsBackwards(wordToDeque(word));
        if (wordReversed.equals(word)) {
            return true;
        }
        return false;
    }

    /**
     * The method will return true if the word is a palindrome according to the character
     * comparison test provided by the CharacterComparator passed in as argument cc.
     * @param word The word need to be judge.
     * @param cc A character comparator.
     * @return If the word is a off-by-1 palindrome.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() == 0 | word.length() == 1) {
            return true;
        }
        if (cc == null) {
            return false;
        }
        int length = word.length();
        String wordReversed = getWordsBackwards(wordToDeque(word));
        if (length % 2 == 0) {
            for (int i = 0; i < length; i++) {
                if (!cc.equalChars(word.charAt(i), wordReversed.charAt(i))) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (i == (length - 1) / 2) {
                    continue;
                }
                if (!cc.equalChars(word.charAt(i), wordReversed.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * HELPER METHOD
     * Returns a string of the word in the deque reversed. Using recursion.
     * @param deque The deque saved the word need to be reversed.
     * @return The reversed word.
     */
    private String getWordsBackwards(Deque<Character> deque) {
        if (deque.isEmpty()) {
            return "";
        }
        char last = deque.removeLast();
        return last + getWordsBackwards(deque);
    }
}

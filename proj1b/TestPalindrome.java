import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }
    // Uncomment this class once you've created your Palindrome class.

    @Test
    public void testIsPalindrome() {
        String test1 = "racecar";
        String test2 = "noon";
        String test3 = "horse";
        String test4 = "rancor";
        boolean actual1 = palindrome.isPalindrome(test1);
        boolean actual2 = palindrome.isPalindrome(test2);
        boolean actual3 = palindrome.isPalindrome(test3);
        boolean actual4 = palindrome.isPalindrome(test4);
        assertTrue(actual1);
        assertTrue(actual2);
        assertFalse(actual3);
        assertFalse(actual4);
    }

    @Test
    public void testIsPalindromeNew() {
        String test1 = "flake";
        String test2 = "noon";
        assertTrue(palindrome.isPalindrome(test1, new OffByOne()));
        assertFalse(palindrome.isPalindrome(test2, new OffByOne()));
    }
}

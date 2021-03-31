import java.util.LinkedList;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxLength = 0;
        for (String s : asciis) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        String[] sorted = sortHelperLSD(asciis, maxLength - 1);
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        if (index < 0) {
            return asciis;
        }
        int[] count = new int[256];
        int[] startPosition = new int[256];
        int start = 0;
        for (String s : asciis) {
            count[charAt(s, index)]++;
        }
        for (int i = 0; i < 256; i++) {
            startPosition[i] = start;
            start += count[i];
        }
        String[] result = new String[asciis.length];
        for (String s : asciis) {
            int charCode = charAt(s, index);
            result[startPosition[charCode]] = s;
            startPosition[charCode]++;
        }
        return sortHelperLSD(result, index - 1);
    }

    private static int charAt(String s, int index) {
        if (s.length() < index + 1) {
            return 0;
        }
        return s.charAt(index);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] strings = new String[5];
        strings[0] = "abced";
        strings[1] = "abcd";
        strings[2] = "abc";
        strings[3] = "ab";
        strings[4] = "a";
        String[] result = RadixSort.sort(strings);
        for (String s : result) {
            System.out.println(s);
        }
    }
}

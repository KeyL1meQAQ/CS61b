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
        LinkedList<String>[] buckets = new LinkedList[256];
        for (String s : asciis) {
            if (s.length() < index + 1) {
                if (buckets[0] == null) {
                    buckets[0] = new LinkedList<>();
                }
                buckets[0].add(s);
                continue;
            }
            int numOfBucket = (int) s.charAt(index);
            if (buckets[numOfBucket] == null) {
                buckets[numOfBucket] = new LinkedList<>();
            }
            buckets[numOfBucket].add(s);
        }
        asciis = catenateBuckets(buckets, asciis.length);
        return sortHelperLSD(asciis, index - 1);
    }

    private static String[] catenateBuckets(LinkedList<String>[] buckets, int size) {
        String[] catenated = new String[size];
        int index = 0;
        for (LinkedList<String> bucket : buckets) {
            if (bucket != null && !bucket.isEmpty()) {
                while (!bucket.isEmpty()) {
                    catenated[index] = bucket.removeFirst();
                    index++;
                }
            }
        }
        return catenated;
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
}

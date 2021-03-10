package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        int N = oomages.size();
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = 0;
        }
        for (Oomage oomage : oomages) {
            int bucket = (oomage.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucket]++;
        }
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] < (double) N / 50 || buckets[i] > (double) N / 2.5) {
                return false;
            }
        }
        return true;
    }
}

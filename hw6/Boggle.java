import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    static Tries tries = new Tries();
    static List<char[]> boggle = new ArrayList<>();
    static boolean[][] marked;
    static LinkedList<String> valid;
    static Set<String> validSet = new HashSet<>();

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k < 1) {
            throw new IllegalArgumentException();
        }
        readDictionary();
        readBoggle(boardFilePath);
        valid = new LinkedList<>();
        marked = new boolean[boggle.size()][boggle.get(0).length];
        for (int i = 0; i < boggle.size(); i++) {
            for (int j = 0; j < boggle.get(0).length; j++) {
                String s = "" + boggle.get(i)[j];
                Tries.Node node = tries.getRoot().getNode(boggle.get(i)[j]);
                if (node == null) {
                    continue;
                }
                search(i, j, marked, node, s);
            }
        }
        LinkedList<String> result = mergeSort(valid);
        LinkedList<String> firstK = new LinkedList<>();
        for (int i = 0; i < k; i++) {
            firstK.addLast(result.removeFirst());
        }
        return firstK;
    }

    private static void readDictionary() {
        In in = new In(dictPath);
        while (in.hasNextLine()) {
            String s = in.readLine();
            tries.addString(s);
        }
    }

    private static void readBoggle(String boardFilePath) {
        In in = new In(boardFilePath);
        int length = 0;
        if (in.hasNextLine()) {
            char[] line = in.readLine().toCharArray();
            length = line.length;
            boggle.add(line);
        }
        while (in.hasNextLine()) {
            char[] line = in.readLine().toCharArray();
            if (line.length != length) {
                throw new IllegalArgumentException();
            }
            boggle.add(line);
        }
    }

    private static List<Integer> getSurround(int i, int j) {
        List<Integer> surround = new ArrayList<>();
        if (isExist(i - 1, j - 1)) {
            surround.add(to1D(i - 1, j - 1));
        }
        if (isExist(i - 1, j)) {
            surround.add(to1D(i - 1, j));
        }
        if (isExist(i - 1, j + 1)) {
            surround.add(to1D(i - 1, j + 1));
        }
        if (isExist(i, j - 1)) {
            surround.add(to1D(i, j - 1));
        }
        if (isExist(i, j + 1)) {
            surround.add(to1D(i, j + 1));
        }
        if (isExist(i + 1, j - 1)) {
            surround.add(to1D(i + 1, j - 1));
        }
        if (isExist(i + 1, j)) {
            surround.add(to1D(i + 1, j));
        }
        if (isExist(i + 1, j + 1)) {
            surround.add(to1D(i + 1, j + 1));
        }
        return surround;
    }

    private static boolean isExist(int i, int j) {
        return (i >= 0 && i < boggle.get(0).length && j >= 0 && j < boggle.size());
    }

    private static int to1D(int i, int j) {
        int width = boggle.get(0).length;
        return i * width + j;
    }

    private static int toRow(int i) {
        int width = boggle.get(0).length;
        return i / width;
    }

    private static int toCol(int i) {
        int width = boggle.get(0).length;
        return i % width;
    }

    private static void search(int row, int col, boolean[][] mark, Tries.Node node,
                                       String s) {
        if (s.length() > 2 && node.isEnd() && !validSet.contains(s)) {
            valid.add(s);
            validSet.add(s);
        }
        List<Integer> surround = getSurround(row, col);
        mark[row][col] = true;
        for (int next : surround) {
            int nextRow = toRow(next);
            int nextCol = toCol(next);
            char nextChar = boggle.get(nextRow)[nextCol];
            if (node.getNode(nextChar) != null && !marked[nextRow][nextCol]) {
                search(nextRow, nextCol, marked, node.getNode(nextChar), s + nextChar);
            }
        }
        mark[row][col] = false;
    }

    private static LinkedList<String> mergeSort(LinkedList<String> list) {
        LinkedList<String> leftItems = new LinkedList<>();
        LinkedList<String> rightItems = new LinkedList<>();
        int midIndex = list.size() / 2;
        if (list.size() <= 1) {
            return list;
        }
        for (String s : list) {
            if (midIndex > 0) {
                leftItems.add(s);
            } else {
                rightItems.add(s);
            }
            midIndex--;
        }
        return mergeSortedLists(mergeSort(leftItems), mergeSort(rightItems));
    }

    private static LinkedList<String> mergeSortedLists(LinkedList<String> first,
                                                       LinkedList<String> second) {
        LinkedList<String> sorted = new LinkedList<>();
        while (!first.isEmpty() || !second.isEmpty()) {
            sorted.add(getMax(first, second));
        }
        return sorted;
    }

    private static String getMax(LinkedList<String> first, LinkedList<String> second) {
        if (first.isEmpty()) {
            return second.removeFirst();
        } else if (second.isEmpty()) {
            return first.removeFirst();
        } else {
            String s1 = first.getFirst();
            String s2 = second.getFirst();
            if (s1.length() > s2.length()) {
                return first.removeFirst();
            } else if (s1.length() < s2.length()) {
                return second.removeFirst();
            } else {
                if (s1.compareTo(s2) > 0) {
                    return second.removeFirst();
                } else {
                    return first.removeFirst();
                }
            }
        }
    }

    public static void main(String[] args) {
        List<String> result = solve(7, "exampleBoard.txt");
        System.out.println(result);
    }
}

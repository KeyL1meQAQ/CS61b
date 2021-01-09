public class OffByN implements CharacterComparator {

    /**
     * The value to off.
     */
    private int N;

    /**
     * The constructor.
     * @param N The value to off
     */
    public OffByN(int N) {
        this.N = N;
    }

    /**
     * Returns true for characters that are off by N.
     * @param x The first character to be compared.
     * @param y The second character to be compared.
     * @return If the characters are off by N.
     */
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == N) {
            return true;
        }
        return false;
    }
}

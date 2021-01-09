public class OffByOne implements CharacterComparator {

    /**
     * equalChars returns true for characters that are different by exactly one.
     * @param x The first character to be compared.
     * @param y The second character to be compared.
     * @return Return if the characters are different by exactly one.
     */
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == 1) {
            return true;
        }
        return false;
    }
}

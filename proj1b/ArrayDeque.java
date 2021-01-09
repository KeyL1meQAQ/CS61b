public class ArrayDeque<T> implements Deque<T> {

    /**
     * The array to save items.
     */
    private T[] items;
    /**
     * The size of the list.
     */
    private int size;
    /**
     * Indicate the next position of first item when use addFirst.
     */
    private int nextFirst;
    /**
     * Indicate the next position of last item when use addLast.
     */
    private int nextLast;

    /**
     * Constructor, create an empty array deque. The starting size of the array is 8.
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
    }

    /**
     * Adds an item to the front of the deque
     * @param item The item needs to be added to the front of the deque.
     */
    @Override
    public void addFirst(T item) {
        if (item == null) {
            return;
        }
        if (size == 0) {
            addFirstItem(item);
            return;
        }
        if (size == items.length - 1) {
            resizeExp();
            items[nextFirst] = item;
            nextFirst--;
            size++;
        } else {
            items[nextFirst] = item;
            nextFirst--;
            size++;
            if (nextFirst < 0) {
                nextFirst = items.length - 1;
            }
        }
    }

    /**
     * Adds an item to the back of the deque.
     * @param item The item needs to be added to the back of the deque.
     */
    @Override
    public void addLast(T item) {
        if (item == null) {
            return;
        }
        if (size == 0) {
            addFirstItem(item);
            return;
        }
        if (size == items.length - 1) {
            resizeExp();
            items[nextLast] = item;
            nextLast++;
            size++;
        } else {
            items[nextLast] = item;
            nextLast++;
            size++;
            if (nextLast > items.length - 1) {
                nextLast = 0;
            }
        }
    }

    /**
     * Returns true if deque is empty, false otherwise.
     * @return The status of deque, empty or not.
     */
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of items inthe deque.
     * @return The size of the deque.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        if (nextFirst > nextLast) {
            if (nextFirst == items.length - 1) {
                for (int i = 0; i < size; i++) {
                    System.out.print(items[i] + " ");
                }
            } else {
                for (int i = 0; i < items.length - nextFirst - 1; i++) {
                    System.out.print(items[nextFirst + i + 1] + " ");
                }
                for (int i = 0; i < nextLast; i++) {
                    System.out.print(items[i] + " ");
                }
            }
        } else if (nextFirst < nextLast) {
            for (int i = 0; i < size; i++) {
                System.out.print(items[nextFirst + 1 + i] + " ");
            }
        }
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     * @return The item returned at the front of the deque.
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item;
        if (nextFirst + 1 == items.length) {
            item = items[0];
            items[0] = null;
            size--;
            nextFirst = 0;
        } else {
            item = items[nextFirst + 1];
            items[nextFirst + 1] = null;
            size--;
            nextFirst++;
        }
        if ((double) size / (double) items.length < 0.25 && items.length > 8) {
            resizeShk();
        }
        return item;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     * @return The item returned at the back of the deque.
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T item;
        if (nextLast - 1 < 0) {
            item = items[items.length - 1];
            items[items.length - 1] = null;
            size--;
            nextLast = items.length - 1;
        } else {
            item = items[nextLast - 1];
            items[nextLast - 1] = null;
            size--;
            nextLast--;
        }
        if ((double) size / (double) items.length < 0.25 && items.length > 8) {
            resizeShk();
        }
        return item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index The position of the item.
     * @return The item gotten.
     */
    @Override
    public T get(int index) {
        if (index >= size | index < 0) {
            return null;
        }
        if (nextFirst + index + 1 > items.length - 1) {
            return items[index - (items.length - nextFirst - 1)];
        } else {
            return items[nextFirst + index + 1];
        }
    }

    /**
     * HELPER METHOD
     * Resizes to expand the array.
     */
    private void resizeExp() {
        int length = items.length;
        T[] resizedArray = (T[]) new Object[length * 2];
        int start = length / 2;
        if (nextFirst == 0) {
            System.arraycopy(items, 1, resizedArray, start, size);
            nextFirst = start - 1;
            nextLast = start + size;
        } else if (nextFirst == length - 1) {
            System.arraycopy(items, 0, resizedArray, start, size);
            nextFirst = start - 1;
            nextLast = start + size;
        } else if (nextLast == nextFirst) {
            System.arraycopy(items, nextFirst + 1, resizedArray, start, length - nextFirst - 1);
            System.arraycopy(items, 0, resizedArray, start + (length - nextFirst - 1), nextFirst);
            nextFirst = start - 1;
            nextLast = start + size;
        }
        items = resizedArray;
    }

    /**
     * HELPER METHOD
     * Resizes to shrink the array.
     */
    private void resizeShk() {
        int length = items.length;
        T[] resizedArray = (T[]) new Object[length / 2];
        int start = length / 8;
        if (nextFirst > nextLast) {
            if (nextFirst == length - 1) {
                System.arraycopy(items, 0, resizedArray, start, size);
                nextFirst = start - 1;
                nextLast = start + size;
            } else {
                System.arraycopy(items, nextFirst + 1, resizedArray, start, length - nextFirst - 1);
                System.arraycopy(items, 0, resizedArray, start + (length - nextFirst - 1),
                        nextLast);
                nextFirst = start - 1;
                nextLast = start + size;
            }
        } else if (nextFirst < nextLast) {
            System.arraycopy(items, nextFirst + 1, resizedArray, start, size);
            nextFirst = start - 1;
            nextLast = start + size;
        }
        items = resizedArray;
    }

    /**
     * HELPER METHOD
     * Adds the first item to the deque.
     * @param item The item need to be added to the deque.
     */
    private void addFirstItem(T item) {
        items[3] = item;
        nextFirst = 2;
        nextLast = 4;
        size = 1;
    }
}

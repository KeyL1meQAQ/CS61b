package synthesizer;

import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {

    /**
     * @return Size of the buffer.
     */
    int capacity();

    /**
     * @return Number of item currently in the buffer.
     */
    int fillCount();

    /**
     * Adds item x to the end.
     * @param x The item.
     */
    void enqueue(T x);

    /**
     * Deletes and returns item from the front.
     * @return The item removed.
     */
    T dequeue();

    /**
     * Returns (but not delete) item from the front.
     * @return The item gotten.
     */
    T peek();

    /**
     * The iterator method.
     * @return An iterator of BoundedQueue.
     */
    @Override
    Iterator<T> iterator();

    /**
     * Is the buffer empty.
     * @return True if the buffer is empty, false otherwise.
     */
    default boolean isEmpty() {
        if (fillCount() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Is the buffer full.
     * @return True if the buffer is full, false otherwise.
     */
    default boolean isFull() {
        if (fillCount() == capacity()) {
            return true;
        }
        return false;
    }
}

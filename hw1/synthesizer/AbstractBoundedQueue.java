package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {

    /**
     * Number of item currently in the buffer.
     */
    protected int fillCount;

    /**
     * Size of the buffer.
     */
    protected int capacity;

    /**
     * @return Size of the buffer.
     */
    @Override
    public int capacity() {
        return capacity;
    }

    /**
     * @return Number of item currently in the buffer.
     */
    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Returns (but not delete) item from the front.
     * @return The item gotten.
     */
    public abstract T peek();

    /**
     * Deletes and returns item from the front.
     * @return The item removed.
     */
    public abstract T dequeue();

    /**
     * Adds item x to the end.
     * @param x The item.
     */
    public abstract void enqueue(T x);
}

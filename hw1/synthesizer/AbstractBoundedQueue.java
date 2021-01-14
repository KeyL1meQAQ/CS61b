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
}

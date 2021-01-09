/* A Deque LinkedList with circular approach */
public class LinkedListDeque<T> implements Deque<T> {

    private class Node {
        /**
         * Used to save the item.
         */
        private T item;
        /**
         * The prev points to the previous node in this list, if it is the first node,
         * it will point to the sentinel node.
         */
        private Node prev;
        /**
         * The next points to the next node in this list, if it is the last node,
         * it will point to the sentinel node.
         */
        private Node next;

        /**
         * The constructor of node.
         * @param item The item to be saved by this node.
         * @param prev The previous node of the new node.
         * @param next The next node of the new node.
         */
        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * The "actually first" node in the list is the sentinel node, the first item is sentinel.next.
     */
    private Node sentinel;
    /**
     * The size of the list
     */
    private int size;

    /**
     * The constructor, create an empty list.
     */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        size = 0;
    }

    /**
     * Add an item to the front of the list.
     * @param item The item you want to add to the front of the list.
     */
    @Override
    public void addFirst(T item) {
        if (item == null) {
            return;
        }
        if (isEmpty()) {
            addFirstItem(item);
            return;
        }
        Node node = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = node;
        sentinel.next = node;
        size++;
    }

    /**
     * Add an item to the to the back of the list.
     * @param item The item you want to add to the back of the list.
     */
    @Override
    public void addLast(T item) {
        if (item == null) {
            return;
        }
        if (isEmpty()) {
            addFirstItem(item);
            return;
        }
        Node node = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = node;
        sentinel.prev = node;
        size++;
    }

    /**
     * @Return Returns true if list is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        if (sentinel.next == null) {
            return true;
        }
        return false;
    }

    /**
     * @Return Returns the size of the list.
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
        Node N = sentinel.next;
        while (!N.equals(sentinel)) {
            System.out.print(N.item + " ");
            N = N.next;
        }
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     * @return Returns the item removed.
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size == 1) {
            return removeToInit();
        }
        Node node = sentinel.next;
        node.next.prev = sentinel;
        sentinel.next = node.next;
        node.prev = null;
        node.next = null;
        size--;
        return node.item;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     * @return Returns the item removed.
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size == 1) {
            return removeToInit();
        }
        Node node = sentinel.prev;
        sentinel.prev = node.prev;
        node.prev.next = sentinel;
        node.prev = null;
        node.next = null;
        size--;
        return node.item;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * @param index The position of the item you want to get, started by 0;
     * @return Returns the item you get.
     */
    @Override
    public T get(int index) {
        if (index >= size | index < 0) {
            return null;
        }
        Node N = sentinel.next;
        int i = 0;
        while (true) {
            if (i != index) {
                N = N.next;
                i++;
            } else {
                return N.item;
            }
        }
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Using recursion.
     * @param index The position of the item you want to get, started by 0;
     * @return Returns the item gotten.
     */
    public T getRecursive(int index) {
        if (index >= size | index < 0) {
            return null;
        }
        Node node = sentinel.next;
        return getRecursion(node, index);
    }

    /**
     * HELPER METHOD
     * The recursion of method getRecursive.
     * @param node The list.
     * @param index The position of the item.
     * @return Returns the item gotten.
     */
    private T getRecursion(Node node, int index) {
        if (index != 0) {
            node = node.next;
            index--;
            return getRecursion(node, index);
        } else {
            return node.item;
        }
    }

    /**
     * HELPER METHOD
     * Add the first item into the list.
     * @param item The first item you want to add.
     */
    private void addFirstItem(T item) {
        sentinel.next = new Node(item, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    /**
     * HELPER METHOD
     * If the list has only 1 item and the removeFirst or removeLast is called,
     * this method will be called.
     * @return Returns the item removed.
     */
    private T removeToInit() {
        Node node = sentinel.next;
        node.prev = null;
        node.next = null;
        sentinel.next = null;
        sentinel.prev = null;
        size = 0;
        return node.item;
    }
}

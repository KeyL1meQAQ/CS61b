import static org.junit.Assert.*;

import org.junit.Test;

public class TestDeque {

    @Test
    public void testGet() {
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
        linkedListDeque.addFirst(1);
        linkedListDeque.addFirst(2);
        linkedListDeque.addFirst(3);
        linkedListDeque.addFirst(4);
        linkedListDeque.addFirst(8);
        linkedListDeque.addLast(10);
        int exp = 3;
        int get = linkedListDeque.getRecursive(2);
        assertEquals(exp, get);
        int exp2 = 10;
        int get2 = linkedListDeque.getRecursive(5);
        assertEquals(exp2, get2);
    }

    @Test
    public void testArray() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        for (int i = 0; i < 14; i++) {
            arrayDeque.addFirst(i);
        }
        arrayDeque.printDeque();
        System.out.println();
        System.out.println(arrayDeque.removeFirst());
        arrayDeque.printDeque();
        System.out.println();
        System.out.println(arrayDeque.removeLast());
        arrayDeque.printDeque();
        System.out.println();
        System.out.println(arrayDeque.get(12));
    }
}

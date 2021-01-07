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
        arrayDeque.addLast(0);
        arrayDeque.removeLast()    ;
        arrayDeque.addLast(2);
        arrayDeque.addFirst(3);
        arrayDeque.get(1)      ;
        arrayDeque.get(1)      ;
        arrayDeque.addFirst(6);
        arrayDeque.addLast(7);
        arrayDeque.get(3)      ;
        arrayDeque.removeFirst()    ;
        arrayDeque.addLast(10);
        arrayDeque.get(1)      ;
        arrayDeque.addLast(12);
        arrayDeque.get(1)      ;
        arrayDeque.addLast(14);
        arrayDeque.addLast(15);
        arrayDeque.get(6)      ;
        arrayDeque.get(3)      ;
        arrayDeque.removeLast() ;
        arrayDeque.get(3)      ;
        arrayDeque.addFirst(20);
        arrayDeque.get(6)     ;
        arrayDeque.removeFirst();
        arrayDeque.removeLast();
    }
}

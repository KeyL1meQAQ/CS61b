import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {

    /**
     * The method calling sequence.
     */
    private String sequence;

    /**
     * The student made buggy array deque.
     */
    private  StudentArrayDeque<Integer> studentArrayDeque;

    /**
     * The correct array deque.
     */
    private ArrayDequeSolution<Integer> arrayDequeSolution;

    /**
     * The number of times of calling methods.
     */
    private Integer count = 1;

    /**
     * Tests the student created array deque by randomly calling the method until a failure occurs.
     */
    @Test
    public void testStudentArrayDeque() {
        sequence = "";
        arrayDequeSolution = new ArrayDequeSolution<>();
        studentArrayDeque = new StudentArrayDeque<>();
        Integer methodCode;
        while (true) {
            if (arrayDequeSolution.size() == 0) {
                methodCode = StdRandom.uniform(2);
            } else {
                methodCode = StdRandom.uniform(4);
            }
            operate(methodCode);
            count++;
        }
    }

    /**
     * HELPER METHOD
     * Operates the student array deque and the correct array deque.
     * @param methodCode The methodCode, from 0 to 3, each number represents a specific method.
     *                   0 for addFirst, 1 for addLast, 2 for removeFirst and 3 for removeLast.
     */
    private void operate(Integer methodCode) {
        Integer numberToAdd;
        Integer studentReturn;
        Integer solutionReturn;
        switch (methodCode) {
            case 0:
                numberToAdd = StdRandom.uniform(100);
                studentArrayDeque.addFirst(numberToAdd);
                arrayDequeSolution.addFirst(numberToAdd);
                if (count == 1) {
                    sequence += "addFirst(" + numberToAdd + ")";
                } else {
                    sequence += "\naddFirst(" + numberToAdd + ")";
                }
                break;
            case 1:
                numberToAdd = StdRandom.uniform(100);
                studentArrayDeque.addLast(numberToAdd);
                arrayDequeSolution.addLast(numberToAdd);
                if (count == 1) {
                    sequence += "addLast(" + numberToAdd + ")";
                } else {
                    sequence += "\naddLast(" + numberToAdd + ")";
                }
                break;
            case 2:
                studentReturn = studentArrayDeque.removeFirst();
                solutionReturn = arrayDequeSolution.removeFirst();
                sequence += "\nremoveFirst()";
                assertEquals(sequence, solutionReturn, studentReturn);
                break;
            case 3:
                studentReturn = studentArrayDeque.removeLast();
                solutionReturn = arrayDequeSolution.removeLast();
                sequence += "\nremoveLast()";
                assertEquals(sequence, solutionReturn, studentReturn);
                break;
            default:
                return;
        }
    }
}

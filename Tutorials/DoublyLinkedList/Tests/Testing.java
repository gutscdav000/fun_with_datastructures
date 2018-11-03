//import com.sun.java.util.jar.pack.ConstantPool;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static org.junit.Assert.*;


public class Testing {

    @Test
    public void testDLLSimple() {
        DoublyLinkedList<Integer> xs = new DoublyLinkedList<>();

        for (int x = 1; x <= 10; x++)
            xs.add(x);

        for (int i = 0; i < xs.size(); i++) {
            int x = xs.get(i);
            assert x == xs.get(i);
        }

        boolean thrown = false;

        try {
            xs.get(55);
        }
        catch (IndexOutOfBoundsException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testConstructor() {
        List<Integer> xs = new DoublyLinkedList<>();

        assertTrue(xs.isEmpty());

        for (int x = 1; x <= 10; x++)
            xs.add(x);

        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>(xs);

        for(int i = 0; i < 10; i++)
            assertEquals(dll.get(i), xs.get(i));
    }

    @Test
    public void testContains() {
        List<Integer> dll = new DoublyLinkedList<>();

        for (int x = 1; x <= 10; x++)
            dll.add(x);

        for(int i = 1; i <= 10; i++)
            assertTrue(dll.contains(i));

        assertFalse(dll.contains(49));
        assertFalse(dll.contains(347324));

    }

    @Test
    public void testRemove() {
        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>();

        for(int i = 1; i <= 10; i++)
            dll.add(i);

        for(int i = 1; i <= 10; i++)
            if (i % 2 == 0)
                assertTrue(dll.remove(new Integer(i)));

        assertFalse(dll.remove(new Integer(5555)));

        Integer[] a = {1, 3, 5, 7, 9,};
        int i = 0;

        while(!dll.isEmpty())
            assertEquals(a[i++], dll.remove(0));

        boolean thrown = false;

        try {
            dll.remove(55);
        }
        catch (IndexOutOfBoundsException e) {
            thrown = true;
        }
        assertTrue(thrown);

        assertEquals("()", dll.toString());

        // repopulate
        for(i = 0; i < 5; i++)
            dll.add(i);

        assertEquals("(0 1 2 3 4)", dll.toString());
        assertEquals(new Integer(4), dll.remove(4));
        assertEquals(new Integer(3), dll.remove(3));
    }


    @Test
    public void testIteratorExceptions() {

        DoublyLinkedList<Integer> dll = new DoublyLinkedList<>();
        for(int i = 0; i < 10; i++)
            dll.add(i);

        Iterator it = dll.iterator();
        boolean thrown = false;

        try {
            it.next();
            it.remove();
            it.remove();
        }
        catch (IllegalStateException e) {
            thrown = true;
        }
        assertTrue(thrown);

        DoublyLinkedList<Integer> dll2 = new DoublyLinkedList<>();
        for(int i = 0; i < 10; i++)
            dll2.add(i);

        it = dll2.iterator();
        thrown = false;

        try {
            it.next();
            dll2.remove(2);
            it.next();
        }
        catch (ConcurrentModificationException e) {
            thrown = true;
        }
        assertTrue(thrown);


    }

}

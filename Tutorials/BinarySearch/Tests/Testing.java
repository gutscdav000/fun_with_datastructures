import org.junit.*;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import java.util.*;


public class Testing {

    @Test
    public void testingBinarySearch() {

    int a1[] = {1, 3, 5, 7, 9, 11};

    // match
    assertTrue(BinarySearch.retrieve(a1, 5));
    assertTrue(BinarySearch.retrieve(a1, 1));
    assertTrue(BinarySearch.retrieve(a1, 11));
    assertTrue(BinarySearch.retrieve(a1, 9));
    assertTrue(BinarySearch.retrieve(a1, 3));
    assertTrue(BinarySearch.retrieve(a1, 7));

    // no match
    assertFalse(BinarySearch.retrieve(a1, 0));
    assertFalse(BinarySearch.retrieve(a1, 2));
    assertFalse(BinarySearch.retrieve(a1, 4));
    assertFalse(BinarySearch.retrieve(a1, 6));
    assertFalse(BinarySearch.retrieve(a1, 8));
    assertFalse(BinarySearch.retrieve(a1, 10));
    assertFalse(BinarySearch.retrieve(a1, 12));


    int a2[] = {345, 654, 958, 223, 1000, 1500, 2000};
    Arrays.sort(a2);

    // match
    assertTrue(BinarySearch.retrieve(a2, 1500));
    assertTrue(BinarySearch.retrieve(a2, 2000));
    assertTrue(BinarySearch.retrieve(a2, 1000));
    assertTrue(BinarySearch.retrieve(a2, 223));
    assertTrue(BinarySearch.retrieve(a2, 345));
    assertTrue(BinarySearch.retrieve(a2, 958));
    assertTrue(BinarySearch.retrieve(a2, 654));

    //no match
    assertFalse(BinarySearch.retrieve(a2, 0));
    assertFalse(BinarySearch.retrieve(a2, 15));
    assertFalse(BinarySearch.retrieve(a2, 200));
    assertFalse(BinarySearch.retrieve(a2, 400));
    assertFalse(BinarySearch.retrieve(a2, 700));
    assertFalse(BinarySearch.retrieve(a2, 988));
    assertFalse(BinarySearch.retrieve(a2, 1100));
    assertFalse(BinarySearch.retrieve(a2, 1700));
    assertFalse(BinarySearch.retrieve(a2, 2200));
    }
}

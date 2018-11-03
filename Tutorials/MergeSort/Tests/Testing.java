import org.junit.Test;
import org.junit.Assert;
import org.junit.*;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Testing {

    @Test
    public void testingMergeSort() {

        int a1[] = {1,};
        int a2[] = {1, 2, 3, 4, 5,};
        int a3[] = {12, 6, 2, 3, 5, 4,};

        MergeSort.mergeSort(a1);
        assertArrayEquals(a1, new int[]{1});





        MergeSort.mergeSort(a2);
        assertArrayEquals(a2, new int[]{1, 2, 3, 4, 5});


        MergeSort.mergeSort(a3);
        assertArrayEquals( a3, new int[]{2, 3, 4, 5, 6, 12});

        int a4[] = {6958, 300, 1000, 12000, 900, 9000, 1500};
        MergeSort.mergeSort(a4);
        assertArrayEquals(a4, new int[]{300, 900, 1000, 1500, 6958, 9000, 12000});




/*        for(int i : a2)
            System.out.print(i + " ");
*/

    }
}

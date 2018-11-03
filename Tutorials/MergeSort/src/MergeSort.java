public class MergeSort {

    //static function to sort an array of integers
    public static void mergeSort(int[] array) {
        mergeSort(array, new int[array.length], 0, array.length - 1);
    }

    /**private helper to recursively segment the array
     * @param array: the array being sorted
     * @param temp: a copy of the array for merging halves
     * @param start: the left index of our current range
     * @param end: the right index of our current range
     */
    private static void mergeSort(int[] array, int[] temp, int start, int end) {
        // base case: when pointers have overtaken each other
        if(start >= end)
            return;

        int partition = (start + end) / 2; //midpoint

        //recur
        mergeSort(array, temp, start, partition);
        mergeSort(array, temp,partition + 1, end);

        // sort halves before propogating up
        sortHalves(array, temp, start, end);
    }

    /**private helper to sort each half of the list per recursive call
     * @param array: the array being sorted
     * @param temp: a copy of the array for merging halves
     * @param start: the left index of our current range
     * @param end: the right index of our current range
     */
    private static void sortHalves(int[] array, int[] temp, int start, int end) {
        // partition bounds
        int leftEnd = (start + end) / 2;
        int rightStart = leftEnd + 1;
        int size = end - start + 1;

        // index pointers
        int left = start;
        int right = rightStart;
        int index = start;

        // walk through halves and copy over the smaller item
        while(left <= leftEnd && right <= end) {
            if(array[left] <= array[right])
                temp[index] = array[left++];
            else
                temp[index] = array[right++];

            index++;
        }

        //copy the remainder of elements after pointers go out of range
        System.arraycopy(array, left, temp, index,leftEnd - left + 1); // remaining elements from left
        System.arraycopy(array, right, temp, index, end - right +  1); // remaining elements from right
        System.arraycopy(temp, start, array, start, size); // copy everything from temp back into array
    }

    /*public static String toString() {

    }*/

}

public class BinarySearch {


    public static boolean retrieve(int[] array, int match) {
        return retrieve(array, match,0, array.length - 1);
    }

    private static boolean retrieve(int [] array, int match, int start, int end) {
        // base case
        if(start >= end)
            return array[start] == match;

        int middle = ((end - start) / 2) + start;

        // if they match
        if(array[middle] == match)
            return true;
        // if match is greater than middle
        else if(match > array[middle])
            return retrieve(array, match, middle + 1, end);
        // if match is less than middle
        else
           return retrieve(array, match, start, middle);

    }
}

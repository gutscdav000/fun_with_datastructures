/** 
 * Here's a starter file to help get you going on Problem 1.6 in Weiss,
 * which is the first problem on hw1.
 *
 * @author <put your name here>
 */
import java.lang.String;

public class Permutations {

  /**
   * Print all permutations of the given string. 
   */

  // testing fields
  public static String[] permutationTest = new String[24];
  public static int permutationIndex = 0;

  private static int factorial(int fac) {
    return factorialHelper(fac, fac);
  }

  private static int factorialHelper(int num, int result) {
    // base case
    if(num == 0 || num == 1) return result;
    --num;
    return factorialHelper(num, (result * num) );

  }

  public static void permute(String str) {
    permute(str.toCharArray(), 0, str.length());
  }
  
  /**
   * Print those permutations of the given string such that the characters 
   * at index low up to (but not including) high vary, and the remaining
   * characters are fixed. For example, if str corresponds to "abCDEfgh",
   * low is 2, and high is 5, then the following output is produced:
   *    abCDEfgh
   *    abCEDfgh
   *    abDCEfgh
   *    abDECfgh
   *    abEDCfgh
   *    abECDfgh
   *
   * @param str is an array representation of the string we wish to permute.
   * @param low is the index of the leftmost character in the fluid range.
   * @param high is one beyond the index of the rightmost character in the 
   * fluid range.
   */

  public static void permute(char[] str, int low, int high) {
      char hold;
      //high = high - 1;  // makes high an index
      if(low == high - 1) {
          String tmp = new String(str);
          System.out.println(tmp);
          //FOR TESTING PURPOSES
          permutationTest[permutationIndex] = tmp;
          ++permutationIndex;
          return;
      }



      permute(str, low + 1, high); // permutes index 0

      for(int i = low + 1; i < high; ++i) {
          // print enumeration
          //System.out.println(new String(str));

          // swap elements
          hold = str[low];
          str[low] = str[i];
          str[i] = hold;

          // recur
          permute(str, low + 1, high);

          hold = str[low];
          str[low] = str[i];
          str[i] = hold;

      }

  }

  /*
        circle back to this attempt if nothing else works
  public static void permute(char[] str, int low, int high) {
      char curChar;
      if (low == high) return;

      for(int i = low; i != high; ++i) {
          str[i] = curChar;
          System.out.print("a");
          for(int j = i + 1;;)

          permute(str, low + 1, high)
      }
  }*/


  /*
  private static void permuteHelper(char[] str, int low, int high, int iter, int index) {
    if (factorial(high - low) == iter) return;

    System.out.println(Integer.toString(iter) + ",  " + new String(str));
    char hold = str[low];
    str[low] = str[index];
    str[index] = hold;

    permuteHelper(str, low, high, ++iter, ++index == high ? low + 1: index);
  }*/


  
  public static void main(String[] args) {


    permute("abcd");

    String[] test1 = new String[] {"abcd", "abdc", "acbd", "acdb", "adcb", "adbc", "bacd", "badc", "bcad", "bcda", "bdca", "bdac", "cbad","cbda", "cabd", "cadb", "cdab", "cdba", "dbca", "dbac", "dcba", "dcab", "dacb", "dabc"};

    assert test1.equals(permutationTest);

    /* Expected output:
       abcd
       abdc
       acbd
       acdb
       adcb
       adbc
       bacd
       badc
       bcad
       bcda
       bdca
       bdac
       cbad
       cbda
       cabd
       cadb
       cdab
       cdba
       dbca
       dbac
       dcba
       dcab
       dacb
       dabc
     */



      System.out.println("Permutation test passed. Woooh!");

  }  
}

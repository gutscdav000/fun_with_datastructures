/**
 *
 * @author David Gutsch
 */
import javax.print.DocFlavor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.LinkedList;

public class Permutations {

  /**
   * Print all permutations of the given string. 
   */

  // testing fields
  private static LinkedList<String> permutationTest = new LinkedList();
  //public static int permutationIndex = 0;

  private static int factorial(int fac) {
    return factorialHelper(fac, fac);
  }

  private static int factorialHelper(int num, int result) {
    // base case
    if(num == 0 || num == 1) return result;
    --num;
    return factorialHelper(num, (result * num) );

  }

  public static LinkedList<String> permute(String str) {
    permute(str.toCharArray(), 0, str.length());

    return permutationTest;
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
          //System.out.println(tmp);

          //append to list
          permutationTest.add(tmp);
          //++permutationIndex;

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




  
  public static void main(String[] args) {

    // permutation test
    LinkedList<String> test = permute("abcd");

    String[] test1 = new String[] {"abcd", "abdc", "acbd", "acdb", "adcb", "adbc", "bacd", "badc", "bcad", "bcda", "bdca", "bdac", "cbad","cbda", "cabd", "cadb", "cdab", "cdba", "dbca", "dbac", "dcba", "dcab", "dacb", "dabc"};

      String[] permArr = new String[permutationTest.size()];
      int i = 0;

    System.out.println("break");
    for(String str : test) {
        System.out.println(str);
    }

    //assert test1.equals(permArr);

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

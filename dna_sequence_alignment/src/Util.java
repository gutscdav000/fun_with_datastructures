import java.awt.Color;
import java.util.Iterator;

/**
 * There's a moderate amount to do here, but only after you have
 * completed the ColorTable class. 
 *
 */

public class Util {

  /**  
   * TO DO
   *
   * Implement cosineSimilarity() using the formula given here: 
   * 
   *         https://en.wikipedia.org/wiki/Cosine_similarity
   *    
   *     Hints:
   *     -- The result of multiplying in the dot product should be
   *        of type double.
   *
   *     -- Consider defining two private helper methods: one to
   *        compute the dot product and one to find the vector 
   *        magnitude.
   *
   *     -- Use a ColorTable.iterator() to traverse the vector of
   *        frequency counts in the color table.
   */


  /*
  cosineSimilarity finds the similarity in direction and magnitude of the
  this function depends on the dotProduct and magnitude functions
  @param is a colorTable that is created from an image by the vectorize function
  @param is a colorTable that is created from an image by the vectorize function
  @return of type double with the value of cosine similarity.
   */
  public static double cosineSimilarity(ColorTable A, ColorTable B) {
    return ((dotProduct(A,B)) / (magnitude(A) * magnitude(B)));
  }

  /*
  this function determines the length of the vector and requires the use of an iterator
  @param of type colorTable corresponding to the image whose magnitude is desired
  @return a double that is the magnitude for the colorTable
   */
  public static double magnitude(ColorTable c) {
    Iterator<Long> it = c.iterator();
    double prod = 0.0;

    while(it.hasNext())
      prod += Math.pow(it.next(), 2);

    return  Math.sqrt(prod);

  }

  /*
    this method provides the dotProduct of two color tables by multiplying together
    all of their components using an iterator
    @param of type colorTable that will be used to multiply one of the components
    @param of type colorTable that will be used to multiply the components of the other colorTable
    @return a double with the numeric value of the dotproduct for the two colortables
   */
    public static double dotProduct(ColorTable A, ColorTable B) {
      Iterator<Long> itA = A.iterator(), itB = B.iterator();
      double prod = 1.0;

      while(itA.hasNext() && itB.hasNext())
        prod += itA.next() * itB.next();

      return prod;
    }



  /**
   * To Do
   * 
   * Return the ColorTable associated with this image, assuming the
   * color key space is restricted to bitsPerChannel. Consult the
   * Image class for methods to find the width and height of the
   * image, and the pixel color at a particular (x, y) coordinate.
   *25 .7   |  13 .4        */


  /*
  vectorize is responsible for converting an image into a color table with a specified
  amount of bits for each channel. This is how we empirically quantify the image.
  @param an Image object that will be used to build the colorTable
  @param an integer representing the number of bits that will be used to represent each pixel
  @return a colorTable containing the numeric representation of the image as Pixel : Frequency associations
   */
  public static ColorTable vectorize(Image image, int bitsPerChannel) {
    // using .7 is a standard I was told?   also was recommended to try initialCap: 13 and rehasThresh: .4
    ColorTable ct = new ColorTable(13, bitsPerChannel, 0.4);
    Color umaCor;

    for(int i = 0; i < image.getWidth(); i++) {
      for(int j = 0; j < image.getHeight(); j++) {
        umaCor = image.getColor(i,j);
        ct.increment(umaCor);
      }
    }
    return ct;
  }

  /**
   * To Do
   * 
   * Return the result of running Util.cosineSimilarity() on the
   * vectorized images.
   * 
   * Note: If you compute the similarity of an image with itself, it
   * should be close to 1.0.
   */

  /*
  This function compares two images using the cosineSimilarity function. The colorTable
  structure was needed to house the color : frequency associations. and the vectorize function
  was needed to obtain the colorTable from the image
  @param Image object corresponding to the first image to be compared
  @param Image object corresponding to the second image to be compared
  @param an integer representing the number of bits that will be used to represent each pixel
  @return a double corresponding to the similarity value
   */
  public static double similarity(Image image1, Image image2, 
                                  int bitsPerChannel) {
    return cosineSimilarity(vectorize(image1, bitsPerChannel), vectorize(image2, bitsPerChannel));

  }

  /**
   * Returns true iff n is a prime number. We handles several common
   * cases quickly, and then use a variation of the Sieve of
   * Eratosthenes.
   */

  /*
  the isPrime function is returning whether or not the value is prime
  @param integer for the value to be checked if it is prime
  @return a boolean value whether or not the integer is prime
   */
  public static boolean isPrime(int n) {
    if (n < 2) 
      return false;
    if (n == 2 || n == 3) 
      return true;
    if (n % 2 == 0 || n % 3 == 0) 
      return false;
    long sqrtN = (long) Math.sqrt(n) + 1;
    for (int i = 6; i <= sqrtN; i += 6) {
      if (n % (i - 1) == 0 || n % (i + 1) == 0) 
        return false;
    }
    return true;
  }

}

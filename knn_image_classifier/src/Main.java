import javax.swing.SwingUtilities;
import java.io.File;

/**
  * In this project, you will implement the k-Nearest Neighbor algorithm
 * to classify an image of a single handwritten digit 0-9, as described
 * in lecture.
 *
 * This is the main entry point for the GUI application.
 */

public class Main {

  public static void main(final String... args) {
    System.out.println(Constants.TITLE);
    SwingUtilities.invokeLater(() -> new GUI());
  }

}


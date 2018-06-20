import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class DigitDisplay extends JPanel {

  private Digit digit;

  /**
   * Creates a blank display.
   */

  public DigitDisplay() {
    this(new Digit());
  }

  /**
   * Creates a display for the given digit.
   */

  public DigitDisplay(Digit digit) {
    this.digit = digit;
  }

  /**
   * Returns the preferred size of this display.
   */

  public Dimension getPreferredSize() {
    int dim = Constants.DIM * Constants.RESOLUTION_UNIT;
    return new Dimension(dim, dim);
  }

  /**
   * Uses the provided graphics context to draw the associated digit
   * on the display.
   */

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    digit.draw(g2);
    g2.dispose();
  }

  /**
   * Updates and redraws the digit on this display.
   */

  public void setDigit(Digit digit) {
    this.digit = digit;
    repaint();
  }

  /**
   * Returns the digit associated with this display.
   */

  public Digit getDigit() {
    return digit;
  }
  
}

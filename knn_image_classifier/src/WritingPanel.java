import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import java.util.List;
import java.util.ArrayList;

/**
 * Panel for handwritten digits. Automatically quantizes the handwriting into
 * a grayscale encoding consistent with the MNIST database.
 */

public class WritingPanel extends JPanel {
  private boolean clear;
  private int res = Constants.RESOLUTION_UNIT;
  private List<Point> curve;
  private BufferedImage image;
  private String code;

  /**
   * Creates the panel on which the user hand-writes a digit. Listens for
   * mouse events and adds the associated points to the curve list.
   */

  public WritingPanel() {
    curve = new ArrayList<>();
    MouseAdapter mouseAdapter = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        image = null;
        curve.add(e.getPoint());
        repaint();
      }

      public void mouseDragged(MouseEvent e) {
        curve.add(e.getPoint());
        repaint();
      }

      public void mouseReleased(MouseEvent e) {
        curve.add(Constants.END_OF_STROKE); 
        image = quantize();
        repaint();
      }
    };

    addMouseListener(mouseAdapter);
    addMouseMotionListener(mouseAdapter); 
    setMaximumSize(getPreferredSize());
    setMinimumSize(getPreferredSize());
  }

  /**
   * Returns the preferred size of this panel.
   */

  public Dimension getPreferredSize() {
    int dim = Constants.DIM * res;
    return new Dimension(dim, dim);
  }

  /**
   * Encodes the hand-drawn image as a csv string and updates the display as
   * as a canonical grayscale digit.
   */

  public BufferedImage quantize() {
    int width = getWidth(), height = getHeight();
    BufferedImage image =
        new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();
    printAll(g2);
    g2.dispose();
    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < height; y += res) 
      for (int x = 0; x < width; x += res) {
        int gray = 0;
        for (int i = 0; i < res; i++)
          for (int j = 0; j < res; j++)
            gray += new Color(image.getRGB(x + i, y + j)).getRed();
        gray = gray / (res * res);
        sb.append(gray).append(",");
        gray = new Color(gray, gray, gray).getRGB();
        for (int i = 0; i < res; i++)
          for (int j = 0; j < res; j++)
            image.setRGB(x + i, y + j, gray);
      }
    code = sb.toString();
    return image;
  }

  /**
   * Uses the provided graphics context to draw the current digit on this
   * panel by connecting adjacent points along the curve with line
   * segments.
   */

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();

    if (isOpaque()) {
      g2.setColor(Color.BLACK);
      g2.fillRect(0, 0, getWidth(), getHeight());
    }
    if (clear) {
      clear = false;
      curve.clear();
      image = null;
    }
    else if (image != null) 
      g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    else {
      if (!curve.isEmpty()) {
        // Draw Lines between successive points along the curve.
        g2.setPaint(Constants.CURVE_COLOR);
        g2.setStroke(new BasicStroke(7.0f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < curve.size() - 1; i++) {
          Point p = curve.get(i);
          Point q = curve.get(i + 1);
          // Take care not to connect separate components.
          if (q.equals(Constants.END_OF_STROKE))
            i++;
          else 
            g2.drawLine(p.x, p.y, q.x, q.y);		
        }
      }
    }
    g2.dispose(); // be polite
  }

  /**
   * Returns the csv encoding associated with the handwritten digit.
   */

  public String getCode() {
    return code;
  }

  /**
   * Sets the resolution used to quantize the curve.
   */

  public void setResolution(int res) {
    this.res = res;
  }

  /**
   * Indicates whether or not this panel should be cleared before collecting
   * the points on the next curve.
   */

  public void setClear(boolean b) {
    clear = b;
    repaint();
  }
}

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SlideShow extends JFrame {
  private int cursor = 0;

  /**
   * Creates a show for the entire dataset.
   */

  public SlideShow(Dataset dataset) {
    this(dataset, 0);
  }

  /**
   * Creates a show for the digits in the dataset from start until the end.
   */

  public SlideShow(Dataset dataset, int start) {
    super("Review");
    assert start >= 0;
    int n = dataset.size();
    assert start <= n;
    cursor = start;
    JPanel controls = new JPanel(new GridLayout(1, 3));
    
    JButton backButton, forwardButton, trashButton;
    backButton = new JButton(null,
        new ImageIcon(Constants.ICON_DIR + "/backward.png"));
    forwardButton = new JButton(null,
        new ImageIcon(Constants.ICON_DIR + "/forward.png"));
    trashButton = new JButton(null,
        new ImageIcon(Constants.ICON_DIR + "/trash.png"));

    ImageIcon disabledIcon =
        new ImageIcon(Constants.ICON_DIR + "/disabled.png");
    backButton.setDisabledIcon(disabledIcon);
    forwardButton.setDisabledIcon(disabledIcon);
    trashButton.setDisabledIcon(disabledIcon);

    backButton.setFocusPainted(false);
    forwardButton.setFocusPainted(false);
    trashButton.setFocusPainted(false);
    
    backButton.setContentAreaFilled(false);
    forwardButton.setContentAreaFilled(false);
    trashButton.setContentAreaFilled(false);

    DigitDisplay datum =
        new DigitDisplay(cursor == n ? new Digit() : dataset.get(cursor));
    backButton.setEnabled(false);
    if (cursor >= n - 1) {
      forwardButton.setEnabled(false);
      if (cursor == n) // no slides in the show
        trashButton.setEnabled(false);
    }
    
    controls.add(backButton);
    controls.add(forwardButton);
    controls.add(trashButton);

    backButton.addActionListener(e -> {
      forwardButton.setEnabled(true);
      cursor--;
      datum.setDigit(dataset.get(cursor));
      if (cursor == start)
        backButton.setEnabled(false);
    });

    forwardButton.addActionListener(e -> {
      backButton.setEnabled(true);
      cursor++;
      datum.setDigit(dataset.get(cursor));
      if (cursor == dataset.size() - 1)
        forwardButton.setEnabled(false);
    });

    trashButton.addActionListener(e -> {
      dataset.remove(cursor);
      int end = dataset.size() - 1;
      cursor = Math.min(cursor, end);
      if (cursor == start)
        backButton.setEnabled(false);
      if (cursor == end)
        forwardButton.setEnabled(false);
      if (start > end) {
        trashButton.setEnabled(false);
        datum.setDigit(new Digit());
      }
      else
        datum.setDigit(dataset.get(cursor));
    });

    Container pane = getContentPane();
    pane.add(datum, BorderLayout.CENTER);
    pane.add(controls, BorderLayout.SOUTH);

    pack();
    Dimension dim = getPreferredSize();
    dim.width = datum.getPreferredSize().width;
    setPreferredSize(dim);
    
    pack();
    setResizable(false);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Simple testing.
   */

  public static void main(String[] args) {
    Dataset dataset = new Dataset(Constants.TRAINING_DATA);
    new SlideShow(dataset, dataset.size() - 5).setVisible(true);
  }

}
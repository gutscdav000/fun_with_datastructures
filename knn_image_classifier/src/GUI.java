import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class GUI extends JFrame {  

  // Boilerplate setup.
  static {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
  
  class CustomButton extends JButton {
    CustomButton() {
      setForeground(new Color(0xEEEDEB));
      setBackground(new Color(0x01426A));
      setFocusPainted(false);
    }
  }
  
  private static Dataset dataset = new Dataset();
  
  private WritingPanel drawingPanel = new WritingPanel();
  private DigitDisplay matchPanel = new DigitDisplay();
  private DigitDisplay[] neighborsPanel = new DigitDisplay[Constants.K];
  private JButton[] trainingButtons = new JButton[10];
  private JButton classifyButton;
  private JLabel addLabel;
  private JLabel instructLabel;
  private JLabel sizeLabel;
  private int from;

  /**
   * Sets up and displays the graphical user interface.
   */

  public GUI() { 
    setTitle(Constants.TITLE);
    setLocation(50, 50);
    Container main = getContentPane();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
    main.add(controlPanel());
    main.add(Box.createVerticalStrut(10)); 
    main.add(screen());
    main.add(Box.createVerticalStrut(15)); 
    main.add(addToTraining());
    main.add(Box.createVerticalStrut(15)); 
    main.add(leftJustify(neighborPanel()));
    pack();
    setVisible(true);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  private JComponent screen() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(leftJustify(new JLabel(" Draw a digit here:", JLabel.LEFT)));
    panel.add(Box.createVerticalStrut(15)); 
    JPanel screens = new JPanel();
    screens.setLayout(new BoxLayout(screens, BoxLayout.X_AXIS));
    screens.add(Box.createHorizontalStrut(15)); 
    screens.add(drawingPanel);
    drawingPanel.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        for (int i = 0; i < 10; i++)
          trainingButtons[i].setEnabled(true);
      }
    });
    screens.add(Box.createHorizontalStrut(15)); 
    JPanel labels = new JPanel();
    labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
    sizeLabel = new JLabel("Size of training dataset: " + dataset.size(),
        JLabel.LEFT);
    instructLabel = new JLabel("<html>Start by loading a dataset to train " +
        "the machine.&nbsp;<br>&nbsp;<html>",
        JLabel.LEFT);
    instructLabel.setForeground(Constants.INSTRUCT_COLOR);
    labels.add(sizeLabel);
    labels.add(Box.createVerticalStrut(15)); 
    labels.add(instructLabel);
    screens.add(labels);
    screens.add(matchPanel);
    screens.add(Box.createHorizontalStrut(15)); 
    panel.add(screens);
    return panel;
  }
 
  private JComponent neighborPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(leftJustify(new JLabel(" Nearest Neighbors: k = " +
        Constants.K, JLabel.LEFT)));
    JPanel views = new JPanel(new FlowLayout(FlowLayout.LEFT));
    for (int i = 0; i < neighborsPanel.length; i++) 
      views.add(neighborsPanel[i] = new DigitDisplay());
    panel.add(views);
    return panel;
  }
  
  private JComponent controlPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    panel.add(clearButton());
    panel.add(classifyButton = classifyButton());
    panel.add(loadButton());
    panel.add(saveButton());
    panel.add(reviewButton());
    return leftJustify(panel);
  }
  
  private JComponent clearButton() {
    JButton button = new CustomButton();
    button.setText("Clear");
    button.addActionListener(e -> {
      processClear();
    });
    return button;
  }
  
  private JButton classifyButton() {
    JButton button = new CustomButton();
    button.setText("Classify");
    button.addActionListener(e -> {
      if (dataset.size() == 0)
        JOptionPane.showMessageDialog(this,
            "Load a training dataset first.");
      else if (dataset.size() < Constants.K)
        JOptionPane.showMessageDialog(this,
            "Not enough training data to classify.");
      else try {
        String code = drawingPanel.getCode();
        Digit unknown = new Digit(code);
        Digit bestMatch = dataset.knn(Constants.K, unknown);
        matchPanel.setDigit(bestMatch);
        bestMatch.select();
        Digit[] neighbors = dataset.getNeighbors();
        for (int i = 0; i < Constants.K; i++)
          neighborsPanel[i].setDigit(neighbors[i]);
        for (int i = 0; i < trainingButtons.length; i++) 
          trainingButtons[i].setEnabled(true);
        instructLabel.setText("<html>Add this digit to the training dataset," +
            "<br>or press Clear to draw a new digit.</html>");
      } catch (UnsupportedOperationException ex) {
        JOptionPane.showMessageDialog(this,
            "kNN algorithm has not been implemented.");
      }
    });
    button.requestFocus();
    return button;
  }

  private JComponent addToTraining() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    addLabel = new JLabel(" Add handwritten digit to training set with classification: ",
        JLabel.LEFT);
    panel.add(leftJustify(addLabel));
    
    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    for (int d = 0; d < 10; d++) {
      JButton button = new JButton(d + "");
      trainingButtons[d] = button;
      button.setEnabled(false);
      button.setFont(new Font("Arial", Font.PLAIN, 18));
      button.setForeground(Constants.BUTTON_ON);
      button.setBackground(Color.BLACK);
      button.addActionListener(e -> {
        Digit known = new Digit(((JButton) e.getSource()).getText() + "," +
            drawingPanel.getCode());
        dataset.add(known);
        updateSize();
        processClear();
      });
      buttons.add(button);
    }
    panel.add(buttons);
    return panel;
  }
  
  private JComponent loadButton() {
    JButton button = new CustomButton();
    button.setText("Load Dataset");
    button.addActionListener(e -> {
      instructLabel.setText("<html>Draw a digit. Then press the Classify " +
          "button to run knn.&nbsp;<br>&nbsp;<html>");
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File(Constants.DATA_DIR));
      int retrival = chooser.showOpenDialog(this);
      if (retrival == JFileChooser.APPROVE_OPTION) {
        dataset.load(chooser.getSelectedFile().getAbsolutePath());
        from = dataset.size();
        updateSize();
      }
    });
    return button;
  }
  
  private JComponent saveButton() {
    JButton button = new CustomButton();
    button.setText("Save Dataset");
    button.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File(Constants.DATA_DIR));
      int retrival = chooser.showSaveDialog(this);
      if (retrival == JFileChooser.APPROVE_OPTION)
        dataset.save(chooser.getSelectedFile().getAbsolutePath());
    });
    return button;
  }
  
  private JComponent reviewButton() {
    JButton button = new CustomButton();
    button.setText("Review Recent Adds");
    button.addActionListener(e -> {
      if (from == dataset.size())
        JOptionPane.showMessageDialog(this,
            "Nothing has been added yet.");
      else {
        JFrame frame = new SlideShow(dataset, from);
        frame.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            updateSize();
          }
        });
        frame.setLocationRelativeTo(this);
        frame.setLocation(624, 94);
        frame.setVisible(true);
      }
    });
    return button;
  }
  
  private JComponent leftJustify(JComponent comp)  {
    Box box = Box.createHorizontalBox();
    box.add(comp);
    box.add(Box.createHorizontalGlue());
    return box;
  }
  
  private void updateSize() {
    sizeLabel.setText("Size of training dataset: " + dataset.size());
    sizeLabel.repaint();
  }
  
  private void processClear() {
    drawingPanel.setClear(true);
    matchPanel.getDigit().deselect();
    matchPanel.setDigit(new Digit());
    for (int i = 0; i < Constants.K; i++)
      neighborsPanel[i].setDigit(new Digit());
    for (int i = 0; i < trainingButtons.length; i++) 
      trainingButtons[i].setEnabled(false);
    instructLabel.setText("<html>Draw a digit. Then press the Classify button" +
        " to run kNN.&nbsp;<br>&nbsp;<html>");
  }
}

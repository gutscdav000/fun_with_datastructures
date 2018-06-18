import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

/**
 * There's nothing for you to do here.
 */

public class GUI extends JFrame {
  
  static {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      ToolTipManager.sharedInstance().setInitialDelay(0);
      ToolTipManager.sharedInstance().setDismissDelay(1500);
      UIManager.put("ToolTip.background", Constants.PATH_COLOR);
      UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder());
      UIManager.put("MenuItem.foreground", Constants.MENU_COLOR);
      UIManager.put("PopupMenu.border", BorderFactory.createEmptyBorder());
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }
  
  private JCheckBox showPath;
  private JLabel sLabel, tLabel;
  private JLabel[][] cells;
  private SequenceAligner strands;
  
  public GUI(SequenceAligner strands) {
    setTitle(Constants.TITLE);
    this.strands = strands;
    String s = strands.getS(), t = strands.getT();
    int numRows = s.length() + 1; // Rows are labeled with chars in s
    int numCols = t.length() + 1; // Cols are labeled with chars in t

    JPanel grid = new JPanel(new GridLayout(numRows, numCols, 2, 2));
    grid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

    // Set up the look and feel
    Font charFont = new Font("Courier", Font.BOLD, 28),
        scoreFont = new Font("Ariel", Font.PLAIN, 18);
    
    cells = new JLabel[numRows][numCols];
    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        JLabel cell = new JLabel() {
          @Override
          public Point getToolTipLocation(MouseEvent event) {
            return new Point(0, 0);
          }
        };
        cells[row][col] = cell;
        cell.setOpaque(true);
        cell.setBackground(Constants.CELL_COLOR);
        cell.setHorizontalAlignment(JLabel.CENTER);
        if (row == 0 || col == 0) {
          if (row > 1 || col > 1) {
            // label the rows with chars from s and the columns with chars from t
            cell.setFont(charFont);
            cell.setForeground(Constants.NUCLEOTIDE_COLOR);
            cell.setText((row == 0 ? t.charAt(col - 1) : s.charAt(row - 1)) +
                "");
            cell.setComponentPopupMenu(new ACTG(cell, row, col));
            if (row > 1)
              cell.setToolTipText("<html>s<sub>" + (row - 1) + "</sub></html>");
            else
              cell.setToolTipText("<html>t<sub>" + (col - 1) + "</sub></html>");
          }
        }
        else {
          cell.setFont(scoreFont);
          // Change text color in start and end cells of path
          if (row == 1 && col == 1)
            cell.setForeground(new Color(0, 180, 0));  // dark green
          if (row == numRows - 1 && col == numCols - 1)
            cell.setForeground(Color.RED);    
        }
        grid.add(cell);
      }
    }
    
    grid.setPreferredSize(new Dimension(numCols * Constants.CELL_DIM,
        numRows * Constants.CELL_DIM));
    grid.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // top, left, bottom, right
        
    JPanel result = new JPanel();
    result.setLayout(new GridLayout(0, 1));
    result.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 0));
    
    sLabel = new JLabel();
    tLabel = new JLabel();
    sLabel.setFont(new Font("Courier", Font.PLAIN, 22));
    tLabel.setFont(new Font("Courier", Font.PLAIN, 22));
    result.add(sLabel);
    result.add(tLabel);
    showAlignment();
    
    JPanel controls = new JPanel();
    controls.add(result);
    controls.add(Box.createRigidArea(new Dimension(30, 0)));
    showPath = new JCheckBox("Show path");
    showPath.setFont(new Font("", Font.PLAIN, 18));
    showPath.setFocusPainted(false);
    showPath.addItemListener(e -> repaint());
    controls.add(showPath);
    
    JPanel main = new JPanel(new BorderLayout());
    main.add(grid, BorderLayout.CENTER);
    main.add(controls, BorderLayout.SOUTH);
    
    setContentPane(main);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  /**
   * Paint the GUI.
   */

  public void paint(Graphics g) {
    super.paint(g);
    showCache();
    showAlignment();
  }
  
  private void showCache() {
    String s = strands.getS(), t = strands.getT();
    int numRows = s.length();
    int numCols = t.length();
    for (int row = 1; row <= numRows; row++) {
      for (int col = 1; col <= numCols; col++) {  
        JLabel cell = cells[row][col];
        Result result = strands.getResult(row - 1, col - 1);
        if (result == null)
          cell.setToolTipText(String.format("[%d][%d]", row - 1, col - 1));
        else {
          cell.setToolTipText(result.getParent().toString());
          cell.setBackground(Constants.CELL_COLOR);
          if (showPath.isSelected() && result.onPath())   
            cell.setBackground(Constants.PATH_COLOR);
          if (numRows <= Constants.MAX_CELLS && numCols <= Constants.MAX_CELLS) 
 //           if (row * numCols + col < 43)
            cell.setText(result.getScore() + "");
        }
      }
    }
  }
  
  private void showAlignment() {
    String s = strands.getAlignedS();
    sLabel.setText("s:" + (s == null ? strands.getS() : " " + s));
    String t = strands.getAlignedT();
    tLabel.setText("t:" + (t == null ? strands.getT() : " " + t));
  }
  
  /**
   * Context-sensitive menu that allows the user to change one nucleotide
   * in a strand.
   */

  class ACTG extends JPopupMenu {    
    ACTG(JLabel cell, int row, int col) {
      for (int i = 0; i < 4; i++) {
        String nucleotide = "ACTG".substring(i, i + 1);
        JMenuItem item = new JMenuItem(nucleotide);
        item.addActionListener(e -> {
          if (cell.getText().charAt(0) != nucleotide.charAt(0)) {
            cell.setText(nucleotide);
            String s = strands.getS().substring(1),
                t = strands.getT().substring(1);
            if (col == 0)
              s = replaceChar(s, row - 2, nucleotide);
            else
              t = replaceChar(t, col - 2, nucleotide);
            // GUI.this.replaceStrand(s, t);
            strands = new SequenceAligner(s, t, strands.getJudge());
            System.out.println("\n" + strands);
            GUI.this.repaint();
          }
        });
        add(item);
      }
    }
  }
  
  /**
   * Replaces the character at position i in s with the string t.
   */

  private static String replaceChar(String s, int i, String t) {
    return s.substring(0, i) + t + s.substring(i + 1);
  }
}

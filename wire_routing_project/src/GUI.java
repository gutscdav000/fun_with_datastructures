import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

/**
 * Features of the GUI:
 *
 * 1. Row and column numbers are displayed, in grey along the sides of
 *    the chip.
 *
 * 2. Wires are displayed in different colors to separate each
 *    other. You are encouraged to run the medium sized chips and
 *    enjoy the beautiful board displayed!
 *
 * 3. Wire ends are displayed in bold font.
 *
 * 4. User Interaction: When you rest your mouse on a cell, the tool
 *    tip text will tell you what kind of cell this is (free or
 *    obstacle). If it is along a wire, it will tell you the wire ID
 *    and the length of the wire. If it is one of the wire ends, it
 *    will also tell you if it is the start or the other end.
 *
 * 5. Finally but not least: the design considers your screen
 *    size. Running on different screens, the board and font size will
 *    change accordingly to deliver an astheticaly pleasing look.
 *
 * @author Chuck Jia, jiac@iu.edu
 */

public class GUI extends JFrame {

  static {
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      ToolTipManager.sharedInstance().setInitialDelay(0);
      ToolTipManager.sharedInstance().setDismissDelay(1000);
      UIManager.put("ToolTip.background", Constants.PATH_COLOR);
      UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder());
      UIManager.put("MenuItem.foreground", Constants.MENU_COLOR);
      UIManager.put("PopupMenu.border", BorderFactory.createEmptyBorder());
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  public GUI(Chip chip, Map<Integer, Path> layout, String title) {
    setTitle("p7: " + title);

    JLabel xLabel, yLabel;
    JLabel[][] cells;

    // Pad x and y with blanks on left to synchronize indices on the grid
    int numRows = chip.dim.height + 1;
    int numCols = chip.dim.width + 1;

    JPanel grid = new JPanel(new GridLayout(numRows, numCols, 2, 2));
    grid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

    Map<Coord, Integer> chipGrid = chip.grid;
    List<Coord> fromList = new DoublyLinkedList<>();
    List<Coord> toList =new DoublyLinkedList<>();
    for (Wire wire : chip.wires) {
      fromList.add(wire.from);
      toList.add(wire.to);
    }
    // Set up the look and feel
    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    double screenMinEdgeSize = Math.min(screenDim.getWidth(), screenDim.getHeight());
    int gridMaxEdgeSize = Math.min(numCols, numRows) + 2; 
    int textSize = (int) (screenMinEdgeSize / gridMaxEdgeSize / 2.2); // Need
    // double calculation
    int labelTextSize = (int) (screenMinEdgeSize / gridMaxEdgeSize / 2.3);

    int cellDim = (int) (screenMinEdgeSize/gridMaxEdgeSize);
    if (numCols < 10 && numRows < 10) {
      cellDim = Constants.CELL_DIM_LARGE;
      textSize = 16;
      labelTextSize = 14;
    }
    else if (numCols < 20 && numRows < 20){
      cellDim = Constants.CELL_DIM_MED;
      textSize = 16;
      labelTextSize = 14;
    }
    
    Font labelTextFont = new Font("Courier", Font.PLAIN, labelTextSize);
    Font wireTextFont = new Font("Ariel", Font.PLAIN, textSize);
    Font wireStartFont = new Font("Ariel", Font.BOLD, textSize);
    Font wireEndFont = new Font("Ariel", Font.BOLD, textSize);
    Color[] wireColors = Constants.WIRE_COLOR;
    int mod = wireColors.length;
    
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
        cell.setHorizontalAlignment(JLabel.CENTER);
        if (row == 0 || col == 0) {
          if (row >= 1 || col >= 1) {
            // label the rows with chars from x and the columns with chars from y
            cell.setFont(labelTextFont);
            cell.setForeground(Constants.LABEL_TEXT_COLOR);
            cell.setText(Integer.toString(row == 0 ? col - 1 : row - 1));
          }
        }
        else {
          Coord coord = new Coord(col - 1, row - 1);
          int wireId = chipGrid.get(coord);
          if (wireId == Constants.FREE) {
            cell.setText("");
            cell.setBackground(Constants.FREE_CELL_COLOR);
            cell.setToolTipText("Free Cell");
          }
          else if (wireId == Constants.OBSTACLE) {
            cell.setBackground(Constants.OBSTACLE_COLOR);
            cell.setToolTipText("Obstacle");
          }
          else {
            // Choose style for start and end of wires
            String pathLengthStr = "";
            Path p = layout.get(wireId);
            if (p != null)
              pathLengthStr = "Length " + Integer.toString(p.length());
            else
              pathLengthStr = "Not Connected";

            if (fromList.contains(coord)) {
              cell.setForeground(Constants.WIRE_START_TEXT_COLOR);
              cell.setFont(wireStartFont);
              cell.setText(Integer.toString(wireId));
              cell.setBackground(wireColors[wireId % mod]);
              String text = "Start ";
              if (toList.contains(coord))
                text += "and End ";
              cell.setToolTipText(text + "of Wire " + wireId + ", " +
                  pathLengthStr);
            }
            else if (toList.contains(coord)) {
              cell.setForeground(Constants.WIRE_END_TEXT_COLOR);
              cell.setFont(wireEndFont);
              cell.setText(Integer.toString(wireId));
              cell.setBackground(wireColors[wireId % mod]);
              cell.setToolTipText("End of Wire " + wireId + ", " + pathLengthStr);
            }
            else {
              cell.setFont(wireTextFont);
              cell.setText(Integer.toString(wireId));
              cell.setBackground(wireColors[wireId % mod]);
              cell.setToolTipText("Wire " + wireId + ", " + pathLengthStr);
            }
          }
        }
        grid.add(cell);
      }
    }

    grid.setPreferredSize(new Dimension(numCols * cellDim, numRows * cellDim));
    grid.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // top, left, bottom, right

    JPanel result = new JPanel();
    result.setLayout(new GridLayout(0, 1));
    result.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 0));

    xLabel = new JLabel();
    yLabel = new JLabel();
    xLabel.setFont(new Font("Courier", Font.PLAIN, 22));
    yLabel.setFont(new Font("Courier", Font.PLAIN, 22));
    result.add(xLabel);
    result.add(yLabel);

    JPanel main = new JPanel(new BorderLayout());
    main.add(grid, BorderLayout.CENTER);

    setContentPane(main);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}
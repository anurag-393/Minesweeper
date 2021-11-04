import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game {
    JFrame frame;

    static ImageIcon FLAG_ICON;

    static {
        try {
            FLAG_ICON = new  ImageIcon(new URL("https://www.google.com/logos/fnbx/minesweeper/flag_icon.png"));
            Image img = FLAG_ICON.getImage();
            Image resizedImage = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            FLAG_ICON = new ImageIcon(resizedImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // int x;
    // int y;
    Game() {   

        frame  = new JFrame();

        Level selectedLevel = Config.LEVELS[0];
        
        Board board = new Board(selectedLevel);
        
        JPanel panel = new JPanel();
        
        panel.setLayout(new GridLayout(selectedLevel.getRows(), selectedLevel.getCols()));

        Cell[][] cells = board.getCells();
        JButton[][] buttons = new JButton[selectedLevel.getRows()][selectedLevel.getCols()];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];

                JButton button = new JButton();
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(button.isEnabled()) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                cell.setFlagged(!cell.isFlagged());
                                button.setIcon(cell.isFlagged() ? FLAG_ICON : null);
                                button.setText(null);
                            } else if(SwingUtilities.isLeftMouseButton(e)){
                                Stack<Cell> neighbourCells = new Stack<>();
                                Stack<JButton> neighbourButtons = new Stack<>();

                                neighbourCells.push(cell);
                                neighbourButtons.push(button);

                                do  {
                                    Cell neighbourCell = neighbourCells.pop();
                                    JButton neighbourButton = neighbourButtons.pop();

                                    neighbourCell.setRevealed(true);
                                    neighbourButton.setEnabled(false);
                                    neighbourButton.setIcon(null);
                                    if(neighbourCell.hasBomb()) {
                                        neighbourButton.setText("B");
                                    } else if(neighbourCell.getBombsNearby() > 0) {
                                        neighbourButton.setText(String.valueOf(neighbourCell.getBombsNearby()));                                    
                                    } else {
                                        neighbourButton.setText("0");

                                        for (int xOffset = -1; xOffset <= 1; xOffset++) {
                                            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                                                int x = neighbourCell.getPosition().getGridx() + xOffset;
                                                int y = neighbourCell.getPosition().getGridy() + yOffset;

                                                if (!(xOffset == 0 && yOffset == 0) && (0 <= x && x < selectedLevel.getRows()) && (0 <= y && y < selectedLevel.getCols())) {
                                                    if(!cells[x][y].isRevealed()) {
                                                        neighbourCells.push(cells[x][y]);
                                                        neighbourButtons.push(buttons[x][y]);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } while(!neighbourCells.isEmpty());
                            }
                        }
                    }
                });

                buttons[i][j] = button;
                panel.add(button);
            }
        }
        
        frame.add(panel);
        frame.setTitle("Minesweeper"); 
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game {
    JFrame frame;
    JButton[][] buttons;
    Cell[][] cells;
    Level selectedLevel;
    
    static ImageIcon FLAG_ICON;
    static ImageIcon BOMB_ICON;

    static {
        try {
            FLAG_ICON = new  ImageIcon(new URL("https://www.google.com/logos/fnbx/minesweeper/flag_icon.png"));
            Image flagImg = FLAG_ICON.getImage();
            Image resizedFlagImage = flagImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            FLAG_ICON = new ImageIcon(resizedFlagImage);
            
            BOMB_ICON = new  ImageIcon(new URL("https://w7.pngwing.com/pngs/220/369/png-transparent-minesweeper-pro-classic-mine-sweeper-minesweeper-plus-likeminesweeper-bomb-game-computer-wallpaper-video-game.png"));
            Image BombImg = BOMB_ICON.getImage();
            Image resizedBombImage = BombImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            BOMB_ICON = new ImageIcon(resizedBombImage);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // int x;
    // int y;
    Game() {   
        frame  = new JFrame();

        selectedLevel = Config.LEVELS[0];
        Board board = new Board(selectedLevel);
        
        JPanel panel = new JPanel();
        
        panel.setLayout(new GridLayout(selectedLevel.getRows(), selectedLevel.getCols()));

        cells = board.getCells();
        
        buttons = new JButton[selectedLevel.getRows()][selectedLevel.getCols()];
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
                                        // neighbourButton.setIcon(BOMB_ICON);
                                        result(false);

                                    } else if(neighbourCell.getBombsNearby() > 0) {
                                        neighbourButton.setText(String.valueOf(neighbourCell.getBombsNearby()));
                                    } else {
                                        neighbourButton.setText("0");

                                        for(Cell nextCell: neighbourCell.getNeighbours()) {
                                            if (!nextCell.isRevealed()) {
                                                neighbourCells.push(nextCell);
                                                neighbourButtons.push(buttons[nextCell.getPosition().getGridx()][nextCell
                                                        .getPosition().getGridy()]);
                                            }
                                        }
                                    }
                                } while(!neighbourCells.isEmpty());

                                if (winCheck()) {
                                    result(true);
                                }
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

    boolean winCheck() {
        int unRevealedCellCount = 0;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if(!(cells[i][j].isRevealed())) {
                    unRevealedCellCount ++;
                }
            }
        }
        System.out.println(unRevealedCellCount);

        if (unRevealedCellCount == selectedLevel.getBombs()) {
            return true;
        }
        else {
            return false;
        }
    }

    void result (boolean result) {
        if (result) {
            
            JOptionPane.showMessageDialog(frame, "You Won");
            
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    buttons[i][j].setEnabled(false);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "You Lost");

             for (int i = 0; i < cells.length; i++) {
                 for (int j = 0; j < cells[i].length; j++) {
                    Cell cell = cells[i][j];
                     if(cell.hasBomb()) {
                         buttons[i][j].setText("B");
                    } else if(cell.getBombsNearby() > 0) {
                        buttons[i][j].setText(String.valueOf(cell.getBombsNearby()));                                    
                    
                    } else {
                        buttons[i][j].setText("0");                
                    }
                }
            }
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }
}
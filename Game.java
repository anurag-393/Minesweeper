import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.metal.MetalButtonUI;

public class Game {
    JFrame frame;
    JButton[][] buttons;
    Cell[][] cells;
    Level selectedLevel;
    JPanel boardPanel;
    JLabel flagCount;
    JPanel parentPanel;
    int flagCounter;
    JLabel timerLabel;
    Timer timer;
    Date startedTime;

    
    static ImageIcon FLAG_ICON;
    static ImageIcon BOMB_ICON;
    static ImageIcon TIMER_ICON;

    static {
        try {
            FLAG_ICON = new  ImageIcon(new URL(Config.FLAG_ICON_URL));
            Image flagImg = FLAG_ICON.getImage();
            Image resizedFlagImage = flagImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            FLAG_ICON = new ImageIcon(resizedFlagImage);
            
            BOMB_ICON = new  ImageIcon(new URL(Config.BOMB_ICON_URL));
            Image bombImg = BOMB_ICON.getImage();
            Image resizedBombImage = bombImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            BOMB_ICON = new ImageIcon(resizedBombImage);
            
            TIMER_ICON = new  ImageIcon(new URL(Config.TIMER_ICON_URL));
            Image tiemrImg = TIMER_ICON.getImage();
            Image resizedTimerImage = tiemrImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            TIMER_ICON = new ImageIcon(resizedTimerImage);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    Game() {   
        frame  = new JFrame();

        parentPanel = new JPanel();
        JPanel gameDetailsPanel = new JPanel();
        
        parentPanel.setLayout(new BorderLayout());
        gameDetailsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));
        gameDetailsPanel.setBackground(Config.GAME_DETAILS_BG_COLOR);

        
        flagCount = new JLabel();
        flagCount.setForeground(Config.GAME_DETAILS_FLAG_COUNT_COLOR);
        flagCount.setFont(Config.FONT_FOR_BUTTONS);
        
        timerLabel = new JLabel();
        timerLabel.setForeground(Config.GAME_DETAILS_FLAG_COUNT_COLOR);
        timerLabel.setFont(Config.FONT_FOR_BUTTONS);
        // timerLabel.setIcon(TIMER_ICON);
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startedTime != null) {
                    long diff = new Date().getTime() - startedTime.getTime();
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;
                    timerLabel.setText(minutes + ":" + seconds);
                }
            }
        });
        timer.setRepeats(true);

        selectedLevel = null;
        JComboBox<String> difficulty = new JComboBox<String>();

        for (Level level : Config.LEVELS) {
             difficulty.addItem(level.getName());
        }  
        difficulty.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Level previousLevel = selectedLevel;
                selectedLevel = Config.LEVELS[difficulty.getSelectedIndex()];

                if (previousLevel != selectedLevel) {
                    if(boardPanel != null) {
                        parentPanel.remove(boardPanel);
                    }
                    startedTime = null;
                    timerLabel.setText(null);
                    boardPanel =  initBoard(selectedLevel);
                    parentPanel.add(boardPanel, BorderLayout.CENTER);
                    parentPanel.revalidate();
                }
            }
        });
        difficulty.getItemListeners()[0].itemStateChanged(null);
        
        flagCount.setVerticalAlignment(JLabel.CENTER);
        flagCount.setHorizontalAlignment(JLabel.CENTER);

        gameDetailsPanel.add(difficulty);
        gameDetailsPanel.add(flagCount);
        gameDetailsPanel.add(timerLabel);
        

        parentPanel.add(gameDetailsPanel, BorderLayout.NORTH);
        
        frame.add(parentPanel);
        
        frame.setTitle("Minesweeper"); 
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }


    JPanel initBoard(Level selectedLevel) {
        
        Board board = new Board(selectedLevel);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(selectedLevel.getRows(), selectedLevel.getCols()));

        cells = board.getCells();
        flagCounter = selectedLevel.getBombs();
        flagCount.setText(String.valueOf(selectedLevel.getBombs()));
        flagCount.setIcon(FLAG_ICON);
        timerLabel.setIcon(TIMER_ICON);

        buttons = new JButton[selectedLevel.getRows()][selectedLevel.getCols()];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = cells[i][j];

                JButton button = new JButton();
                if(i % 2 == j % 2) {
                    button.setBackground(Config.BOARD_DARK_COLOR);
                } else {
                    button.setBackground(Config.BOARD_LIGHT_COLOR);
                }
                button.setUI(new MetalButtonUI() {
                    @Override
                    protected Color getDisabledTextColor() {
                        return Config.NUMBER_COLORS[cell.getBombsNearby()];
                    }
                });
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (button.isEnabled()) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                cell.setFlagged(!cell.isFlagged());
                                button.setIcon(cell.isFlagged() ? FLAG_ICON : null);
                                button.setText(null);
                                if (cell.isFlagged()) {
                                    flagCounter--;
                                    flagCount.setText(String.valueOf(flagCounter));
                                } else {
                                    flagCounter++;
                                    flagCount.setText(String.valueOf(flagCounter));
                                }
                            } else if (SwingUtilities.isLeftMouseButton(e)) {
                                if(startedTime == null) {
                                    startedTime = new Date();
                                    timer.start();
                                }

                                Stack<Cell> neighbourCells = new Stack<>();
                                Stack<JButton> neighbourButtons = new Stack<>();

                                neighbourCells.push(cell);
                                neighbourButtons.push(button);

                                do {
                                    Cell neighbourCell = neighbourCells.pop();
                                    JButton neighbourButton = neighbourButtons.pop();

                                    neighbourCell.setRevealed(true);
                                    neighbourButton.setEnabled(false);
                                    neighbourButton.setIcon(null);
                                    neighbourButton.setDisabledIcon(null);
                                    
                                    int i = neighbourCell.getPosition().getGridx();
                                    int j = neighbourCell.getPosition().getGridy();
                                    neighbourButton.setBackground(i % 2 == j % 2 ? Config.BOARD_DISABLED_DARK_COLOR : Config.BOARD_DISABLED_LIGHT_COLOR);

                                    if (neighbourCell.hasBomb()) {
                                        // neighbourButton.setText("B");
                                        neighbourButton.setIcon(BOMB_ICON);
                                        neighbourButton.setDisabledIcon(BOMB_ICON);
                                        result(false);

                                    } else if (neighbourCell.getBombsNearby() > 0) {
                                        neighbourButton.setText(String.valueOf(neighbourCell.getBombsNearby()));
                                    } else {
                                        neighbourButton.setText(null);

                                        for (Cell nextCell : neighbourCell.getNeighbours()) {
                                            if (!nextCell.isRevealed()) {
                                                neighbourCells.push(nextCell);
                                                neighbourButtons.push(buttons[nextCell.getPosition().getGridx()][nextCell.getPosition().getGridy()]);
                                            }
                                        }
                                    }
                                } while (!neighbourCells.isEmpty());

                                if (winCheck()) {
                                    result(true);
                                }
                            }
                        }
                    }
                });

                buttons[i][j] = button;
                buttons[i][j].setFont(Config.FONT_FOR_BUTTONS);
                gamePanel.add(button);
            }

        }

        return gamePanel;
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
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[i].length; j++) {
                    if (!(cells[i][j].isRevealed())) {
                        buttons[i][j].setIcon(BOMB_ICON);
                        buttons[i][j].setDisabledIcon(BOMB_ICON);
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    void result (boolean result) {
        timer.stop();
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
                         buttons[i][j].setIcon(BOMB_ICON);
                         buttons[i][j].setDisabledIcon(BOMB_ICON);
                     } else if(cell.getBombsNearby() > 0) {
                        buttons[i][j].setText(String.valueOf(cell.getBombsNearby()));
                    } else {
                        buttons[i][j].setText(null); 
                    }
                }
            }
            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setBackground(i % 2 == j % 2 ? Config.BOARD_DISABLED_DARK_COLOR : Config.BOARD_DISABLED_LIGHT_COLOR);
                }
            }
        }
    }
}
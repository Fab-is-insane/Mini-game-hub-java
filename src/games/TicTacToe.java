package games;

import utils.ScoreManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class TicTacToe extends JFrame {

    private static final String GAME_NAME = "Tic Tac Toe";
    private static final Color BG      = new Color(15, 15, 35);
    private static final Color ACCENT  = new Color(99, 179, 237);
    private static final Color SUCCESS = new Color(72, 199, 142);
    private static final Color DANGER  = new Color(252, 110, 110);
    private static final Color DRAW_C  = new Color(250, 200, 70);
    private static final Color CARD_BG = new Color(26, 32, 60);
    private static final Color TEXT    = new Color(226, 232, 240);
    private static final Color X_COLOR = new Color(99, 179, 237);
    private static final Color O_COLOR = new Color(252, 110, 110);

    private final ScoreManager sm = ScoreManager.getInstance();
    private JButton[] cells = new JButton[9];
    private char[] board = new char[9];
    private char currentPlayer = 'X';
    private boolean gameOver = false;
    private int winsX, winsO, draws;

    private JLabel statusLabel, scoreLabel, topScoreLabel;
    private JLabel winsXLbl, winsOLbl, drawsLbl;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(480, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        sm.incrementGamesPlayed(GAME_NAME);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new GridLayout(2, 2, 10, 4));
        p.setBackground(CARD_BG);
        p.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = statLabel("Tic Tac Toe");
        title.setForeground(ACCENT);
        scoreLabel    = statLabel("Score: 0");
        statusLabel   = statLabel("Player X's turn");
        topScoreLabel = statLabel("Best: 0");

        p.add(title);
        p.add(scoreLabel);
        p.add(statusLabel);
        p.add(topScoreLabel);
        return p;
    }

    private JLabel statLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }

    private JPanel buildCenter() {
        JPanel outer = new JPanel();
        outer.setBackground(BG);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBorder(new EmptyBorder(14, 30, 10, 30));

        // Stats
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statsRow.setBackground(CARD_BG);
        statsRow.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsRow.setMaximumSize(new Dimension(420, 60));
        winsXLbl = new JLabel(statHtml("X Wins", "0", X_COLOR), SwingConstants.CENTER);
        drawsLbl = new JLabel(statHtml("Draws", "0", DRAW_C), SwingConstants.CENTER);
        winsOLbl = new JLabel(statHtml("O Wins", "0", O_COLOR), SwingConstants.CENTER);
        statsRow.add(winsXLbl);
        statsRow.add(drawsLbl);
        statsRow.add(winsOLbl);

        // Board
        JPanel board = new JPanel(new GridLayout(3, 3, 6, 6));
        board.setBackground(new Color(45, 55, 72));
        board.setBorder(new EmptyBorder(6, 6, 6, 6));
        board.setMaximumSize(new Dimension(360, 360));
        board.setAlignmentX(Component.CENTER_ALIGNMENT);

        for (int i = 0; i < 9; i++) {
            cells[i] = new JButton("");
            cells[i].setFont(new Font("Segoe UI Emoji", Font.BOLD, 42));
            cells[i].setBackground(CARD_BG);
            cells[i].setForeground(TEXT);
            cells[i].setFocusPainted(false);
            cells[i].setBorderPainted(false);
            cells[i].setOpaque(true);
            cells[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final int idx = i;
            cells[i].addActionListener(e -> handleMove(idx));
            board.add(cells[i]);
        }

        outer.add(statsRow);
        outer.add(Box.createVerticalStrut(16));
        outer.add(board);
        return outer;
    }

    private String statHtml(String title, String val, Color color) {
        return "<html><center><span style='font-size:9px;color:#a0aec0'>" + title
                + "</span><br><span style='font-size:18px;font-weight:bold;color:"
                + String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue())
                + "'>" + val + "</span></center></html>";
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        p.setBackground(CARD_BG);

        JButton reset = btn("New Game", SUCCESS);
        reset.addActionListener(e -> resetBoard());

        JButton close = btn("Close", DANGER);
        close.addActionListener(e -> dispose());

        p.add(reset);
        p.add(close);
        return p;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(new Color(15, 15, 35));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        b.setOpaque(true);
        return b;
    }

    private void handleMove(int idx) {
        if (gameOver || board[idx] != '\0') return;
        board[idx] = currentPlayer;
        cells[idx].setText(currentPlayer == 'X' ? "❌" : "⭕");
        cells[idx].setForeground(currentPlayer == 'X' ? X_COLOR : O_COLOR);

        int[] winner = checkWinner();
        if (winner != null) {
            highlightWinner(winner);
            if (currentPlayer == 'X') {
                winsX++;
                sm.addScore(GAME_NAME, 20);
                statusLabel.setText("❌ Player X Wins! +20 pts");
                statusLabel.setForeground(X_COLOR);
            } else {
                winsO++;
                statusLabel.setText("⭕ Player O Wins!");
                statusLabel.setForeground(O_COLOR);
            }
            gameOver = true;
            updateStats();
        } else if (isBoardFull()) {
            draws++;
            statusLabel.setText("It's a Draw!");
            statusLabel.setForeground(DRAW_C);
            gameOver = true;
            updateStats();
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText("Player " + currentPlayer + "'s turn");
            statusLabel.setForeground(currentPlayer == 'X' ? X_COLOR : O_COLOR);
        }
    }

    private void highlightWinner(int[] line) {
        for (int i : line) {
            cells[i].setBackground(new Color(40, 70, 40));
        }
    }

    private int[] checkWinner() {
        int[][] lines = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] l : lines) {
            if (board[l[0]] != '\0' && board[l[0]] == board[l[1]] && board[l[1]] == board[l[2]])
                return l;
        }
        return null;
    }

    private boolean isBoardFull() {
        for (char c : board) if (c == '\0') return false;
        return true;
    }

    private void updateStats() {
        scoreLabel.setText("Score: " + sm.getCurrentScore(GAME_NAME));
        topScoreLabel.setText("Best: " + sm.getTopScore(GAME_NAME));
        winsXLbl.setText(statHtml("X Wins", String.valueOf(winsX), X_COLOR));
        winsOLbl.setText(statHtml("O Wins", String.valueOf(winsO), O_COLOR));
        drawsLbl.setText(statHtml("Draws", String.valueOf(draws), DRAW_C));
    }

    private void resetBoard() {
        board = new char[9];
        currentPlayer = 'X';
        gameOver = false;
        for (JButton c : cells) {
            c.setText("");
            c.setBackground(CARD_BG);
        }
        statusLabel.setText("Player X's turn");
        statusLabel.setForeground(TEXT);
        sm.incrementGamesPlayed(GAME_NAME);
    }
}

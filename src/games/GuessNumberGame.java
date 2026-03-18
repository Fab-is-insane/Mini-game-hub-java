package games;

import utils.ScoreManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;

public class GuessNumberGame extends JFrame {

    private static final String GAME_NAME = "Guess The Number";
    private static final Color BG        = new Color(15, 15, 35);
    private static final Color ACCENT    = new Color(99, 179, 237);
    private static final Color SUCCESS   = new Color(72, 199, 142);
    private static final Color DANGER    = new Color(252, 110, 110);
    private static final Color CARD_BG   = new Color(26, 32, 60);
    private static final Color TEXT      = new Color(226, 232, 240);

    private int numberToGuess;
    private int attempts;
    private int maxAttempts = 7;
    private final ScoreManager sm = ScoreManager.getInstance();

    private JLabel messageLabel, attemptsLabel, scoreLabel, topScoreLabel, hintLabel;
    private JTextField inputField;
    private JButton guessButton, newGameButton;
    private JProgressBar attemptsBar;

    public GuessNumberGame() {
        setTitle("Guess The Number");
        setSize(480, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        startNewGame();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new GridLayout(2, 2, 10, 4));
        p.setBackground(CARD_BG);
        p.setBorder(new EmptyBorder(14, 20, 14, 20));

        scoreLabel    = statLabel("Score: 0");
        topScoreLabel = statLabel("Best: 0");
        JLabel game   = statLabel("Guess The Number");
        game.setForeground(ACCENT);
        attemptsLabel = statLabel("Attempts: 0/" + maxAttempts);

        p.add(game);
        p.add(scoreLabel);
        p.add(attemptsLabel);
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
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 30, 10, 30));

        messageLabel = new JLabel("Guess a number between 1 and 100!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        messageLabel.setForeground(TEXT);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintLabel = new JLabel(" ", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hintLabel.setForeground(new Color(160, 174, 192));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        attemptsBar = new JProgressBar(0, maxAttempts);
        attemptsBar.setStringPainted(false);
        attemptsBar.setBackground(new Color(45, 55, 72));
        attemptsBar.setForeground(SUCCESS);
        attemptsBar.setMaximumSize(new Dimension(400, 10));
        attemptsBar.setBorderPainted(false);

        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(200, 42));
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBackground(CARD_BG);
        inputField.setForeground(TEXT);
        inputField.setCaretColor(ACCENT);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT, 2, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        inputField.addActionListener(e -> checkGuess());

        guessButton = styledButton("  Guess  ", ACCENT, BG);
        guessButton.addActionListener(e -> checkGuess());
        guessButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(Box.createVerticalStrut(10));
        p.add(messageLabel);
        p.add(Box.createVerticalStrut(8));
        p.add(hintLabel);
        p.add(Box.createVerticalStrut(20));
        p.add(attemptsBar);
        p.add(Box.createVerticalStrut(20));
        p.add(inputField);
        p.add(Box.createVerticalStrut(14));
        p.add(guessButton);
        return p;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        p.setBackground(CARD_BG);

        newGameButton = styledButton("New Game", SUCCESS, BG);
        newGameButton.addActionListener(e -> startNewGame());

        JButton closeBtn = styledButton("Close", DANGER, BG);
        closeBtn.addActionListener(e -> dispose());

        p.add(newGameButton);
        p.add(closeBtn);
        return p;
    }

    private JButton styledButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 24, 10, 24));
        b.setOpaque(true);
        return b;
    }

    private void startNewGame() {
        numberToGuess = new Random().nextInt(100) + 1;
        attempts = 0;
        sm.incrementGamesPlayed(GAME_NAME);
        messageLabel.setText("Guess a number between 1 and 100!");
        messageLabel.setForeground(TEXT);
        hintLabel.setText(" ");
        inputField.setText("");
        inputField.setEnabled(true);
        guessButton.setEnabled(true);
        attemptsBar.setValue(0);
        attemptsBar.setForeground(SUCCESS);
        updateStats();
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(inputField.getText().trim());
            if (guess < 1 || guess > 100) {
                hintLabel.setText("Enter a number between 1 and 100!");
                return;
            }
            attempts++;
            attemptsBar.setValue(attempts);
            int remaining = maxAttempts - attempts;

            if (guess < numberToGuess) {
                messageLabel.setText("📉 Too Low!");
                messageLabel.setForeground(new Color(250, 200, 70));
                hintLabel.setText(guess < numberToGuess - 20 ? "Way too low! Try much higher 🚀" : "A bit higher...");
            } else if (guess > numberToGuess) {
                messageLabel.setText("📈 Too High!");
                messageLabel.setForeground(new Color(250, 200, 70));
                hintLabel.setText(guess > numberToGuess + 20 ? "Way too high! Try much lower 📉" : "A bit lower...");
            } else {
                int points = Math.max(10, (maxAttempts - attempts + 1) * 15);
                sm.addScore(GAME_NAME, points);
                messageLabel.setText("Correct! +" + points + " pts");
                messageLabel.setForeground(SUCCESS);
                hintLabel.setText("You got it in " + attempts + " attempt(s)!");
                inputField.setEnabled(false);
                guessButton.setEnabled(false);
                attemptsBar.setForeground(SUCCESS);
                updateStats();
                return;
            }

            if (remaining <= 0) {
                messageLabel.setText("💀 Game Over! It was " + numberToGuess);
                messageLabel.setForeground(DANGER);
                hintLabel.setText("Better luck next time!");
                attemptsBar.setForeground(DANGER);
                inputField.setEnabled(false);
                guessButton.setEnabled(false);
            } else {
                hintLabel.setText(hintLabel.getText() + "  (" + remaining + " tries left)");
            }

            inputField.setText("");
            updateStats();
        } catch (NumberFormatException ex) {
            hintLabel.setText("⚠ Please enter a valid number!");
        }
    }

    private void updateStats() {
        scoreLabel.setText("Score: " + sm.getCurrentScore(GAME_NAME));
        topScoreLabel.setText("Best: " + sm.getTopScore(GAME_NAME));
        attemptsLabel.setText("Attempts: " + attempts + "/" + maxAttempts);
    }
}

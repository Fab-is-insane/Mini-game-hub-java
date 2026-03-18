package games;

import utils.ScoreManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TriviaGame extends JFrame {

    private static final String GAME_NAME = "Trivia Quiz";
    private static final Color BG      = new Color(15, 15, 35);
    private static final Color ACCENT  = new Color(255, 165, 80);
    private static final Color SUCCESS = new Color(72, 199, 142);
    private static final Color DANGER  = new Color(252, 110, 110);
    private static final Color CARD_BG = new Color(26, 32, 60);
    private static final Color TEXT    = new Color(226, 232, 240);

    private final ScoreManager sm = ScoreManager.getInstance();

    private static final String[][] QUESTIONS = {
        {"What is the capital of France?", "Berlin", "Madrid", "Paris", "Rome", "2"},
        {"Which planet is known as the Red Planet?", "Venus", "Mars", "Jupiter", "Saturn", "1"},
        {"How many sides does a hexagon have?", "5", "7", "8", "6", "3"},
        {"What is the chemical symbol for water?", "CO2", "H2O", "O2", "NaCl", "1"},
        {"Who painted the Mona Lisa?", "Van Gogh", "Picasso", "Da Vinci", "Rembrandt", "2"},
        {"What is 7 x 8?", "54", "56", "58", "52", "1"},
        {"Which country invented pizza?", "USA", "Greece", "Italy", "France", "2"},
        {"How many continents are there?", "5", "6", "7", "8", "2"},
        {"What gas do plants absorb?", "Oxygen", "Nitrogen", "CO2", "Hydrogen", "2"},
        {"Which ocean is the largest?", "Atlantic", "Indian", "Arctic", "Pacific", "3"},
        {"What is the boiling point of water (°C)?", "90", "95", "100", "110", "2"},
        {"Who wrote Hamlet?", "Dickens", "Shakespeare", "Tolkien", "Hemingway", "1"},
        {"What language does Brazil speak?", "Spanish", "English", "Portuguese", "French", "2"},
        {"How many bones in the adult human body?", "186", "206", "226", "246", "1"},
        {"Which animal is the fastest on land?", "Lion", "Cheetah", "Horse", "Leopard", "1"},
    };

    private List<String[]> questionPool;
    private int currentQ, score, totalQ = 10;
    private JLabel questionLabel, scoreLabel, topScoreLabel, progressLabel, feedbackLabel;
    private JButton[] optionBtns = new JButton[4];
    private JProgressBar progressBar;

    public TriviaGame() {
        setTitle("Trivia Quiz");
        setSize(540, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        startGame();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new GridLayout(2, 2, 10, 4));
        p.setBackground(CARD_BG);
        p.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = statLabel("Trivia Quiz");
        title.setForeground(ACCENT);
        scoreLabel    = statLabel("Score: 0");
        progressLabel = statLabel("Q 1 / " + totalQ);
        topScoreLabel = statLabel("Best: 0");

        p.add(title);
        p.add(scoreLabel);
        p.add(progressLabel);
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
        p.setBorder(new EmptyBorder(16, 30, 10, 30));

        progressBar = new JProgressBar(0, totalQ);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setBackground(new Color(45, 55, 72));
        progressBar.setForeground(ACCENT);
        progressBar.setMaximumSize(new Dimension(480, 8));
        progressBar.setBorderPainted(false);

        JPanel qCard = new JPanel(new BorderLayout());
        qCard.setBackground(CARD_BG);
        qCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        qCard.setMaximumSize(new Dimension(480, 110));

        questionLabel = new JLabel("<html><center>Loading...</center></html>", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        questionLabel.setForeground(TEXT);
        qCard.add(questionLabel, BorderLayout.CENTER);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        feedbackLabel.setForeground(SUCCESS);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel optPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optPanel.setBackground(BG);
        optPanel.setMaximumSize(new Dimension(480, 180));

        String[] labels = {"A", "B", "C", "D"};
        Color[] colors  = {new Color(99,179,237), new Color(154,230,180),
                           new Color(252,176,64),  new Color(252,110,110)};
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            optionBtns[i] = new JButton(labels[i] + ". ...");
            optionBtns[i].setFont(new Font("Segoe UI", Font.BOLD, 13));
            optionBtns[i].setBackground(colors[i]);
            optionBtns[i].setForeground(new Color(15,15,35));
            optionBtns[i].setFocusPainted(false);
            optionBtns[i].setBorderPainted(false);
            optionBtns[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            optionBtns[i].setBorder(new EmptyBorder(12, 10, 12, 10));
            optionBtns[i].setOpaque(true);
            optionBtns[i].addActionListener(e -> checkAnswer(idx));
            optPanel.add(optionBtns[i]);
        }

        p.add(progressBar);
        p.add(Box.createVerticalStrut(14));
        p.add(qCard);
        p.add(Box.createVerticalStrut(10));
        p.add(feedbackLabel);
        p.add(Box.createVerticalStrut(10));
        p.add(optPanel);
        return p;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        p.setBackground(CARD_BG);

        JButton replay = btn("Play Again", SUCCESS);
        replay.addActionListener(e -> startGame());

        JButton close = btn("Close", DANGER);
        close.addActionListener(e -> dispose());

        p.add(replay);
        p.add(close);
        return p;
    }

    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(new Color(15,15,35));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        b.setOpaque(true);
        return b;
    }

    private void startGame() {
        List<String[]> all = new ArrayList<>(Arrays.asList(QUESTIONS));
        Collections.shuffle(all);
        questionPool = all.subList(0, Math.min(totalQ, all.size()));
        currentQ = 0;
        score = 0;
        sm.resetCurrentScore(GAME_NAME);
        sm.incrementGamesPlayed(GAME_NAME);
        progressBar.setValue(0);
        feedbackLabel.setText(" ");
        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQ >= questionPool.size()) { showResult(); return; }
        String[] q = questionPool.get(currentQ);
        questionLabel.setText("<html><center>" + q[0] + "</center></html>");
        for (int i = 0; i < 4; i++) {
            String[] labels = {"A","B","C","D"};
            optionBtns[i].setText(labels[i] + ". " + q[i + 1]);
            optionBtns[i].setEnabled(true);
        }
        progressLabel.setText("Q " + (currentQ + 1) + " / " + totalQ);
        progressBar.setValue(currentQ);
        feedbackLabel.setText(" ");
    }

    private void checkAnswer(int selected) {
        String[] q = questionPool.get(currentQ);
        int correct = Integer.parseInt(q[5]);

        for (JButton b : optionBtns) b.setEnabled(false);

        if (selected == correct) {
            score += 10;
            sm.addScore(GAME_NAME, 10);
            feedbackLabel.setText("✅ Correct! +10 pts");
            feedbackLabel.setForeground(SUCCESS);
            optionBtns[selected].setBackground(SUCCESS);
        } else {
            feedbackLabel.setText("Wrong! Correct: " + new String[]{"A","B","C","D"}[correct]);
            feedbackLabel.setForeground(DANGER);
            optionBtns[selected].setBackground(DANGER);
            optionBtns[correct].setBackground(SUCCESS);
        }

        scoreLabel.setText("Score: " + sm.getCurrentScore(GAME_NAME));
        topScoreLabel.setText("Best: " + sm.getTopScore(GAME_NAME));
        currentQ++;
        progressBar.setValue(currentQ);

        javax.swing.Timer t = new javax.swing.Timer(1200, e -> loadQuestion());
        t.setRepeats(false);
        t.start();
    }

    private void showResult() {
        String grade = score >= 80 ? "🏆 Excellent!" : score >= 50 ? "👍 Good Job!" : "📚 Keep Practicing!";
        questionLabel.setText("<html><center>" + grade + "<br>You scored <b>" + score + " / " + (totalQ * 10) + "</b>!</center></html>");
        feedbackLabel.setText("Top Score: " + sm.getTopScore(GAME_NAME));
        feedbackLabel.setForeground(ACCENT);
        for (JButton b : optionBtns) b.setEnabled(false);
    }
}

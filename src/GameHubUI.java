import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import games.*;
import utils.ScoreManager;

public class GameHubUI extends JFrame {

    private static final Color BG      = new Color(15, 15, 35);
    private static final Color CARD_BG = new Color(26, 32, 60);
    private static final Color TEXT    = new Color(226, 232, 240);
    private static final Color SUBTEXT = new Color(160, 174, 192);

    private final ScoreManager sm = ScoreManager.getInstance();
    private JLabel totalScoreLabel;

    public GameHubUI() {
        setTitle("🎮 Mini Game Hub");
        setSize(520, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildGames(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 30, 20, 30));

        JLabel icon = new JLabel("🎮", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Mini Game Hub", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(99, 179, 237));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Choose a game and start playing!", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(SUBTEXT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalScoreLabel = new JLabel("Total Score: 0", SwingConstants.CENTER);
        totalScoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalScoreLabel.setForeground(new Color(250, 200, 70));
        totalScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(icon);
        p.add(Box.createVerticalStrut(6));
        p.add(title);
        p.add(Box.createVerticalStrut(4));
        p.add(sub);
        p.add(Box.createVerticalStrut(8));
        p.add(totalScoreLabel);
        return p;
    }

    private JPanel buildGames() {
        JPanel p = new JPanel(new GridLayout(4, 1, 0, 12));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(20, 30, 20, 30));

        p.add(gameCard("🔢", "Guess The Number",
                "Guess the secret number in 7 tries!",
                new Color(99, 179, 237), () -> openGame(new GuessNumberGame())));

        p.add(gameCard("✂", "Rock Paper Scissors",
                "Challenge the computer in this classic game!",
                new Color(180, 120, 255), () -> openGame(new RockPaperScissors())));

        p.add(gameCard("❌", "Tic Tac Toe",
                "Two players take turns — X vs O!",
                new Color(72, 199, 142), () -> openGame(new TicTacToe())));

        p.add(gameCard("🧠", "Trivia Quiz",
                "10 questions — how many can you answer?",
                new Color(255, 165, 80), () -> openGame(new TriviaGame())));

        return p;
    }

    private JPanel gameCard(String icon, String name, String desc, Color color, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(14, 0));
        card.setBackground(CARD_BG);
        card.setBorder(new EmptyBorder(14, 18, 14, 18));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));
        iconLbl.setForeground(color);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(CARD_BG);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLbl.setForeground(TEXT);

        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLbl.setForeground(SUBTEXT);

        textPanel.add(nameLbl);
        textPanel.add(descLbl);

        JButton playBtn = new JButton("Play");
        playBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        playBtn.setBackground(color);
        playBtn.setForeground(new Color(15, 15, 35));
        playBtn.setFocusPainted(false);
        playBtn.setBorderPainted(false);
        playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playBtn.setBorder(new EmptyBorder(8, 16, 8, 16));
        playBtn.setOpaque(true);
        playBtn.addActionListener(e -> { action.run(); refreshTotalScore(); });

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(35, 45, 80)); textPanel.setBackground(new Color(35, 45, 80)); }
            public void mouseExited(MouseEvent e)  { card.setBackground(CARD_BG); textPanel.setBackground(CARD_BG); }
            public void mouseClicked(MouseEvent e) { action.run(); refreshTotalScore(); }
        });

        card.add(iconLbl, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(playBtn, BorderLayout.EAST);
        return card;
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        p.setBackground(CARD_BG);

        JButton exitBtn = new JButton("Exit Game");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exitBtn.setBackground(new Color(252, 110, 110));
        exitBtn.setForeground(new Color(15, 15, 35));
        exitBtn.setFocusPainted(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitBtn.setBorder(new EmptyBorder(10, 30, 10, 30));
        exitBtn.setOpaque(true);
        exitBtn.addActionListener(e -> System.exit(0));

        p.add(exitBtn);
        return p;
    }

    private void openGame(JFrame game) {
        game.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) { refreshTotalScore(); }
        });
    }

    private void refreshTotalScore() {
        totalScoreLabel.setText("Total Score: " + sm.getTotalScore());
    }
}

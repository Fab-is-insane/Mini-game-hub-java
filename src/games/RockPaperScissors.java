package games;

import utils.ScoreManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;

public class RockPaperScissors extends JFrame {

    private static final String GAME_NAME = "Rock Paper Scissors";
    private static final Color BG      = new Color(15, 15, 35);
    private static final Color ACCENT  = new Color(180, 120, 255);
    private static final Color SUCCESS = new Color(72, 199, 142);
    private static final Color DANGER  = new Color(252, 110, 110);
    private static final Color DRAW    = new Color(250, 200, 70);
    private static final Color CARD_BG = new Color(26, 32, 60);
    private static final Color TEXT    = new Color(226, 232, 240);

    private final ScoreManager sm = ScoreManager.getInstance();
    private int wins, losses, draws;

    private JLabel resultLabel, detailLabel, scoreLabel, topScoreLabel;
    private JLabel playerChoiceLabel, cpuChoiceLabel;
    private JLabel winsLbl, lossesLbl, drawsLbl;

    public RockPaperScissors() {
        setTitle("Rock Paper Scissors");
        setSize(520, 560);
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
        JPanel p = new JPanel(new GridLayout(1, 3, 10, 0));
        p.setBackground(CARD_BG);
        p.setBorder(new EmptyBorder(14, 20, 14, 20));

        scoreLabel    = statLabel("Score: 0");
        topScoreLabel = statLabel("Best: 0");
        JLabel title  = statLabel("Rock Paper Scissors");
        title.setForeground(ACCENT);

        p.add(title);
        p.add(scoreLabel);
        p.add(topScoreLabel);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(16, 30, 10, 30));

        // Choices display
        JPanel choicePanel = new JPanel(new GridLayout(1, 3, 10, 0));
        choicePanel.setBackground(BG);
        choicePanel.setMaximumSize(new Dimension(420, 100));

        playerChoiceLabel = bigEmoji("❓");
        JLabel vsLabel    = bigEmoji("VS");
        vsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        vsLabel.setForeground(new Color(160, 174, 192));
        cpuChoiceLabel = bigEmoji("❓");

        JLabel youLbl = subLabel("YOU");
        JLabel cpuLbl = subLabel("CPU");
        JPanel pLeft  = labeledEmoji(playerChoiceLabel, youLbl);
        JPanel pRight = labeledEmoji(cpuChoiceLabel, cpuLbl);
        JPanel pVs    = new JPanel(new BorderLayout()); pVs.setBackground(BG);
        pVs.add(vsLabel, BorderLayout.CENTER);

        choicePanel.add(pLeft);
        choicePanel.add(pVs);
        choicePanel.add(pRight);

        resultLabel = new JLabel("Make your move!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        resultLabel.setForeground(TEXT);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailLabel = new JLabel(" ", SwingConstants.CENTER);
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailLabel.setForeground(new Color(160, 174, 192));
        detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        statsRow.setBackground(CARD_BG);
        statsRow.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsRow.setMaximumSize(new Dimension(420, 60));
        winsLbl   = statCard("Wins", "0", SUCCESS);
        drawsLbl  = statCard("Draws", "0", DRAW);
        lossesLbl = statCard("Losses", "0", DANGER);
        statsRow.add(winsLbl);
        statsRow.add(drawsLbl);
        statsRow.add(lossesLbl);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setBackground(BG);
        btnRow.setMaximumSize(new Dimension(420, 70));
        String[] choices = {"Rock", "Paper", "Scissors"};
        Color[]  colors  = {new Color(99, 179, 237), new Color(154, 230, 180), new Color(252, 110, 110)};
        for (int i = 0; i < choices.length; i++) {
            final String c = choices[i];
            JButton b = bigBtn(c, colors[i]);
            b.addActionListener(e -> play(c));
            btnRow.add(b);
        }

        p.add(choicePanel);
        p.add(Box.createVerticalStrut(14));
        p.add(resultLabel);
        p.add(Box.createVerticalStrut(6));
        p.add(detailLabel);
        p.add(Box.createVerticalStrut(14));
        p.add(statsRow);
        p.add(Box.createVerticalStrut(14));
        p.add(btnRow);
        return p;
    }

    private JPanel labeledEmoji(JLabel emoji, JLabel sub) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);
        p.add(emoji, BorderLayout.CENTER);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    private JLabel bigEmoji(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        l.setForeground(TEXT);
        return l;
    }

    private JLabel subLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(new Color(160, 174, 192));
        return l;
    }

    private JLabel statCard(String title, String val, Color color) {
        JLabel l = new JLabel("<html><center><span style='font-size:9px;color:#a0aec0'>"
                + title + "</span><br><span style='font-size:18px;font-weight:bold;color:"
                + toHex(color) + "'>" + val + "</span></center></html>", SwingConstants.CENTER);
        return l;
    }

    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private JLabel statLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }

    private JButton bigBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI Emoji", Font.BOLD, 15));
        b.setBackground(bg);
        b.setForeground(new Color(15, 15, 35));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 18, 10, 18));
        b.setOpaque(true);
        return b;
    }

    private JPanel buildBottom() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        p.setBackground(CARD_BG);
        JButton reset = bigBtn("🔄 Reset", ACCENT);
        reset.setForeground(TEXT);
        reset.addActionListener(e -> resetStats());
        JButton close = bigBtn("Close", DANGER);
        close.setForeground(TEXT);
        close.addActionListener(e -> dispose());
        p.add(reset);
        p.add(close);
        return p;
    }

    private void play(String playerChoice) {
        String[] options = {"Rock", "Paper", "Scissors"};
        String cpu = options[new Random().nextInt(3)];

        playerChoiceLabel.setText(emoji(playerChoice));
        cpuChoiceLabel.setText(emoji(cpu));

        String outcome = getOutcome(playerChoice, cpu);
        detailLabel.setText(playerChoice + " vs " + cpu);

        if (outcome.equals("WIN")) {
            wins++;
            int pts = 10;
            sm.addScore(GAME_NAME, pts);
            resultLabel.setText("You Win! +" + pts + " pts");
            resultLabel.setForeground(SUCCESS);
        } else if (outcome.equals("LOSE")) {
            losses++;
            resultLabel.setText("You Lose!");
            resultLabel.setForeground(DANGER);
        } else {
            draws++;
            resultLabel.setText("Draw!");
            resultLabel.setForeground(DRAW);
        }

        winsLbl.setText(statCardHtml("Wins", String.valueOf(wins), SUCCESS));
        drawsLbl.setText(statCardHtml("Draws", String.valueOf(draws), DRAW));
        lossesLbl.setText(statCardHtml("Losses", String.valueOf(losses), DANGER));
        scoreLabel.setText("Score: " + sm.getCurrentScore(GAME_NAME));
        topScoreLabel.setText("Best: " + sm.getTopScore(GAME_NAME));
    }

    private String statCardHtml(String title, String val, Color color) {
        return "<html><center><span style='font-size:9px;color:#a0aec0'>" + title
                + "</span><br><span style='font-size:18px;font-weight:bold;color:"
                + toHex(color) + "'>" + val + "</span></center></html>";
    }

    private String getOutcome(String player, String cpu) {
        if (player.equals(cpu)) return "DRAW";
        if ((player.equals("Rock") && cpu.equals("Scissors")) ||
            (player.equals("Paper") && cpu.equals("Rock")) ||
            (player.equals("Scissors") && cpu.equals("Paper"))) return "WIN";
        return "LOSE";
    }

    private String emoji(String choice) {
        return switch (choice) {
            case "Rock"     -> "✊";
            case "Paper"    -> "📄";
            case "Scissors" -> "✂";
            default -> "❓";
        };
    }

    private void resetStats() {
        wins = losses = draws = 0;
        sm.resetCurrentScore(GAME_NAME);
        playerChoiceLabel.setText("❓");
        cpuChoiceLabel.setText("❓");
        resultLabel.setText("Make your move!");
        resultLabel.setForeground(TEXT);
        detailLabel.setText(" ");
        winsLbl.setText(statCardHtml("Wins", "0", SUCCESS));
        drawsLbl.setText(statCardHtml("Draws", "0", DRAW));
        lossesLbl.setText(statCardHtml("Losses", "0", DANGER));
        scoreLabel.setText("Score: 0");
        topScoreLabel.setText("Best: " + sm.getTopScore(GAME_NAME));
    }
}

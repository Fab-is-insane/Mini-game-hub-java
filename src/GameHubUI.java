import javax.swing.*;
import java.awt.*;
import games.*;

public class GameHubUI extends JFrame {

    public GameHubUI() {
        setTitle("Mini Game Hub");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🎮 Mini Game Hub", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JButton guessBtn = new JButton("Guess The Number");
        JButton rpsBtn = new JButton("Rock Paper Scissors");
        JButton tttBtn = new JButton("Tic Tac Toe");
        JButton exitBtn = new JButton("Exit");

        guessBtn.addActionListener(e -> new GuessNumberGame());
        rpsBtn.addActionListener(e -> new RockPaperScissors());
        tttBtn.addActionListener(e -> new TicTacToe());
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(guessBtn);
        panel.add(rpsBtn);
        panel.add(tttBtn);
        panel.add(exitBtn);

        add(panel);
        setVisible(true);
    }
}
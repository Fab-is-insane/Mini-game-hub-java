package games;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GuessNumberGame extends JFrame {

    private int numberToGuess;
    private JLabel messageLabel;
    private JTextField inputField;

    public GuessNumberGame() {
        setTitle("Guess The Number");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        numberToGuess = new Random().nextInt(100) + 1;

        messageLabel = new JLabel("Guess a number (1-100): ");
        inputField = new JTextField(10);
        JButton guessButton = new JButton("Guess");

        guessButton.addActionListener(e -> checkGuess());

        add(messageLabel);
        add(inputField);
        add(guessButton);

        setVisible(true);
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(inputField.getText());

            if (guess < numberToGuess) {
                messageLabel.setText("Too Low! Try again.");
            } else if (guess > numberToGuess) {
                messageLabel.setText("Too High! Try again.");
            } else {
                messageLabel.setText("🎉 Correct!");
            }

        } catch (Exception e) {
            messageLabel.setText("Enter a valid number.");
        }
    }
}
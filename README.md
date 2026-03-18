# 🎮 Mini Game Hub

A Java Swing-based mini game hub with 4 fully functional games.

## 🕹️ Games Included

| Game | Description | Scoring |
|------|-------------|---------|
| 🔢 Guess The Number | Guess a secret number (1–100) in 7 tries | More points for fewer attempts |
| ✂ Rock Paper Scissors | Beat the computer! | +10 pts per win |
| ❌ Tic Tac Toe | Classic 2-player game (X vs O) | +20 pts for Player X win |
| 🧠 Trivia Quiz | 10 random questions from a question bank | +10 pts per correct answer |

## ✨ Features
- 🎨 Modern dark-themed UI
- 🏆 Score & Top Score tracking per game
- 📊 Stats panel (wins, losses, draws)
- 🔄 New Game / Reset buttons
- 📈 Progress bars and visual feedback

## 🚀 How to Run

### From the `src` folder:
```bash
cd src
javac -d ../out utils/ScoreManager.java games/*.java GameHubUI.java Main.java
cd ../out
java Main
```

### Or from an IDE (IntelliJ / Eclipse):
- Import as a Java project
- Set `src` as the source root
- Run `Main.java`

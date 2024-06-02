// FILE: RockPaperScissorsLizardSpockGUI.java
// Purpose: GUI-Based Version
// Created By: Sehr Abrar

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Comparator;

public class RockPaperScissorsLizardSpockGUI extends JFrame implements ActionListener {
    // define the shapes and winning moves
    // the moves on the left are superior to the moves on the right

    private final List<String> WINNING_MOVES = new ArrayList<>(Arrays.asList(
            "scissors:paper,lizard",
            "paper:rock,Spock",
            "rock:lizard,scissors",
            "lizard:Spock,paper",
            "Spock:scissors,rock"
    ));

    // probabilities for getComputerMove()
    private final int ROCK_PROBABILITY = 25;
    private final int PAPER_PROBABILITY = 20;
    private final int SCISSORS_PROBABILITY = 15;
    private final int LIZARD_PROBABILITY = 20;

    // list to store player objects for displayLeaderboard
    private List<Player> players = new ArrayList<>();

    // date formatting for logging results, player name's label and input field, play, rule, and leaderboard buttons
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final JLabel playerNameLabel;
    private final JTextField playerNameField;
    private final JButton rulesButton;
    private final JButton playButton;
    private final JButton leadButton;
    private final JTextArea logTextArea;

    public RockPaperScissorsLizardSpockGUI() {
        // create the game's user interface
        // base pop up
        setTitle("Rock Paper Scissors Lizard Spock!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        
        // player's name (field), and play, rules, and leaderboard button
        playerNameLabel = new JLabel("Enter your name:");
        playerNameField = new JTextField(20);
        rulesButton = new JButton("Read Rules");
        playButton = new JButton("Play Game");
        leadButton = new JButton("Leaderboard");
        logTextArea = new JTextArea(10, 30);

        logTextArea.setEditable(false);

        // set the layout manager of the container
        setLayout(new BorderLayout());

        // the container that holds the following elements
        JPanel inputPanel = new JPanel();
        inputPanel.add(playerNameLabel);
        inputPanel.add(playerNameField);
        inputPanel.add(playButton);
        inputPanel.add(rulesButton);
        inputPanel.add(leadButton);

        // add the input panel to the main container at the top position
        add(inputPanel, BorderLayout.NORTH);

        // add a scrollable area for log text to the main container
        add(new JScrollPane(logTextArea), BorderLayout.CENTER);

        // attach action listeners to buttons 
        leadButton.addActionListener(this);
        rulesButton.addActionListener(this);
        playButton.addActionListener(this);
    }

    // begin executing the program here
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RockPaperScissorsLizardSpockGUI gui = new RockPaperScissorsLizardSpockGUI();
            gui.setVisible(true); // make the GUI window visible
        });
    }

    // determine the user's actions based on what button they click
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rulesButton) {
            showRules();
        } else if (e.getSource() == playButton) {
            playGame();
        } else if (e.getSource() == leadButton) {
            displayLeaderboard();
        }
    }

    // the player begins to play the game
    private void playGame() {
        // get the player's name from the input field and trim any whitespace
        String playerName = playerNameField.getText().trim();

        // find an existing player or create a new one based on their given name
        Player player = findOrCreatePlayer(playerName);

        // if the player name is empty, prompt the user with a message to enter their name
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.");
            return;
        }

        // loop until a clear winner is determined
        while (true) {
            // get the player's move and computer's move
            String playerMove = getPlayerMove(playerName);
            String computerMove = getComputerMove();

            // determine the initial winner based on the player's and computer's moves
            String initialWinner = determineWinner(playerMove, computerMove);

            // write the game result with the following information in this format
            String gameResult = String.format("Player: %s (%s) vs Computer (%s), Winner: %s\n",
                    playerName, playerMove, computerMove, initialWinner);

            // if there is a tie, log the tie result and display it
            if (initialWinner.equals("Tie")) {
                JOptionPane.showMessageDialog(this, "It's a tie, let's do a tiebreaker round!");
                logGameResult(gameResult);
                logTextArea.append(gameResult);
            } else {
                // if the initial winner is the player, increment the player's score
                if (initialWinner.equals("Player")) {
                    player.incrementScore();
                }

                // log the game's result
                logGameResult(gameResult);
                logTextArea.append(gameResult);

                // exit the loop once a clear winner is determined
                break;
            }
        }
    }

    private String getComputerMove() {
        // instead of simply using random, we decided to use probabilities to enhance the computer's move
        Random random = new Random();
        int randomNumber = random.nextInt(100); // generate a random number from 0 to 99

        // determine the computer's move based on probabilities
        if (randomNumber < ROCK_PROBABILITY) {
            return "rock";
        } else if (randomNumber < ROCK_PROBABILITY + PAPER_PROBABILITY) {
            return "paper";
        } else if (randomNumber < ROCK_PROBABILITY + PAPER_PROBABILITY + SCISSORS_PROBABILITY) {
            return "scissors";
        } else if (randomNumber < ROCK_PROBABILITY + PAPER_PROBABILITY + SCISSORS_PROBABILITY + LIZARD_PROBABILITY) {
            return "lizard";
        } else {
            return "Spock";
        }
    }

    // get the player's move 
    private String getPlayerMove(String playerName) {   
        // create new buttons
        JButton rockButton = new JButton();
        JButton paperButton = new JButton();
        JButton scissorsButton = new JButton();
        JButton lizardButton = new JButton();
        JButton spockButton = new JButton();
        
        // scale the buttons to preferred size
        int buttonWidth = 200;
        int buttonHeight = 200;
        rockButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        paperButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        scissorsButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        lizardButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        spockButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        
        // no borders on the buttons
        rockButton.setBorderPainted(false);
        paperButton.setBorderPainted(false);
        scissorsButton.setBorderPainted(false);
        lizardButton.setBorderPainted(false);
        spockButton.setBorderPainted(false);
        
        // attach each icon with its correct image
        ImageIcon rockIcon = new ImageIcon("images/rock.png");
        ImageIcon paperIcon = new ImageIcon("images/paper.png");
        ImageIcon scissorsIcon = new ImageIcon("images/scissors.png");
        ImageIcon lizardIcon = new ImageIcon("images/lizard.png");
        ImageIcon spockIcon = new ImageIcon("images/spock.png");
        
        // scale the icon images down correctly
        rockIcon = new ImageIcon(rockIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT));
        paperIcon = new ImageIcon(paperIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT));
        scissorsIcon = new ImageIcon(scissorsIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT));
        lizardIcon = new ImageIcon(lizardIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT));
        spockIcon = new ImageIcon(spockIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_DEFAULT));
        
        // set the icons together with the buttons
        rockButton.setIcon(rockIcon);
        paperButton.setIcon(paperIcon);
        scissorsButton.setIcon(scissorsIcon);
        lizardButton.setIcon(lizardIcon);
        spockButton.setIcon(spockIcon);
        
        // array to track button clicks
        boolean[] buttonClicked = {false, false, false, false, false}; 

        // if a button is clicked on, then the button clicked is true
        // then close the prompt
        rockButton.addActionListener(e -> {
            buttonClicked[0] = true;
            closeDialog(rockButton);
        });
        paperButton.addActionListener(e -> {
            buttonClicked[1] = true;
            closeDialog(paperButton);
        });
        scissorsButton.addActionListener(e -> {
            buttonClicked[2] = true;
            closeDialog(scissorsButton);
        });
        lizardButton.addActionListener(e -> {
            buttonClicked[3] = true;
            closeDialog(lizardButton);
        });
        spockButton.addActionListener(e -> {
            buttonClicked[4] = true;
            closeDialog(spockButton);
        });
        
        // place the buttons on the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        buttonPanel.add(lizardButton);
        buttonPanel.add(spockButton);
    
        @SuppressWarnings("unused") // kept getting a warning on int choice, remove later
        int choice = JOptionPane.showOptionDialog(this, buttonPanel, playerName + ", choose your move!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        
        // 
        String[] moves = {"rock", "paper", "scissors", "lizard", "Spock"};
        for (int i = 0; i < moves.length; i++) {
            if (buttonClicked[i]) {
                return moves[i];
            }
        }
        return ""; // returns an empty string if no button was clicked
    }
    
    // close the prompt
    private void closeDialog(JButton button) {
        Window window = SwingUtilities.getWindowAncestor(button);
        if (window instanceof JDialog) {
            ((JDialog) window).dispose();
        }
    }
    
    // determine the winner based on the player's and computer's moves
    private String determineWinner(String playerMove, String computerMove) {
        // if both moves are the same, it's a tie
        if (playerMove.equals(computerMove)) {
            return "Tie";
        } else {
            // check if the player's move wins
            for (String moveCombo : WINNING_MOVES) {
                // split the move combination
                String[] parts = moveCombo.split(":");
                // if the player's move contains the computer's moves, then the player wins
                if (parts[0].equals(playerMove) && parts[1].contains(computerMove)) {
                    return "Player";
                }
            }
            return "Computer"; // otherwise the computer wins
        }
    }
    
    // log the game result with the date and time
    private void logGameResult(String gameResult) {
        String dateTime = LocalDateTime.now().format(formatter); // get the current date and time
        String result = dateTime + " | " + gameResult; // combine dat/time with the game result

        // write the result into the log file
        try (FileWriter writer = new FileWriter("game_log.txt", true)) {
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace(); // traces where the error occured, then print the error message
            JOptionPane.showMessageDialog(this, "Error writing to log file: " + e.getMessage());
        }
    }

    // the game's rules according to the prompt
    private void showRules() {
        ImageIcon rulesIcon = new ImageIcon("images/rules.png"); // import the rules image
        int iconWidth = 900;
        int iconHeight = 600;
        rulesIcon = new ImageIcon(rulesIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_DEFAULT)); // scale it to size
        JOptionPane.showMessageDialog(this, rulesIcon, "Game Rules", JOptionPane.PLAIN_MESSAGE);
    }

    // find the existing player or create a new one based on the player's name
    private Player findOrCreatePlayer(String playerName) {
        for (Player player : players) {
            // check if the player already exists
            if (player.getName().equals(playerName)) {
                return player; // return the existing player
            }
        }
        // if the player already exists, create a new player with their given name
        Player newPlayer = new Player(playerName);
        players.add(newPlayer);
        return newPlayer; // return the new player
    }

    // display the leaderboard based on the player's names and scores
    private void displayLeaderboard() {
        StringBuilder leaderboard = new StringBuilder();

        // filter players with scores greater than 0
        List<Player> filteredPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getScore() > 0) {
                filteredPlayers.add(player);
            }
        }

        // check if there are players with non-zero scores
        if (filteredPlayers.isEmpty()) {
            leaderboard.append("Waiting for your first win!");
        } else {
            // sort the filtered players by score (descending)
            filteredPlayers.sort(Comparator.comparingInt(Player::getScore).reversed());

            // iterate through the sorted list to update the leaderboard display
            for (int i = 0; i < filteredPlayers.size(); i++) {
                Player player = filteredPlayers.get(i);
                leaderboard.append(i + 1).append(". ").append(player.getName()).append(": ").append(player.getScore()).append(" points\n");
            }
        }

        // show the updated leaderboard
        JOptionPane.showMessageDialog(this, leaderboard.toString(), "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    } 
}
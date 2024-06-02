// FILE: RockPaperScissorsLizardSpock.java
// Purpose: Console-Based Version
// Created By: Sehr Abrar

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RockPaperScissorsLizardSpock {
    // define the shapes and the winning moves according to the game's rules
    private static final String[] SHAPES = {"rock", "paper", "scissors", "lizard", "Spock"};
    private static final Map<String, List<String>> WINNING_MOVES = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // the choice on the left are superior to the choices on the right
    static {
        WINNING_MOVES.put("scissors", Arrays.asList("paper", "lizard"));
        WINNING_MOVES.put("paper", Arrays.asList("rock", "Spock"));
        WINNING_MOVES.put("rock", Arrays.asList("lizard", "scissors"));
        WINNING_MOVES.put("lizard", Arrays.asList("Spock", "paper"));
        WINNING_MOVES.put("Spock", Arrays.asList("scissors", "rock"));
    }

    // start the game by showing the menu
    public static void main(String[] args) {
        showMenu(); 
    }

    private static void showMenu() {
        // display the game menu options
        System.out.println("Welcome to Rock, Paper, Scissors, Lizard, Spock!");
        System.out.println("Menu:");
        System.out.println("R - Read the rules");
        System.out.println("P - Play the game");
        System.out.print("Enter your choice (R/P): ");
        String choice = scanner.nextLine();
        System.out.println();

        switch (choice) {
            case "R":
                showRules(); // show the game's rules
                break;
            case "P":
                playGame(); // play the game
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                showMenu(); // if invalid choice, re-display the menu
        }
    }

    // the game's rules according to the prompt
    private static void showRules() {
        System.out.println();
        System.out.println("Rules:");
        System.out.println("Scissors cut paper,");
        System.out.println("Paper covers rock,");
        System.out.println("Rock crushes lizard,");
        System.out.println("Lizard poisons Spock,");
        System.out.println("Spock smashes scissors,");
        System.out.println("Scissors decapitate lizard,");
        System.out.println("Lizard eats paper,");
        System.out.println("Paper disproves Spock,");
        System.out.println("Spock vaporizes rock,");
        System.out.println("And as it has always been, rock crushes scissors.\n");
        showMenu();
    }

    private static void playGame() {
        // the player begins by entering their name
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine(); // read player's name

        String playerMove = getPlayerMove(playerName); // get player's move

        // the computer selects their option from the choices randomly
        Random random = new Random();
        String computerMove = SHAPES[random.nextInt(SHAPES.length)]; // generate computer's random choice
        String winner = determineWinner(playerMove, computerMove); // determine the winner of the round

        // if the game is a tie
        if (winner.equals("Tie")) {
            System.out.println("It's a tie! Let's have a tiebreaker round.");
            // get the player's and computer's tiebreaker move by playing another game
            String playerMoveTiebreaker = getPlayerMove(playerName + " (Tiebreaker)");
            String computerMoveTiebreaker = SHAPES[random.nextInt(SHAPES.length)];
            winner = determineWinner(playerMoveTiebreaker, computerMoveTiebreaker);
            // log the tiebreaker's game results
            logGameResult(playerName, playerMoveTiebreaker, "Computer", computerMoveTiebreaker, winner);
        } else {
            // log the game's results with the player's name, player's move, computer, computer's move, and the winner
            logGameResult(playerName, playerMove, "Computer", computerMove, winner);
        }
        
        System.out.println(playerName + " (" + playerMove + ") vs Computer (" + computerMove + ")");
        System.out.println("Winner: " + winner);
        System.out.println();
        showMenu();
    }

    // get the player's move ny reading from input
    // note: capitalization doesn't matter, as long as what the user types is a valid choice
    private static String getPlayerMove(String playerName) {
        String move;
        do {
            System.out.print(playerName + ", enter your move (rock, paper, scissors, lizard, Spock): ");
            move = scanner.nextLine();
        } while (!Arrays.asList(SHAPES).contains(move));
        return move;
    }    

    // determine the winner of the game
    private static String determineWinner(String playerMove, String computerMove) {
        String winner;
        // if the winner's move equals the computer's move, it's a tie
        if (playerMove.equals(computerMove)) {
            winner = "Tie";
        } else if (WINNING_MOVES.get(playerMove).contains(computerMove)) {
            winner = "Player"; // if the winner's move is superior to the computer's move, then player wins
        } else {
            winner = "Computer"; // otherwise, the computer wins
        }
        return winner;
    }

    // log the game's result into a text file with the following data:
    // player's name, player's move, computer's move, and winner
    private static void logGameResult(String playerName, String playerMove, String computerName,
                                      String computerMove, String winner) {
        // utilize the date formatting library
        String dateTime = LocalDateTime.now().format(formatter);
        // format each game as the following
        String result = dateTime + " | Player: " + playerName + " (" + playerMove + ")" +
                " vs " +
                "Computer: " + "(" + computerMove + ")" +
                ", Winner: " + winner + "\n";
        // add the game into the text file as a new line, and write th result
        try (FileWriter writer = new FileWriter("game_log.txt", true)) {
            writer.write(result);
        } catch (IOException e) {
            // in case printing the results doesn't work
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
// FILE: Player.java
// Purpose: Helper File
// Created By: Sehr Abrar

public class Player {
    private String name; // stores the player's name
    private int score; // stores the player's score

    // initiaize a player object with the given name and a score set to 0
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    // retrive the player's name
    public String getName() {
        return name;
    }

    // retrieve the player's score
    public int getScore() {
        return score;
    }

    // increase the player's score by 1
    public void incrementScore() {
        score++;
    }
}
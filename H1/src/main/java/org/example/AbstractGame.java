package org.example;

abstract class AbstractGame {
    public abstract void setPlayers(String Player1, String Player2);
    public abstract boolean gameOver();
    public abstract boolean move(int now_location, int target_location);
}

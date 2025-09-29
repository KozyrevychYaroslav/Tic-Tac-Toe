package org.example.tictactoe.exception;

public class GameAlreadyFinishedException extends RuntimeException {
    public GameAlreadyFinishedException(String message) {
        super(message);
    }
}
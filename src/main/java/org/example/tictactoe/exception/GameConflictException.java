package org.example.tictactoe.exception;

public class GameConflictException extends RuntimeException {
    public GameConflictException(String message) {
        super(message);
    }
}
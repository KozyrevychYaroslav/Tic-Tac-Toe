package org.example.tictactoe.exception;

public class NotPlayerTurnException extends RuntimeException {
    public NotPlayerTurnException(String message) {
        super(message);
    }
}
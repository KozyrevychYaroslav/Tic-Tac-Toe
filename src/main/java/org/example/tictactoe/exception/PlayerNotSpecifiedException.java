package org.example.tictactoe.exception;

public class PlayerNotSpecifiedException extends RuntimeException {
    public PlayerNotSpecifiedException(String message) {
        super(message);
    }
}
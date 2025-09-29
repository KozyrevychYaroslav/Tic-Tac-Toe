package org.example.tictactoe.exception;

public class PositionOutOfBoundsException extends RuntimeException {
    public PositionOutOfBoundsException(String message) {
        super(message);
    }
}
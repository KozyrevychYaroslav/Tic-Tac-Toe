package org.example.tictactoe.exception;

public class CellNotEmptyException extends RuntimeException {
    public CellNotEmptyException(String message) {
        super(message);
    }
}
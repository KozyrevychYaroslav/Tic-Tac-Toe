package org.example.tictactoe.dto;

import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;

public record TicTacToeGameDto(Long id, String boardState, GameStatus status, Player nextPlayer, Player winner) {
}
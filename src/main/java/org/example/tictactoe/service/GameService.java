package org.example.tictactoe.service;

import org.example.tictactoe.dto.MoveRequestDto;
import org.example.tictactoe.dto.TicTacToeGameDto;

import java.util.List;

public interface GameService {
    TicTacToeGameDto createGame();

    TicTacToeGameDto getGame(Long id);

    TicTacToeGameDto makeMove(Long gameId, MoveRequestDto moveRequestDto);

    List<TicTacToeGameDto> getAllGames();
}

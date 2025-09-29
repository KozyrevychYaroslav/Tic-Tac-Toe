package org.example.tictactoe.controller;

import lombok.RequiredArgsConstructor;
import org.example.tictactoe.dto.MoveRequestDto;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class TicTacToeController {

    private final GameService gameService;


    @PostMapping
    public ResponseEntity<TicTacToeGameDto> createTicTacToeGame() {
        TicTacToeGameDto gameDto = gameService.createGame();
        return ResponseEntity.created(URI.create("/api/v1/games/" + gameDto.id())).body(gameDto);
    }

    //moveRequest.position =
    //0 | 1 | 2
    //3 | 4 | 5
    //6 | 7 | 8
    @PostMapping("/{gameId}/moves")
    public ResponseEntity<TicTacToeGameDto> makeMove(@PathVariable Long gameId, @RequestBody MoveRequestDto moveRequest) {
        return ResponseEntity.ok(gameService.makeMove(gameId, moveRequest));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<TicTacToeGameDto> getTicTacToeGame(@PathVariable Long gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @GetMapping
    public ResponseEntity<List<TicTacToeGameDto>> getAllTicTacToeGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }
}

package org.example.tictactoe.unit;

import org.example.tictactoe.controller.TicTacToeController;
import org.example.tictactoe.dto.MoveRequestDto;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;
import org.example.tictactoe.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicTacToeController.class)
class TicTacToeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;


    @Test
    void givenNoGame_whenCreateGame_thenReturnsCreatedWithLocation() throws Exception {
        TicTacToeGameDto dto = new TicTacToeGameDto(1L, " ".repeat(9), GameStatus.IN_PROGRESS, Player.X, null);
        when(gameService.createGame()).thenReturn(dto);

        mockMvc.perform(post("/api/v1/games"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/games/1"));
    }

    @Test
    void givenGameId_whenGetGame_thenReturnsOk() throws Exception {
        TicTacToeGameDto dto = new TicTacToeGameDto(2L, "X        ", GameStatus.IN_PROGRESS, Player.O, null);
        when(gameService.getGame(2L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/games/2"))
                .andExpect(status().isOk());
    }

    @Test
    void givenGameIdAndMove_whenMakeMove_thenReturnsOk() throws Exception {
        TicTacToeGameDto dto = new TicTacToeGameDto(3L, "X        ", GameStatus.IN_PROGRESS, Player.O, null);
        when(gameService.makeMove(3L, new MoveRequestDto(Player.X, 0))).thenReturn(dto);

        String requestBody = """
                {
                  "player": "X",
                  "position": 0
                }
                """;

        mockMvc.perform(post("/api/v1/games/3/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void givenGamesExist_whenGetAllGames_thenReturnsOk() throws Exception {
        when(gameService.getAllGames()).thenReturn(List.of(
                new TicTacToeGameDto(1L, "         ", GameStatus.IN_PROGRESS, Player.X, null)
        ));

        mockMvc.perform(get("/api/v1/games"))
                .andExpect(status().isOk());
    }
}
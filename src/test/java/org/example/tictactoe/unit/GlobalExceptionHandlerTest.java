package org.example.tictactoe.unit;

import org.example.tictactoe.controller.TicTacToeController;
import org.example.tictactoe.exception.GameConflictException;
import org.example.tictactoe.exception.GameNotFoundException;
import org.example.tictactoe.exception.PositionOutOfBoundsException;
import org.example.tictactoe.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicTacToeController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;


    @Test
    void whenGameNotFound_thenReturns404() throws Exception {
        when(gameService.getGame(1L)).thenThrow(new GameNotFoundException("Game is not found"));

        mockMvc.perform(get("/api/v1/games/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Game is not found"))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenInvalidMove_thenReturns400() throws Exception {
        when(gameService.makeMove(eq(1L), any()))
                .thenThrow(new PositionOutOfBoundsException("Position is invalid"));

        String body = """
                {"player": "X", "position": 9}
                """;

        mockMvc.perform(post("/api/v1/games/1/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Position is invalid"))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenGameConflict_thenReturns409() throws Exception {
        when(gameService.makeMove(eq(1L), any()))
                .thenThrow(new GameConflictException("You cannot make multiple moves at the same time. Please try again"));

        String body = """
                { "player": "X", "position": 0 }
                """;

        mockMvc.perform(post("/api/v1/games/1/moves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("You cannot make multiple moves at the " +
                        "same time. Please try again"))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenUnhandledException_thenReturns500() throws Exception {
        when(gameService.getGame(1L)).thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/api/v1/games/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected error: boom"))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
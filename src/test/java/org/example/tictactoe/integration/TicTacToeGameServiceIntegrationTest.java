package org.example.tictactoe.integration;

import org.example.tictactoe.dto.MoveRequestDto;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.entity.TicTacToeGameEntity;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;
import org.example.tictactoe.repository.TicTacToeGameRepository;
import org.example.tictactoe.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicTacToeGameServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("tictactoe")
            .withUsername("tictactoe")
            .withPassword("tictactoe");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private GameService gameService;

    @Autowired
    private TicTacToeGameRepository repository;


    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Test
    void givenNoGame_whenCreateGame_thenGameIsPersistedAndReturned() {
        TicTacToeGameDto dto = gameService.createGame();

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isNotNull();
        assertThat(dto.boardState()).isEqualTo(" ".repeat(9));
        assertThat(dto.status()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(dto.nextPlayer()).isEqualTo(Player.X);

        assertThat(repository.existsById(dto.id())).isTrue();
    }

    @Test
    void givenGame_whenMakeMove_thenPersistedAndReturnedUpdatedState() {
        TicTacToeGameDto created = gameService.createGame();

        TicTacToeGameDto after = gameService.makeMove(created.id(), new MoveRequestDto(Player.X, 0));

        assertThat(after.boardState()).isEqualTo("X        ");
        assertThat(after.nextPlayer()).isEqualTo(Player.O);

        TicTacToeGameEntity entity = repository.findById(after.id()).orElseThrow();
        assertThat(entity.getBoardState()).isEqualTo("X        ");
        assertThat(entity.getNextPlayer()).isEqualTo(Player.O);
        assertThat(entity.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void givenCreatedGame_whenGetGameAndGetAll_thenReturnSavedGame() {
        TicTacToeGameDto created = gameService.createGame();

        TicTacToeGameDto fetched = gameService.getGame(created.id());
        assertThat(fetched).isNotNull();
        assertThat(fetched.id()).isEqualTo(created.id());

        List<TicTacToeGameDto> all = gameService.getAllGames();
        assertThat(all).isNotEmpty();
        assertThat(all.stream().map(TicTacToeGameDto::id)).contains(created.id());
    }

    @Test
    void givenMoves_whenXWins_thenStatusXWon() {
        TicTacToeGameDto game = gameService.createGame();
        gameService.makeMove(game.id(), new MoveRequestDto(Player.X, 0));
        gameService.makeMove(game.id(), new MoveRequestDto(Player.O, 3));
        gameService.makeMove(game.id(), new MoveRequestDto(Player.X, 1));
        gameService.makeMove(game.id(), new MoveRequestDto(Player.O, 4));
        TicTacToeGameDto result = gameService.makeMove(game.id(), new MoveRequestDto(Player.X, 2));

        assertThat(result.status()).isEqualTo(GameStatus.X_WON);
        assertThat(result.winner()).isEqualTo(Player.X);
    }
}

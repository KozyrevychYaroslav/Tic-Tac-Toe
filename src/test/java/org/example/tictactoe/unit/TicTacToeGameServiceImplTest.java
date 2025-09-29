package org.example.tictactoe.unit;

import org.example.tictactoe.domain.TicTacToeGameDomain;
import org.example.tictactoe.dto.MoveRequestDto;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.entity.TicTacToeGameEntity;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;
import org.example.tictactoe.exception.GameConflictException;
import org.example.tictactoe.exception.GameNotFoundException;
import org.example.tictactoe.mapper.TicTacToeGameMapper;
import org.example.tictactoe.repository.TicTacToeGameRepository;
import org.example.tictactoe.service.impl.TicTacToeGameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicTacToeGameServiceImplTest {

    private TicTacToeGameRepository repository;
    private TicTacToeGameMapper mapper;
    private TicTacToeGameServiceImpl service;


    @BeforeEach
    void setup() {
        repository = mock(TicTacToeGameRepository.class);
        mapper = mock(TicTacToeGameMapper.class);
        service = new TicTacToeGameServiceImpl(repository, mapper);
    }

    @Test
    void givenGame_whenMakeMove_thenRepositorySaveIsCalled() {
        TicTacToeGameEntity entity = new TicTacToeGameEntity();

        when(repository.findById(2L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(any())).thenReturn(new TicTacToeGameDomain());
        when(mapper.toDto(any())).thenReturn(new TicTacToeGameDto(
                entity.getId(), "X        ", GameStatus.IN_PROGRESS, Player.O, null
        ));
        service.makeMove(2L, new MoveRequestDto(Player.X, 0));

        verify(repository).save(entity);
    }

    @Test
    void givenOptimisticLockingFailure_whenMakeMove_thenThrowGameConflictException() {
        TicTacToeGameEntity entity = new TicTacToeGameEntity();

        when(repository.findById(2L)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new TicTacToeGameDomain());
        when(repository.save(any())).thenThrow(new org.springframework.dao.OptimisticLockingFailureException("conflict"));

        assertThatThrownBy(() -> service.makeMove(2L, new MoveRequestDto(Player.X, 0)))
                .isInstanceOf(GameConflictException.class);
    }

    @Test
    void givenNothing_whenCreateGame_thenEntitySavedAndReturned() {
        TicTacToeGameEntity entity = new TicTacToeGameEntity();
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(new TicTacToeGameDto(1L, " ".repeat(9), GameStatus.IN_PROGRESS, Player.X, null));

        TicTacToeGameDto dto = service.createGame();

        assertThat(dto).isNotNull();
        verify(repository).save(any());
    }


    @Test
    void givenMissingGame_whenGetGame_thenThrowNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getGame(99L))
                .isInstanceOf(GameNotFoundException.class);
    }
}
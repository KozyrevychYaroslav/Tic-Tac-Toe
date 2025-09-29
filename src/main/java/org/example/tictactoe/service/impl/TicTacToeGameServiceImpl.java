package org.example.tictactoe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tictactoe.domain.TicTacToeGameDomain;
import org.example.tictactoe.dto.MoveRequestDto;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.entity.TicTacToeGameEntity;
import org.example.tictactoe.exception.GameConflictException;
import org.example.tictactoe.exception.GameNotFoundException;
import org.example.tictactoe.mapper.TicTacToeGameMapper;
import org.example.tictactoe.repository.TicTacToeGameRepository;
import org.example.tictactoe.service.GameService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicTacToeGameServiceImpl implements GameService {

    private final TicTacToeGameRepository ticTacToeGameRepository;
    private final TicTacToeGameMapper ticTacToeGameMapper;


    @Override
    @Transactional
    public TicTacToeGameDto createGame() {
        TicTacToeGameEntity ticTacToeGameEntity = new TicTacToeGameEntity();
        ticTacToeGameEntity = ticTacToeGameRepository.save(ticTacToeGameEntity);
        TicTacToeGameDto dto = ticTacToeGameMapper.toDto(ticTacToeGameEntity);

        log.info("Tic Tac Toe game is created: id={}", dto.id());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public TicTacToeGameDto getGame(Long id) {
        TicTacToeGameDto ticTacToeGameDto = ticTacToeGameRepository.findById(id)
                .map(ticTacToeGameMapper::toDto)
                .orElseThrow(() -> new GameNotFoundException("Game with id " + id + " is not found"));

        log.debug("Fetched Tic Tac Toe game: id={}, status={}", ticTacToeGameDto.id(), ticTacToeGameDto.status());
        return ticTacToeGameDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicTacToeGameDto> getAllGames() {
        List<TicTacToeGameDto> ticTacToeGameDtos = ticTacToeGameRepository.findAll()
                .stream()
                .map(ticTacToeGameMapper::toDto)
                .toList();

        log.debug("Fetched all Tic Tac Toe games, count={}", ticTacToeGameDtos.size());
        return ticTacToeGameDtos;
    }

    @Override
    @Transactional
    public TicTacToeGameDto makeMove(Long gameId, MoveRequestDto moveRequest) {
        try {
            log.info("Applying move for gameId={} player={} position={}", gameId, moveRequest.player(), moveRequest.position());
            TicTacToeGameEntity ticTacToeGameEntity = ticTacToeGameRepository.findById(gameId)
                    .orElseThrow(() -> new GameNotFoundException("Game is not found: " + gameId));
            log.debug("Current board: {}", ticTacToeGameEntity.getBoardState());
            TicTacToeGameDomain ticTacToeGameDomain = ticTacToeGameMapper.toDomain(ticTacToeGameEntity);

            ticTacToeGameDomain.applyMove(moveRequest.player(), moveRequest.position());
            ticTacToeGameMapper.toEntity(ticTacToeGameDomain, ticTacToeGameEntity);

            TicTacToeGameDto dto = ticTacToeGameMapper.toDto(ticTacToeGameRepository.save(ticTacToeGameEntity));
            log.info("Move is applied: gameId={}, player={}, position={}, board={}",
                    gameId, moveRequest.player(), moveRequest.position(), dto.boardState());
            return dto;
        } catch (OptimisticLockingFailureException ex) {
            log.warn("Optimistic locking conflict: gameId={}, player={}", gameId, moveRequest.player());
            throw new GameConflictException("You cannot make multiple moves at the same time. Please try again");
        }
    }
}

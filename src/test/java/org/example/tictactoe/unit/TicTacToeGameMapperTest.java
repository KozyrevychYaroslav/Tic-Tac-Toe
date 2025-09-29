package org.example.tictactoe.unit;

import org.example.tictactoe.domain.TicTacToeGameDomain;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.entity.TicTacToeGameEntity;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;
import org.example.tictactoe.mapper.TicTacToeGameMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TicTacToeGameMapperTest {

    private final TicTacToeGameMapper mapper = Mappers.getMapper(TicTacToeGameMapper.class);


    @Test
    void givenEntity_whenToDomain_thenFieldsMapped() {
        TicTacToeGameEntity entity = new TicTacToeGameEntity();
        entity.setId(10L);
        entity.setBoardState("X OX     ");
        entity.setStatus(GameStatus.IN_PROGRESS);
        entity.setNextPlayer(Player.O);
        entity.setWinner(null);

        TicTacToeGameDomain domain = mapper.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getBoardState()).isEqualTo(entity.getBoardState());
        assertThat(domain.getStatus()).isEqualTo(entity.getStatus());
        assertThat(domain.getNextPlayer()).isEqualTo(entity.getNextPlayer());
        assertThat(domain.getWinner()).isEqualTo(entity.getWinner());
    }

    @Test
    void givenEntity_whenToDto_thenFieldsMapped() {
        TicTacToeGameEntity entity = new TicTacToeGameEntity();
        entity.setId(20L);
        entity.setBoardState("         ");
        entity.setStatus(GameStatus.IN_PROGRESS);
        entity.setNextPlayer(Player.X);
        entity.setWinner(null);

        TicTacToeGameDto dto = mapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.boardState()).isEqualTo(entity.getBoardState());
        assertThat(dto.status()).isEqualTo(entity.getStatus());
        assertThat(dto.nextPlayer()).isEqualTo(entity.getNextPlayer());
        assertThat(dto.winner()).isEqualTo(entity.getWinner());
    }

    @Test
    void givenDomainAndEntity_whenToEntity_thenEntityUpdated() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain("X        ", GameStatus.IN_PROGRESS, Player.O, null);
        TicTacToeGameEntity entity = new TicTacToeGameEntity();
        entity.setId(1L);

        mapper.toEntity(domain, entity);

        assertThat(entity.getBoardState()).isEqualTo(domain.getBoardState());
        assertThat(entity.getStatus()).isEqualTo(domain.getStatus());
        assertThat(entity.getNextPlayer()).isEqualTo(domain.getNextPlayer());
        assertThat(entity.getId()).isNotNull();
    }
}
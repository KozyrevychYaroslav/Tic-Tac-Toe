package org.example.tictactoe.mapper;

import org.example.tictactoe.domain.TicTacToeGameDomain;
import org.example.tictactoe.dto.TicTacToeGameDto;
import org.example.tictactoe.entity.TicTacToeGameEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicTacToeGameMapper {
    TicTacToeGameDto toDto(TicTacToeGameEntity game);

    TicTacToeGameDomain toDomain(TicTacToeGameEntity entity);

    void toEntity(TicTacToeGameDomain domain, @MappingTarget TicTacToeGameEntity entity);
}


package org.example.tictactoe.repository;

import org.example.tictactoe.entity.TicTacToeGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicTacToeGameRepository extends JpaRepository<TicTacToeGameEntity, Long> {
}
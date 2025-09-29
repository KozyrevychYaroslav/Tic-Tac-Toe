package org.example.tictactoe.unit;

import org.example.tictactoe.domain.TicTacToeGameDomain;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;
import org.example.tictactoe.exception.CellNotEmptyException;
import org.example.tictactoe.exception.GameAlreadyFinishedException;
import org.example.tictactoe.exception.NotPlayerTurnException;
import org.example.tictactoe.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeGameDomainTest {

    @Test
    void givenInitialState_whenApplyValidMove_thenBoardUpdatedAndNextPlayerChanged() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain();
        domain.applyMove(Player.X, 0);

        assertEquals("X        ", domain.getBoardState());
        assertEquals(Player.O, domain.getNextPlayer());
        assertEquals(GameStatus.IN_PROGRESS, domain.getStatus());
        assertNull(domain.getWinner());
    }

    @Test
    void givenNotPlayerTurn_whenApplyMove_thenThrowNotPlayerTurnException() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain();
        domain.setNextPlayer(Player.O);

        assertThrows(NotPlayerTurnException.class, () -> domain.applyMove(Player.X, 0));
    }

    @Test
    void givenWinningLine_whenApplyMove_thenStatusXWon() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain("XX     OO", GameStatus.IN_PROGRESS, Player.X, null);
        domain.applyMove(Player.X, 2);

        assertEquals(GameStatus.X_WON, domain.getStatus());
        assertEquals(Player.X, domain.getWinner());
    }

    @Test
    void givenBoardOneEmptyCell_whenLastMoveCreatesDraw_thenStatusDraw() {
        String beforeLast = "XOXXXOOX ";
        TicTacToeGameDomain domain = new TicTacToeGameDomain(beforeLast, GameStatus.IN_PROGRESS, Player.O, null);
        domain.applyMove(Player.O, 8);

        assertEquals(GameStatus.DRAW, domain.getStatus());
        assertNull(domain.getWinner());
        assertFalse(domain.getBoardState().contains(" "));
    }

    @Test
    void givenPositionOutOfBounds_whenApplyMove_thenThrowPositionOutOfBoundsException() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain();
        assertThrows(PositionOutOfBoundsException.class, () -> domain.applyMove(Player.X, 9));
        assertThrows(PositionOutOfBoundsException.class, () -> domain.applyMove(Player.X, -1));
    }

    @Test
    void givenCellNotEmpty_whenApplyMove_thenThrowCellNotEmptyException() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain();
        domain.applyMove(Player.X, 4);
        assertThrows(CellNotEmptyException.class, () -> domain.applyMove(Player.O, 4));
    }

    @Test
    void givenGameAlreadyFinished_whenApplyMove_thenThrowGameAlreadyFinishedException() {
        TicTacToeGameDomain domain = new TicTacToeGameDomain();
        domain.setStatus(GameStatus.X_WON);
        assertThrows(GameAlreadyFinishedException.class, () -> domain.applyMove(Player.O, 0));
    }
}

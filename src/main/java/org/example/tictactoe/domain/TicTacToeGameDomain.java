package org.example.tictactoe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;
import org.example.tictactoe.exception.*;

import static org.example.tictactoe.utils.WinningLinesUtils.WINNING_LINES;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class TicTacToeGameDomain {

    private String boardState = " ".repeat(9);
    private GameStatus status = GameStatus.IN_PROGRESS;
    private Player nextPlayer = Player.X;
    private Player winner;


    public void applyMove(Player player, int position) {
        validateMove(player, position);

        char symbol = (player == Player.X) ? 'X' : 'O';
        StringBuilder sb = new StringBuilder(boardState);
        sb.setCharAt(position, symbol);
        boardState = sb.toString();

        Player foundWinner = detectWinner();
        if (foundWinner != null) {
            winner = foundWinner;
            status = (winner == Player.X) ? GameStatus.X_WON : GameStatus.O_WON;
            log.debug("Game is finished: winner={} board={}", winner, boardState);
        } else if (isBoardFull()) {
            status = GameStatus.DRAW;
            log.debug("Game is finished: draw board={}", boardState);
        } else {
            nextPlayer = (player == Player.X) ? Player.O : Player.X;
        }
    }

    private void validateMove(Player player, int position) {
        if (status != GameStatus.IN_PROGRESS) throw new GameAlreadyFinishedException("Game already finished");
        if (player == null) throw new PlayerNotSpecifiedException("Player must be specified");
        if (nextPlayer != player) throw new NotPlayerTurnException("It's not your turn");
        if (position < 0 || position > 8) throw new PositionOutOfBoundsException("Position must be between 0 and 8");
        if (!isCellEmpty(position)) throw new CellNotEmptyException("Cell is not empty");
    }

    private boolean isCellEmpty(int position) {
        return boardState.charAt(position) == ' ';
    }

    private boolean isBoardFull() {
        return !boardState.contains(" ");
    }

    private Player detectWinner() {
        if (boardState.chars().filter(c -> c != ' ').count() < 5) return null;

        for (int[] line : WINNING_LINES) {
            char first = boardState.charAt(line[0]);
            if (first == ' ') continue;
            char second = boardState.charAt(line[1]);
            char third = boardState.charAt(line[2]);
            if (first == second && second == third) {
                return first == 'X' ? Player.X : Player.O;
            }
        }
        return null;
    }
}
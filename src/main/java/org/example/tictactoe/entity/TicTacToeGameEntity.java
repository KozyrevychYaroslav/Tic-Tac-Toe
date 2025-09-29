package org.example.tictactoe.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.tictactoe.enums.GameStatus;
import org.example.tictactoe.enums.Player;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "games")
public class TicTacToeGameEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 9, nullable = false, name = "board_state")
    private String boardState = " ".repeat(9); // In total, we have 9 cells, by default they are empty

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status = GameStatus.IN_PROGRESS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "next_player")
    private Player nextPlayer = Player.X;

    @Column
    private Player winner;

    @Version
    private Long version;
}

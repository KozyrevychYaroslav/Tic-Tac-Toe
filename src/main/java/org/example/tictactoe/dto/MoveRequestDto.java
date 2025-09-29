package org.example.tictactoe.dto;

import org.example.tictactoe.enums.Player;

public record MoveRequestDto(Player player, int position) {
}
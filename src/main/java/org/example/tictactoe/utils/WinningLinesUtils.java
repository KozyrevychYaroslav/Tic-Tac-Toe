package org.example.tictactoe.utils;

public final class WinningLinesUtils {
    // each array = possible win sequence of X or O
    public static final int[][] WINNING_LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private WinningLinesUtils() {
    }
}
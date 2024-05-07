package chess;

import java.util.Collection;

public class KingMovesCalculator {

    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();

        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 1, col - 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 1, col - 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 1, col))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 1, col), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row - 1, col + 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row - 1, col + 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row, col - 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row, col - 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row, col + 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row, col + 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 1, col - 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 1, col - 1), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 1, col))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 1, col), validMoves);
        }
        if (MovesCalculator.isValidMove(board, position, new ChessPosition(row + 1, col + 1))) {
            MovesCalculator.addMove(position, new ChessPosition(row + 1, col + 1), validMoves);
        }
    }
}

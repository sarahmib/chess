package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.upLeftDiagonal;
import static chess.PieceMovesCalculator.downLeftDiagonal;
import static chess.PieceMovesCalculator.upRightDiagonal;
import static chess.PieceMovesCalculator.downRightDiagonal;


public class BishopMovesCalculator {
    ChessBoard board;
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        upLeftDiagonal(board, position, validMoves);
        downLeftDiagonal(board, position, validMoves);
        upRightDiagonal(board, position, validMoves);
        downRightDiagonal(board, position, validMoves);

        return validMoves;
    }
}

package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.*;

public class RookMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        straightUp(board, position, validMoves);
        straightDown(board, position, validMoves);
        straightLeft(board, position, validMoves);
        straightRight(board, position, validMoves);

        return validMoves;
    }
}

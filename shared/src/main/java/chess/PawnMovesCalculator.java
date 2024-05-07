package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.addMovePawn;

public class PawnMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (checkValidMove(board, position, new ChessPosition(row + 1, col - 1), ChessGame.TeamColor.WHITE, true)) {
                if (row + 1 == 8) {
                    addAllPromotion(validMoves, position, new ChessPosition(row + 1, col - 1));
                } else {
                    addMovePawn(validMoves, position, row + 1, col - 1);
                }
            }
            if (checkValidMove(board, position, new ChessPosition(row + 1, col), ChessGame.TeamColor.WHITE, false)) {
                if (row + 1 == 8) {
                    addAllPromotion(validMoves, position, new ChessPosition(row + 1, col));
                } else {
                    addMovePawn(validMoves, position, row + 1, col);
                }
                if (row == 2 && checkValidMove(board, position, new ChessPosition(row + 2, col), ChessGame.TeamColor.WHITE, false)) {
                    addMovePawn(validMoves, position, row + 2, col);
                }
            }
            if (checkValidMove(board, position, new ChessPosition(row + 1, col + 1), ChessGame.TeamColor.WHITE, true)) {
                if (row + 1 == 8) {
                    addAllPromotion(validMoves, position, new ChessPosition(row + 1, col + 1));
                } else {
                    addMovePawn(validMoves, position, row + 1, col + 1);
                }
            }
        } else {
            if (checkValidMove(board, position, new ChessPosition(row -1, col - 1), ChessGame.TeamColor.BLACK, true)) {
                if (row - 1 == 1) {
                    addAllPromotion(validMoves, position, new ChessPosition(row - 1, col - 1));
                } else {
                    addMovePawn(validMoves, position, row - 1, col - 1);
                }
            }
            if (checkValidMove(board, position, new ChessPosition(row - 1, col), ChessGame.TeamColor.BLACK, false)) {
                if (row - 1 == 1) {
                    addAllPromotion(validMoves, position, new ChessPosition(row - 1, col));
                } else {
                    addMovePawn(validMoves, position, row - 1, col);
                }
                if (row == 7 && checkValidMove(board, position, new ChessPosition(row - 2, col), ChessGame.TeamColor.BLACK, false)) {
                    addMovePawn(validMoves, position, row - 2, col);
                }
            }
            if (checkValidMove(board, position, new ChessPosition(row - 1, col + 1), ChessGame.TeamColor.BLACK, true)) {
                if (row - 1 == 1) {
                    addAllPromotion(validMoves, position, new ChessPosition(row - 1, col + 1));
                } else {
                    addMovePawn(validMoves, position, row - 1, col + 1);
                }
            }
        }

        return validMoves;
    }

    private static boolean checkValidMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, ChessGame.TeamColor pieceColor, boolean diagonalMove) {
        if (endPosition.getColumn() < 1 || endPosition.getColumn() > 8 || endPosition.getRow() < 1 || endPosition.getRow() > 8) {
            return false;
        }
        if (!diagonalMove && board.getPiece(endPosition) != null) {
            return false;
        }
        if (diagonalMove) {
            if (board.getPiece(endPosition) == null || board.getPiece(endPosition).getTeamColor() == pieceColor) {
                return false;
            }
            return true;
        }
        return true;
    }

    private static void addAllPromotion(Collection<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
    }
}

package chess;

import java.util.Collection;

public class PawnMovesCalculator {

    public static void pieceMoves(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (row == 2 && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                if (isValidMovePawn(board, position, new ChessPosition(row + 2, col), false)) {
                    MovesCalculator.addMove(position, new ChessPosition(row + 2, col), validMoves);
                }
            }

            if (isValidMovePawn(board, position, new ChessPosition(row + 1, col - 1), true)) {
                if (row + 1 == 8) {
                    addAllPromotion(position, new ChessPosition(row + 1, col - 1), validMoves);
                } else {
                    MovesCalculator.addMove(position, new ChessPosition(row + 1, col - 1), validMoves);
                }
            }

            if (isValidMovePawn(board, position, new ChessPosition(row + 1, col), false)) {
                if (row + 1 == 8) {
                    addAllPromotion(position, new ChessPosition(row + 1, col), validMoves);
                } else {
                    MovesCalculator.addMove(position, new ChessPosition(row + 1, col), validMoves);
                }
            }

            if (isValidMovePawn(board, position, new ChessPosition(row + 1, col + 1), true)) {
                if (row + 1 == 8) {
                    addAllPromotion(position, new ChessPosition(row + 1, col + 1), validMoves);
                } else {
                    MovesCalculator.addMove(position, new ChessPosition(row + 1, col + 1), validMoves);
                }
            }
        } else {
            if (row == 7 && board.getPiece(new ChessPosition(row - 1, col)) == null) {
                if (isValidMovePawn(board, position, new ChessPosition(row - 2, col), false)) {
                    MovesCalculator.addMove(position, new ChessPosition(row - 2, col), validMoves);
                }
            }

            if (isValidMovePawn(board, position, new ChessPosition(row - 1, col - 1), true)) {
                if (row - 1 == 1) {
                    addAllPromotion(position, new ChessPosition(row - 1, col - 1), validMoves);
                } else {
                    MovesCalculator.addMove(position, new ChessPosition(row - 1, col - 1), validMoves);
                }
            }

            if (isValidMovePawn(board, position, new ChessPosition(row - 1, col), false)) {
                if (row - 1 == 1) {
                    addAllPromotion(position, new ChessPosition(row - 1, col), validMoves);
                } else {
                    MovesCalculator.addMove(position, new ChessPosition(row - 1, col), validMoves);
                }
            }

            if (isValidMovePawn(board, position, new ChessPosition(row - 1, col + 1), true)) {
                if (row - 1 == 1) {
                    addAllPromotion(position, new ChessPosition(row - 1, col + 1), validMoves);
                } else {
                    MovesCalculator.addMove(position, new ChessPosition(row - 1, col + 1), validMoves);
                }
            }
        }
    }

    private static boolean isValidMovePawn(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, boolean isDiagonalMove) {
        if (endPosition.getColumn() > 8 || endPosition.getColumn() < 1 || endPosition.getRow() > 8 || endPosition.getRow() < 1) {
            return false;
        }
        if (isDiagonalMove) {
            return board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor();
        }
        return board.getPiece(endPosition) == null;
    }

    private static void addAllPromotion(ChessPosition startPosition, ChessPosition endPosition, Collection<ChessMove> validMoves) {
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
    }
}

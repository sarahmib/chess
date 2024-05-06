package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    Object[][] board;

    public ChessBoard() {
        board = new Object[8][8];
        for (int i = 0; i < 8; i++) {
            Object[] newRow = new Object[8];
            for (int j = 0; j < 8; j++) {
                newRow[j] = null;
            }
            board[i] = newRow;
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() -1;
        int column = position.getColumn() -1;

        board[row][column] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;

        if (board[row][column] != "") {
            return (ChessPiece) board[row][column];
        }

        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        setWhite();
        setBlack();
    }

    private void setWhite() {
        ChessGame.TeamColor team = ChessGame.TeamColor.WHITE;
        setRook(team, 1, 1);
        setKnight(team, 1, 2);
        setBishop(team, 1, 3);
        setQueen(team, 1, 4);
        setKing(team, 1, 5);
        setBishop(team, 1, 6);
        setKnight(team, 1, 7);
        setRook(team, 1, 8);

        for (int i=1; i <9; i++) {
            setPawn(team, 2, i);
        }
    }

    private void setBlack() {
        ChessGame.TeamColor team = ChessGame.TeamColor.BLACK;

        for (int i=1; i < 9; i++) {
            setPawn(team, 7, i);
        }

        setRook(team, 8, 1);
        setKnight(team, 8, 2);
        setBishop(team, 8, 3);
        setQueen(team, 8, 4);
        setKing(team, 8, 5);
        setBishop(team, 8, 6);
        setKnight(team, 8, 7);
        setRook(team, 8, 8);
    }


    private void setPawn(ChessGame.TeamColor color, int row, int column) {
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.PAWN);

        addPiece(position, piece);
    }

    private void setRook(ChessGame.TeamColor color, int row, int column) {
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.ROOK);

        addPiece(position, piece);
    }

    private void setKnight(ChessGame.TeamColor color, int row, int column) {
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);

        addPiece(position, piece);
    }

    private void setBishop(ChessGame.TeamColor color, int row, int column) {
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.BISHOP);

        addPiece(position, piece);
    }

    private void setQueen(ChessGame.TeamColor color, int row, int column) {
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.QUEEN);

        addPiece(position, piece);
    }

    private void setKing(ChessGame.TeamColor color, int row, int column) {
        ChessPosition position = new ChessPosition(row, column);
        ChessPiece piece = new ChessPiece(color, ChessPiece.PieceType.KING);

        addPiece(position, piece);
    }

    @Override
    public boolean equals(Object o) {
        ChessBoard that = (ChessBoard) o;

        for (int i=0; i < 8; i++) {
            for (int j=0; j < 8; j++) {
                if (this.board[i][j] == null && that.board[i][j] == null) {
                    continue;
                }
                if (this.board[i][j] instanceof ChessPiece && that.board[i][j] instanceof ChessPiece) {
                    if (this.board[i][j].equals(that.board[i][j])) {
                        continue;
                    }
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}

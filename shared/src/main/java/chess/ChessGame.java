package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> possibleMoves =  board.getPiece(startPosition).pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves) {
            if (testMove(move)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null ||
                board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn ||
                !validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException();
        }
        if (!testMove(move)) {
            throw new InvalidMoveException();
        } else {
            movePiece(move);
            switchTurn();
        }
    }

    private void switchTurn() {
        if (teamTurn == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    private boolean testMove(ChessMove move) {
        ChessBoard originalBoard = board;
        ChessBoard newBoard = board.clone();
        setBoard(newBoard);

        movePiece(move);

        if (isInCheck(board.getPiece(move.getEndPosition()).getTeamColor())) {
            setBoard(originalBoard);
            return false;
        } else {
            setBoard(originalBoard);
            return true;
        }
    }

    private void movePiece(ChessMove move) {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            board.clearSquare(move.getStartPosition());
        } else {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.clearSquare(move.getStartPosition());
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingLocation(teamColor);
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if (board.getPiece(currentPosition) != null && board.getPiece(currentPosition).getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleMoves = board.getPiece(currentPosition).pieceMoves(board, currentPosition);

                    for (ChessMove move : possibleMoves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessPosition getKingLocation(TeamColor teamColor) {
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(i, j));
                if (currentPiece != null &&
                        currentPiece.getPieceType() == ChessPiece.PieceType.KING &&
                        currentPiece.getTeamColor() == teamColor) {
                    return new ChessPosition(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noAvailableMovementCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noAvailableMovementCheck(teamColor);
    }

    private boolean noAvailableMovementCheck(TeamColor teamColor) {
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == teamColor && !validMoves(currentPosition).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

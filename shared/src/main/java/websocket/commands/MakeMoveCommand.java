package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessMove chessMove;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove chessMove) {
        super(authToken, gameID);
        commandType = CommandType.MAKE_MOVE;
        this.chessMove = chessMove;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }
}

package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;
import model.GameData;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static dataaccess.SQLExecution.configureTable;
import static dataaccess.SQLExecution.executeUpdate;

public class SQLGameDAO implements GameDAO {
    private final Gson gson;
    public SQLGameDAO() throws DataAccessException {
        configureTable(createGameStatements);
        gson = new GsonBuilder()
                .registerTypeAdapter(ChessBoard.class, new ChessBoardDeserializer())
                .create();
    }

    @Override
    public void clearGames() throws DataAccessException {
        String statement = "DELETE FROM games";
        executeUpdate(statement);
        statement = "ALTER TABLE games AUTO_INCREMENT = 1";
        executeUpdate(statement);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT GameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        games.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to retrieve data: " + e.getMessage());
        }
        return games;
    }

    private GameData readGame(ResultSet resultSet) throws SQLException {
        int resultID = resultSet.getInt("GameID");
        String resultWhiteUsername = resultSet.getString("whiteUsername");
        String resultBlackUsername = resultSet.getString("blackUsername");
        String resultGameName = resultSet.getString("gameName");
        ChessGame resultChessGame = gson.fromJson(resultSet.getString("game"), ChessGame.class);
        return new GameData(resultID, resultWhiteUsername, resultBlackUsername, resultGameName, resultChessGame);
    }

    public static class ChessBoardDeserializer implements JsonDeserializer<ChessBoard> {

        @Override
        public ChessBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray boardArray = jsonObject.getAsJsonArray("board");

            ChessBoard board = new ChessBoard();

            for (int i = 0; i < boardArray.size(); i++) {
                JsonArray row = boardArray.get(i).getAsJsonArray();
                for (int j = 0; j < row.size(); j++) {
                    JsonElement element = row.get(j);
                    if (element != null && !element.isJsonNull()) {
                        ChessPiece piece = context.deserialize(element, ChessPiece.class);
                        board.addPiece(new ChessPosition(i + 1, j + 1), piece);
                    }
                }
            }

            return board;
        }
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        String statement= "INSERT INTO games (gameName, game) VALUES(?, ?)";
        String newGameJson = gson.toJson(new ChessGame());
        return executeUpdate(statement, gameName, newGameJson);
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT GameID, whiteUsername, blackUsername, gameName, game FROM games WHERE GameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return readGame(resultSet);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "UPDATE games SET whiteUsername=?, blackUsername=?, game=? WHERE GameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameData.whiteUsername());
                ps.setString(2, gameData.blackUsername());
                ps.setString(3, gson.toJson(gameData.game()));
                ps.setInt(4, gameData.gameID());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to update data: %s", e.getMessage()));
        }
    }

    private final String[] createGameStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `GameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` VARCHAR(255) DEFAULT NULL,
              `blackUsername` VARCHAR(255) DEFAULT NULL,
              `gameName` TEXT NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`GameID`),
              FOREIGN KEY (`whiteUsername`) REFERENCES users(`username`)
              ON DELETE CASCADE
              ON UPDATE CASCADE,
              FOREIGN KEY (`blackUsername`) REFERENCES users(`username`)
              ON DELETE CASCADE
              ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}

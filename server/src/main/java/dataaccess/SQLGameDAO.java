package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.Exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import request.CreateGameRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static dataaccess.SQLExecution.executeUpdate;

public class SQLGameDAO implements GameDAO {
    private final Gson gson;
    public SQLGameDAO() throws DataAccessException {
        configureGameTable();
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
        return List.of();
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
                        int resultID = resultSet.getInt("GameID");
                        String resultWhiteUsername = resultSet.getString("whiteUsername");
                        String resultBlackUsername = resultSet.getString("blackUsername");
                        String resultGameName = resultSet.getString("gameName");
                        ChessGame resultChessGame = gson.fromJson(resultSet.getString("game"), ChessGame.class);
                        return new GameData(resultID, resultWhiteUsername, resultBlackUsername, resultGameName, resultChessGame);
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

    private ChessGame readChessGame(ResultSet rs) throws SQLException {
        var json = rs.getString("game");
        return new Gson().fromJson(json, ChessGame.class);
    }

    private void configureGameTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}

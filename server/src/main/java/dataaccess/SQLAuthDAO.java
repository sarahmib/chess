package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static dataaccess.SQLExecution.configureTable;
import static dataaccess.SQLExecution.executeUpdate;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureTable(createAuthStatements);
    }

    @Override
    public void clearAuths() throws DataAccessException {
        var statement = "DELETE FROM auths";
        executeUpdate(statement);
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        String statement = "INSERT INTO auths (authToken, username) VALUES(?, ?)";
        executeUpdate(statement, authToken, username);
        return new AuthData(authToken, username);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auths WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT authToken, username FROM auths WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        String resultAuthToken = resultSet.getString("authToken");
                        String resultUsername = resultSet.getString("username");
                        return new AuthData(resultAuthToken, resultUsername);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private final String[] createAuthStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `authToken` VARCHAR(255) NOT NULL,
              `username` VARCHAR(255) NOT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}

package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static dataaccess.SQLExecution.executeUpdate;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureUserTable();
    }

    @Override
    public void clearUsers() throws DataAccessException {
        var statement = "DELETE FROM users";
        executeUpdate(statement);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        String resultUsername = resultSet.getString("username");
                        String resultPassword = resultSet.getString("password");
                        String resultEmail = resultSet.getString("email");
                        return new UserData(resultUsername, resultPassword, resultEmail);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, username, hashedPassword, email);
    }

    @Override
    public boolean findUser(String username, String password) throws DataAccessException {
        return false;
    }

    private final String[] createUserStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` VARCHAR(255) NOT NULL UNIQUE,
              `password` VARCHAR(255) NOT NULL,
              `email` TEXT NOT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureUserTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createUserStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
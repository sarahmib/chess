package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureUserTable();
    }

    @Override
    public void clearUsers() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

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

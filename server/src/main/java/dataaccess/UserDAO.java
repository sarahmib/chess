package dataaccess;

import model.UserData;

public interface UserDAO {

    void clearUsers() throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;
}

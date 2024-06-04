package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import model.UserData;

public class SQLUserDAO implements UserDAO {
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
}

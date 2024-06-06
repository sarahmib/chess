package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private int nextId = 1;
    final private HashMap<Integer, UserData> users = new HashMap<>();

    @Override
    public void clearUsers() {
        users.clear();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            UserData userData = entry.getValue();
            if (userData.username().equals(username)) {
                return userData;
            }
        }
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData userData = new UserData(username, password, email);
        users.put(nextId, userData);
        nextId++;
    }

    @Override
    public boolean findUser(String username, String password) throws DataAccessException {
        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            UserData userData = entry.getValue();
            if (Objects.equals(userData.username(), username) && Objects.equals(userData.password(), password)) {
                return true;
            }
        }
        return false;
    }
}

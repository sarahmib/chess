package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private int nextId = 1;
    final private HashMap<Integer, AuthData> auths = new HashMap<>();

    @Override
    public void clearAuths() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        auths.put(nextId, authData);
        nextId++;

        return authData;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (Map.Entry<Integer, AuthData> entry : auths.entrySet()) {
            AuthData authData = entry.getValue();
            if (Objects.equals(authData.authToken(), authToken)) {
                auths.remove(entry.getKey());
                break;
            }
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (Map.Entry<Integer, AuthData> entry : auths.entrySet()) {
            AuthData authData = entry.getValue();
            if (Objects.equals(authData.authToken(), authToken)) {
                return authData;
            }
        }
        return null;
    }
}

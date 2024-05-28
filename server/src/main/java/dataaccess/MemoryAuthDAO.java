package dataaccess;

import model.AuthData;

import java.util.HashMap;
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

        return authData;
    }
}

package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    private int nextId = 1;
    final private HashMap<Integer, AuthData> auths = new HashMap<>();

    @Override
    public void clearAuths() {
        auths.clear();
    }
}

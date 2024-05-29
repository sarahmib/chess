package request;

public record CreateGameRequest(String gameName, String authToken) {

    public CreateGameRequest setAuthToken(String newAuthToken) {
        return new CreateGameRequest(gameName, newAuthToken);
    }
}

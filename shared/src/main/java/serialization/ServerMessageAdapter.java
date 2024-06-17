package serialization;

import com.google.gson.*;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import websocket.messages.*;

public class ServerMessageAdapter extends TypeAdapter<ServerMessage> {

    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, ServerMessage value) throws IOException {
        gson.toJson(value, value.getClass(), out);
    }

    @Override
    public ServerMessage read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "NOTIFICATION":
                return gson.fromJson(jsonObject, NotificationMessage.class);
            case "ERROR":
                return gson.fromJson(jsonObject, ErrorMessage.class);
            case "LOAD_GAME":
                return gson.fromJson(jsonObject, LoadGameMessage.class);
            default:
                throw new JsonParseException("Unknown element type: " + type);
        }
    }
}

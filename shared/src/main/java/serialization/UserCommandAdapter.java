package serialization;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import websocket.commands.*;

import java.io.IOException;

public class UserCommandAdapter extends TypeAdapter<UserGameCommand> {

    private final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, UserGameCommand value) throws IOException {
        gson.toJson(value, value.getClass(), out);
    }

    @Override
    public UserGameCommand read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObject.get("commandType").getAsString();

        switch (type) {
            case "CONNECT":
                return gson.fromJson(jsonObject, ConnectCommand.class);
            case "LEAVE":
                return gson.fromJson(jsonObject, LeaveCommand.class);
            case "MAKE_MOVE":
                return gson.fromJson(jsonObject, MakeMoveCommand.class);
            case "RESIGN":
                return gson.fromJson(jsonObject, ResignCommand.class);
            default:
                throw new JsonParseException("Unknown element type: " + type);
        }
    }
}

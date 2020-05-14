package uz.texnoblog.texnoblogsite;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class JsonParser {
    public User getMessage(String response) throws JSONException {
        JSONObject userJson = new JSONObject(response);
        String messages = userJson.getString("message");
        return new User(messages);
    }
}

package uz.texnoblog.texnoblogsite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Интерфейс авторизации.
**/
public class auth extends AppCompatActivity {
    private TextView debug_decode_json;
    private EditText login;

    private String getLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        debug_decode_json = findViewById(R.id.textView2);

        Button button = findViewById(R.id.buttonSend);
        View.OnClickListener obutton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = findViewById(R.id.enterLogin);
                getLogin = login.getText().toString();

                //Поток для API
                HttpClient httpClient = new HttpClient();
                httpClient.execute();
            }
        };
        button.setOnClickListener(obutton);
    }

    /**
     * Создает отдельный поток для работы API
     */
    public class HttpClient extends AsyncTask<Void, Void, Void> {
        private String token = "97342e877e8af4f595395474f2fa8bf6f185a0be";
        private String url = "https://www.texnoblog.uz/api/client/auth/";
        private static final String HEADER_AUTHORIZATION = "Authorization";

        public String message = "zz";

        protected Void doInBackground(Void... voids) {
            String urlParaders = "cn=Let's Encrypt Authority X3&o=Let's Encrypt&c=US";
            String output = "";

            try {
                URL url = new URL(this.url+"?" + "token="+this.token+
                        "&getLogin="+getLogin);

                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                //connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "ru-RU,ru;0.5");
                //connection.setDoOutput(true);

                //DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                //dStream.writeByte(Integer.parseInt(urlParaders));
                //dStream.flush();
                //dStream.close();

                //int responsCode = connection.getResponseCode();

                connection.connect(); // подключаемся к ресурсу

                //output += responsCode;

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder rensponsOutput = new StringBuilder();

                while ((line = br.readLine()) != null){
                    rensponsOutput.append(line);
                }
                br.close();

                JSONObject json = new JSONObject(rensponsOutput.toString()); //Декодирование JSON
                String messag = json.getString("messag");
                String Login = json.getString("getLogin");

                //output += System.getProperty("line.separator") + rensponsOutput.toString();
                //output += rensponsOutput.toString();
                this.message = "Код сообщения: "+messag+" Логин: "+Login;
            } catch (IOException | NumberFormatException | JSONException e) {
                e.printStackTrace();
                output = "не отработал запрос ошибка: "+e;
            }

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            debug_decode_json.setText(this.message);
        }
    }
}

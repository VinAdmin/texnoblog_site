package uz.texnoblog.texnoblogsite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class auth extends AppCompatActivity {
    public AsyncTask messages;
    private TextView aa;
    private EditText login;

    private String getLogin;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        aa = findViewById(R.id.textView2);


        Button button = findViewById(R.id.buttonSend);

        View.OnClickListener obutton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = findViewById(R.id.enterLogin);
                getLogin = login.getText().toString();

                //Поток для API
                HttpClient httpClient = new HttpClient();
                messages = httpClient.execute();
            }
        };

        button.setOnClickListener(obutton);

    }

    public class HttpClient extends AsyncTask<Void, Void, Void> {
        private static final String HEADER_AUTHORIZATION = "Authorization";
        private static final String GET = "GET";
        private final JsonParser jsonParser = new JsonParser();

        public String message = "zz";

        @SuppressLint("WrongThread")
        protected Void doInBackground(Void... voids) {
            String urlParaders = "cn=Let's Encrypt Authority X3&o=Let's Encrypt&c=US";
            String output = "";

            try {
                URL url = new URL("https://www.texnoblog.uz/api/client/auth/?getLogin="+getLogin);
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

                //output += responsCode;

                connection.connect(); // подключаемся к ресурсу

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
                this.message = messag+Login;
            } catch (IOException | NumberFormatException | JSONException e) {
                e.printStackTrace();
                output = "не отработал запрос ошибка: "+e;
            }

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            aa.setText(this.message);
        }
    }
}

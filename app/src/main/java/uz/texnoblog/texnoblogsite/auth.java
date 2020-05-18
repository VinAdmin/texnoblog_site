package uz.texnoblog.texnoblogsite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
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
    private EditText password;
    private TextView textToken;
    private WebView webView;
    private SimpleWebViewClient webViewClient;

    private String getLogin;
    private String getPassword;
    private String finish = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        debug_decode_json = findViewById(R.id.textView2);
        textToken = findViewById(R.id.textView3);

        webView = findViewById(R.id.webClient);
        webView.setVisibility(View.GONE);
        webViewClient = new SimpleWebViewClient();
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://www.texnoblog.uz/api/client/auth/?u_token=");

        Button enter = findViewById(R.id.buttonSend);
        View.OnClickListener buttonEnter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = findViewById(R.id.enterLogin);
                getLogin = login.getText().toString();
                password = findViewById(R.id.enterPassword);
                getPassword = password.getText().toString();
                textToken.getText().toString();
                textToken.setText(Token.getToken());

                //Поток для API

                HttpClient httpClient = new HttpClient();
                httpClient.execute();


                /**
                if (httpClient.message.equals("SUCCESS")){
                    //Intent intent = new Intent(auth.this, MainActivity.class);
                    //startActivity(intent);
                    //finish();
                }*/


            }
        };
        enter.setOnClickListener(buttonEnter);
    }

    /**
     * Создает отдельный поток для работы API
     */
    public class HttpClient extends AsyncTask<Void, Void, Void> {
        private String token = "97342e877e8af4f595395474f2fa8bf6f185a0be";
        private String url = "https://www.texnoblog.uz/api/client/auth/";
        private static final String HEADER_AUTHORIZATION = "Authorization";
        private String u_token;

        private String message = null;

        private BufferedReader bufferedReader;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            String urlParaders = "cn=Let's Encrypt Authority X3&o=Let's Encrypt&c=US";
            String output = "";

            try {
                URL url = new URL(this.url+"?" + "token="+token+
                        "&getLogin="+getLogin+"&getPassword="+getPassword);

                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                //connection.setRequestProperty("ACCEPT-LANGUAGE", "ru-RU,ru;0.5");
                //connection.setDoOutput(true);

                //DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                //dStream.writeByte(Integer.parseInt(urlParaders));
                //dStream.flush();
                //dStream.close();

                connection.getResponseCode();
                connection.connect(); // подключаемся к ресурсу

                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = null;
                StringBuilder rensponsOutput = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null){
                    rensponsOutput.append(line);
                }
                bufferedReader.close();

                //Декодирование JSON
                JSONObject json = new JSONObject(rensponsOutput.toString());
                String messag = json.getString("messag");
                String success = "SUCCESS";
                if(messag.equals(success)) {
                    String cookie = json.getString("cookie");
                    String u_token = json.getString("u_token");
                    this.u_token = u_token;
                }


                //output += System.getProperty("line.separator") + rensponsOutput.toString();
                //output += rensponsOutput.toString();
                message = "Код сообщения: "+messag+this.u_token;
            } catch (IOException | NumberFormatException | JSONException e) {
                e.printStackTrace();
                //output = "не отработал запрос ошибка: "+e;
                message = e.toString();
            }

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            debug_decode_json.setText(message);
            Token.setToken(this.u_token);
        }
    }
}

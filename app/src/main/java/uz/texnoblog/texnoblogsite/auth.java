package uz.texnoblog.texnoblogsite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    private String getLogin;
    private String getPassword;
    private String finish = null;

    public auth(){

    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        debug_decode_json = findViewById(R.id.textView2);
        login = findViewById(R.id.enterLogin);
        password = findViewById(R.id.enterPassword);

        Button enter = findViewById(R.id.buttonSend);

        View.OnClickListener buttonEnter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin = login.getText().toString();
                getPassword = password.getText().toString();

                if((login.getText().length() == 0) && (password.getText().length() == 0)){
                    debug_decode_json.setText("Не заполнены поля");
                }
                else if((login.getText().length() == 0) && (password.getText().length() != 0)){
                    debug_decode_json.setText("Не указан логин");
                }
                else if((login.getText().length() != 0) && (password.getText().length() == 0)){
                    debug_decode_json.setText("Не указан пароль");
                }
                else{
                    //Поток для API
                    HttpClient httpClient = new HttpClient(getLogin, getPassword);
                    httpClient.execute();
                }
            }
        };

        enter.setOnClickListener(buttonEnter);
    }

    final String FILE = "settings.json";
    final String LOG_TAG = "myLogs";

    /**
     * Сохранение настроик в файл.
     *
     * @param token
     */
    public void WriteFile(String token)
    {
        String text = "{ \"token\":"+token+" }";
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILE, MODE_PRIVATE)));
            // пишем данные
            bw.write(text);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создает отдельный поток для работы API
     */
    class HttpClient extends AsyncTask<Void, Void, Void> {
        private String token = "97342e877e8af4f595395474f2fa8bf6f185a0be";
        private String url = "https://www.texnoblog.uz/api/client/auth/";
        private String HEADER_AUTHORIZATION = "Authorization";
        private String u_token;
        public String success;
        private String getLogin;
        private String getPassword;

        private String message = null;

        private BufferedReader bufferedReader;

        public HttpClient(String getLogin, String getPassword) {
            this.getLogin = getLogin;
            this.getPassword = getPassword;
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... voids) {
            //String urlParaders = "cn=Let's Encrypt Authority X3&o=Let's Encrypt&c=US";
            //String output = "";

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
                this.success = messag;
                String success = "SUCCESS";
                if(messag.equals(success)) {
                    String cookie = json.getString("cookie");
                    String u_token = json.getString("u_token"); //Токен полученый от сайта и привязан к пользователю.
                    this.u_token = u_token;
                    User usr = new User();
                    usr.setTokent(this.u_token);
                    WriteFile(u_token); //Запись в файл.
                    message = "Код сообщения: "+messag+this.u_token;
                }
                if(messag.equals("NO LOGIN"))
                {
                    message = "Неверный логин или пароль";
                }

                //output += System.getProperty("line.separator") + rensponsOutput.toString();
                //output += rensponsOutput.toString();
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

            if (this.success.equals("SUCCESS")){
                Intent intent = new Intent(auth.this, MainActivity.class);
                intent.putExtra("success", this.success);
                intent.putExtra("token", this.u_token);
                startActivity(intent);
                finish();
            }
        }
    }
}

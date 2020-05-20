package uz.texnoblog.texnoblogsite;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    final String domain = "https://www.texnoblog.uz/";
    public static String Url;
    final String URL_AUTH_TOKEN = "api/client/checktoken/?token=";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReadSettings(); //Активация настроик.
        String Adress = null;

        WebView browser = findViewById(R.id.webBrowser);
        browser.getSettings().setJavaScriptEnabled(true);
        SimpleWebViewClient webViewClient = new SimpleWebViewClient();
        browser.setWebViewClient(webViewClient);

        Intent intent = getIntent();
        try{
            String success = intent.getStringExtra("success");
            String token = intent.getStringExtra("token");

            if(success.equals("SUCCESS")){
                Adress = domain+URL_AUTH_TOKEN+token;
                User.success = "NO";
            }
        }
        catch (Exception e){
            if(Url != null) {
                Adress = domain+Url;
            }
            else{
                Adress = domain;
            }
        }
        browser.loadUrl(Adress);
    }

    /**
     * Левое меню.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        User usr = new User();
        if(usr.getToken() != null) {
            //Авторизированный
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
            menu.getItem(2).setVisible(true);
        }
        else{
            //Не авториированный
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_auth :
                Intent intent = new Intent(this, auth.class);
                startActivity(intent);
                return true;
            case R.id.action_settings :
                Url = "profile";
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
            case R.id.action_exit :
                WriteSetting();
                User usr = new User();
                usr.setTokent("");
                MainActivity.Url = "site/logout";
                Intent intent3 = new Intent(this, MainActivity.class);
                startActivity(intent3);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ReadSettings(){
        //Чтение конфигурационного файла
        try{
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput("settings.json")));
            String str = "";
            // читаем содержимое
            StringBuilder rensponsOutput = new StringBuilder();
            while ((str = br.readLine()) != null) {
                rensponsOutput.append(str);
            }
            br.close();

            JSONObject json = new JSONObject(rensponsOutput.toString());
            String token = json.getString("token"); //Токен полученый от сайта и привязан к пользователю.
            User usr = new User();
            if(token != null) {
                usr.setTokent(token); //Сохраняем токе в память.
            }

        }
        catch (Exception e){
            e.printStackTrace();
            //создаём и отображаем текстовое уведомление
            Toast toast = Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void WriteSetting(){
        String text = "{ \"token\":\"\" }";
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput("settings.json", MODE_PRIVATE)));
            // пишем данные
            bw.write(text);
            // закрываем поток
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

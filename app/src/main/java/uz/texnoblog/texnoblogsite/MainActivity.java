package uz.texnoblog.texnoblogsite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView browser = findViewById(R.id.webBrowser);
        browser.getSettings().setJavaScriptEnabled(true);
        SimpleWebViewClient webViewClient = new SimpleWebViewClient();
        browser.setWebViewClient(webViewClient);

        Intent intent = getIntent();
        try{
            String success = intent.getStringExtra("success");
            String token = intent.getStringExtra("token");

            if(success.equals("SUCCESS")){
                browser.loadUrl("https://www.texnoblog.uz/api/client/checktoken/?token="+token);
                User.success = "NO";
            }
        }
        catch (Exception e){
            browser.loadUrl("https://www.texnoblog.uz");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }
}

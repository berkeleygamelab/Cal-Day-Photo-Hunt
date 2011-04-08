package com.urban_detection.tokentypes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class TokenTypes extends Activity {
    
    //private Button loginButton;
    //private Button keywordButton;
    //private Button sendpicButton;
    
    public static final String USERNAME_DEFAULT = "android_master_6502";
    public static final String PASSWORD_DEFAULT = "whypotholes";
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupUI();
    }
    
    public void setupUI() {
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(USERNAME_DEFAULT, PASSWORD_DEFAULT);
            }
        });
        
        Button keywordButton = (Button) findViewById(R.id.keyword_button);
        keywordButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword();
            }
        });
        
        Button sendpicButton = (Button) findViewById(R.id.sendpic_button);
        sendpicButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPic();
            }
        });
    }
    
    private void login(String username, String password) {
        String login = WebMessenger.login(username, password);
        Log.d("talkWithServer - login", "login response: " + login);
    }
    
    private void keyword() {
        String keyword = WebMessenger.getKeyword();
        Log.d("talkWithServer - keyword", "keyword response: " + keyword);
    }
    
    private void sendPic() {
        String response = WebMessenger.sendPic();
        Log.d("talkWithServer - sendPic", "sendPic response: " + response);
    }
}
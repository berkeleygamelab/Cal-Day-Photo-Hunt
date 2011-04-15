
package com.urban_detection.tokentypes.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.urban_detection.tokentypes.R;
import com.urban_detection.tokentypes.TokenTypes;


/**
 * A friendly startup screen.
 */
public class TitleScreen extends Activity {
    private ImageView myImageView;
    private Button debugButton;
    private Button loginButton;
    private Button registerButton;
    private Button creditsButton;
    private Intent myIntent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titlescreen_layout);
        
        myImageView = 
            (ImageView) findViewById(R.id.titlescreen_image);
        
        setupButtons();
    }
    
    private void setupButtons() {
        debugButton = (Button) findViewById(R.id.title_button_debug);
        debugButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                debugAction(v);
            }
        });
        debugButton.setVisibility(View.GONE);
        //debugButton.setVisibility(View.VISIBLE);
        
        loginButton = (Button) findViewById(R.id.title_button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startLoginActivity(v);
            }
        });
        
        registerButton = (Button) findViewById(R.id.title_button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //myIntent = new Intent(
                //    myImageView.getContext(), 
                //    com.urban_detection.tokentypes.
                //        gui.RegisterScreen.class
                //);
                //
                //startActivityForResult(
                //    myIntent, 
                //    GlobalData.SIGNIN_ACTION
                //);
            }
        });
        
        creditsButton = (Button) findViewById(R.id.title_button_credits);
        creditsButton.setOnClickListener(new ImageView.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                myIntent = new Intent(
                    v.getContext(), 
                    com.urban_detection.tokentypes.
                        gui.Credits.class);
                
                startActivity(myIntent);
            }
        });
    }
    
    private void startLoginActivity(View v) {
        myIntent = new Intent(
            v.getContext(), 
            com.urban_detection.tokentypes.
                gui.LoginScreen.class
        );
        
        startActivity(myIntent);
    }
    
    
    private void debugAction(View v) {
        myIntent = new Intent(
            v.getContext(), 
            com.urban_detection.tokentypes.
                gui.SubmitScreen.class);
                //TokenTypes.class);
        
        myIntent.putExtra("userID",   TokenTypes.USERID_DEFAULT);
        myIntent.putExtra("username", TokenTypes.USERNAME_DEFAULT);
        myIntent.putExtra("password", TokenTypes.PASSWORD_DEFAULT);
        myIntent.putExtra("filename", "/sdcard/dcim/Camera/IMG_20101212_111941.jpg");
        
        startActivity(myIntent);
    }
}
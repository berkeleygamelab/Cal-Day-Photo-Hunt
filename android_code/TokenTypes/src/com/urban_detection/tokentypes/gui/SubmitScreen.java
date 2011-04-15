package com.urban_detection.tokentypes.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.urban_detection.tokentypes.R;
import com.urban_detection.tokentypes.WebMessenger;

/**
 * Selects the catagory and submits the photo.
 */
public class SubmitScreen extends Activity {
    private Intent data;
    
    private ImageView imagePreview;
    private Button cancelButton;
    private Button submitButton;
    
    String filename;
    int userID;
    String username;
    String password;
    int keyword = 7;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_layout);
        
        setupIntent();
        setupImagePreview();
        setupUI();
    }
    
    private void setupIntent() {
        data = getIntent();
        
        userID = data.getIntExtra("userID", -1);
        username = data.getStringExtra("username");
        password = data.getStringExtra("password");
        filename = data.getStringExtra("filename");
        
        //TODO: Check for bad userid/password.
    }
    
    private void setupImagePreview() {
        imagePreview = (ImageView) findViewById(R.id.submit_imageview_photopreview);
        if (filename == null) {
            return;
        }
        
        Bitmap bMap = BitmapFactory.decodeFile(filename);
        imagePreview.setImageBitmap(bMap);
    }
    
    private void setupUI() {
        cancelButton = (Button) findViewById(R.id.submit_button_cancelbutton);
        cancelButton.setOnClickListener(new ImageView.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
        
        submitButton = (Button) findViewById(R.id.submit_button_submitbutton);
        submitButton.setOnClickListener(new ImageView.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                sendPic();
                setResult(RESULT_OK, null);
                finish();
            }
        });
    }
    
    
    private void sendPic() {
        String response = WebMessenger.sendPic(
            keyword, userID, password, filename);
        Log.d("talkWithServer - sendPic", 
            "sendPic response: " + response);
    }
    
}

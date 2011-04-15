
package com.urban_detection.tokentypes.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.urban_detection.tokentypes.GlobalData;
import com.urban_detection.tokentypes.R;


/**
 * A friendly startup screen.
 */
public class SelectPhotoMethod extends Activity {
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    
    private Intent data;
    private Intent myIntent;

    int userID;
    String username;
    String password;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_photo_method_layout);
        
        setupIntent();
        setupButtons();
    }
    
    private void setupIntent() {
        data = getIntent();
        
        userID = data.getIntExtra("userID", -1);
        username = data.getStringExtra("username");
        password = data.getStringExtra("password");
    }
    
    private void createReturnIntent(Intent intent) {
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
    }
    
    private void setupButtons() {
        cameraButton = (ImageButton) findViewById(R.id.select_imagebutton_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startCameraActivity(v);
            }
        });
        
        galleryButton = (ImageButton) findViewById(R.id.select_imagebutton_gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //myIntent = new Intent(
                //    myImageView.getContext(), 
                //    com.urban_detection.tokentypes.
                //        gui.LoginScreen.class
                //);
                //
                //startActivityForResult(
                //    myIntent, 
                //    0//GlobalData.SIGNIN_ACTION
                //);
            }
        });
    }
    
    
    private void startCameraActivity(View v) {
        myIntent = new Intent(
            v.getContext(), 
            com.urban_detection.tokentypes.
                photo.CameraPhoto.class
        );
        
        createReturnIntent(myIntent);
        startActivityForResult(
            myIntent, 
            GlobalData.ACTION_CAMERA
        );
    }
}
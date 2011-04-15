package com.urban_detection.tokentypes.gui;

import android.app.Activity;
import android.os.Bundle;

import com.urban_detection.tokentypes.R;

/**
 * Serves as a hub for starting, joining, and jumping between 
 * various in-game activities.
 */
public class Credits extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits_layout);
    }
    
}

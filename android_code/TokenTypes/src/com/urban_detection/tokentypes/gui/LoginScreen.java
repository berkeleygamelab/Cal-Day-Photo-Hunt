package com.urban_detection.tokentypes.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.urban_detection.tokentypes.GlobalData;
import com.urban_detection.tokentypes.R;
import com.urban_detection.tokentypes.Utils;
import com.urban_detection.tokentypes.WebMessenger;

/**
 * Prompts the user to log in to thier existing account or create a 
 * new one.
 */
public class LoginScreen extends Activity {
    private Context context;
    private ImageView myImageView;
    private Intent myIntent;
    
    private EditText usernameField;
    private EditText passwordField;
    
    private Button okButton;
    
    private static final int LOGIN = 0;
    private static final int CREATE = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        context = this;
        
        setupUI();
        //dismissDialog();
    }
    
    /**
     * Calls functions that create the user interface elements.
     */
    private void setupUI() {
        setupFields();
        setupButtons();
    }
    
    /**
     * Creates the user data collection fields.
     */
    private void setupFields() {
        myImageView  = (ImageView) findViewById(R.id.titlescreen_image);
        usernameField = (EditText) findViewById(R.id.login_edittext_username);
        passwordField = (EditText) findViewById(R.id.login_edittext_password);
    }
    
    /**
     * Creates the user interface buttons for logging in and 
     * creating new users.
     */
    private void setupButtons() {
        okButton = (Button) findViewById(R.id.login_button_ok);
        okButton.setOnClickListener(
            new ImageView.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                login(v);
            }
        });
    }
    
    protected void login(View v) {
        //showDialog(LOGIN);
        attemptToLogin(v);
    }
    
    /**
     * Attempts to login the user based on the current contents of the
     * username and password boxes.
     */
    private void attemptToLogin(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        
        //if (username.equals("")) {
        //    displayMsg(context.getString(R.string.enter_valid_username));
        //    return;
        //} else if (password.equals("")) {
        //    displayMsg(context.getString(R.string.enter_valid_password));
        //    return;
        //}
        
        String loginReply = WebMessenger.login(username, password);
        Log.d("talkWithServer - login", "login response:\n" + loginReply);
        
        int loginStatus = 0;
        int userID = 0;
        try {
            loginStatus = Integer.parseInt(Utils.webKeyValueLookup(loginReply, "status"));
            userID      = Integer.parseInt(Utils.webKeyValueLookup(loginReply, "id"));
        } catch (NumberFormatException e) {
            Log.e("LoginScreen.attemptToLogin", 
                "NumberFormatException: Could not parse XML login reply.");
            loginStatus = 0;
        } catch (NullPointerException e) {
            Log.e("LoginScreen.attemptToLogin", 
                "NullPointerException: Could not parse XML login reply.");
            loginStatus = 0;
        }
        
        if (loginStatus == GlobalData.LOGIN_SUCCESSFUL) {
            //displayMsg(
            //    context.getString(R.string.login_success) + 
            //    " " + username + "!");
            loginSuccessful(v, username, userID, password);
        } else {
            //displayMsg(context.getString(R.string.no_match));
        }
    }
    
    ///**
    // * Attempts to create a new user based on the current contents of
    // * the username and password boxes. Also logs the new user in if 
    // * successfully created.
    // */
    //private void attemptToCreateUser() {
    //    String username = 
    //        usernameField.getText().toString();
    //    String password = 
    //        passwordField.getText().toString();
    //    
    //    if (username.equals("")) {
    //        displayMsg(context.getString(R.string.enter_valid_username));
    //        return;
    //    } else if (password.equals("")) {
    //        displayMsg(context.getString(R.string.enter_valid_password));
    //        return;
    //    }
    //    
    //    String userInfo = WebMessenger.getUserInfo(username, null);
    //    int userID = getUserIDFromWebReply(userInfo);
    //    
    //    if (userID > 0) {
    //        displayMsg(context.getString(R.string.username_already_exists));
    //        return;
    //    }
    //    
    //    userInfo = WebMessenger.createNewUser(username, password);
    //    userID = getUserIDFromWebReply(userInfo);
    //    
    //    displayMsg(context.getString(R.string.create_user_success) +
    //        " " + username + "!");
    //    loginSuccessful(username, userID);
    //}
    
    /**
     * Sets the appropriate username and ID values before launching the
     * next activity.
     */
    private void loginSuccessful(View v, String username, 
        int userID, String password) 
    {
        //dismissDialog();
        
        myIntent = new Intent(
            v.getContext(), 
            com.urban_detection.tokentypes.
                gui.SelectPhotoMethod.class
        );
        
        myIntent.putExtra("userID", userID);
        myIntent.putExtra("username", username);
        myIntent.putExtra("password", password);
        
        startActivityForResult(
            myIntent, 
            GlobalData.ACTION_SELECTMETHOD
        );
    }
    
    ///**
    // * Returns the userID from the web reply, or -1 if no valid user
    // * was found.
    // */
    //private int getUserIDFromWebReply(String reply) {
    //    int userID = -1;
    //    
    //    try {
    //        JSONObject gameInfo = new JSONObject(reply);
    //        userID = gameInfo.getInt("userID");
    //    } catch (JSONException e) {
    //        e.printStackTrace();
    //    }
    //    
    //    return userID;
    //}
    
    ///**
    // * Displays a popup box with a given string of TEXT in it.
    // */
    //private void displayMsg(final String text) {
    //    Runnable r = new Runnable() {
    //        public void run() {
    //            Toast.makeText(
    //                getApplicationContext(), 
    //                text,
    //                Toast.LENGTH_SHORT
    //            ).show();
    //        }
    //    };
    //    
    //    loginHandler.post(r);
    //}
    //
    //private void dismissDialog() {
    //    if (progressDialog != null) {
    //    	progressDialog.dismiss();
    //    	//dismissDialog(LOGIN);
    //    	//dismissDialog(CREATE);
    //    }
    //}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Intent returnIntent = new Intent(v.getContext(),
        //    com.urban_detection.poker_walk.gui.TitleScreen.class);
        //
        //returnIntent.putExtra("username", usernameField.getText().toString());
        //returnIntent.putExtra("password", passwordField.getText().toString());
        
        setResult(RESULT_OK, null);
        finish();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //dismissDialog();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        //dismissDialog();
    }
    
    
    
    
    ///* Code for the deal-wait dialog box. */
    //static final int PROGRESS_DIALOG = 0;
    //ProgressThread progressThread;
    //ProgressDialog progressDialog;
    //class ProgressThread extends Thread {
    //    Handler mHandler;
    //    public final static int STATE_WAIT = 0;
    //    public final static int STATE_LOGIN = 1;
    //    public final static int STATE_CREATE = 2;
    //    int mState;
    //    
    //    ProgressThread(Handler h) {
    //        mHandler = h;
    //    }
    //    
    //    public void run() {
    //        //while (mState == STATE_WAIT) {
    //        //}
    //        
    //        if (mState == STATE_LOGIN) {
    //            attemptToLogin();
    //        } else if (mState == STATE_CREATE) {
    //            attemptToCreateUser();
    //        }
    //        
    //        dismissDialog();
    //    }
    //    
    //    /* sets the current state for the thread,
    //     * used to stop the thread */
    //    public void setState(int state) {
    //        mState = state;
    //    }
    //}
    //
    //// Define the Handler that receives messages from the thread and update the progress
    //final Handler loginHandler = new Handler() {
    //    public void handleMessage(Message msg) {
    //    }
    //};
    //
    //
    //protected void create() {
    //    showDialog(CREATE);
    //}
    //
    //protected Dialog onCreateDialog(int id) {
    //    Resources res = getResources();
    //    
    //    switch(id) {
    //    case LOGIN:
    //        progressDialog = ProgressDialog.show(
    //            LoginScreen.this, 
    //            res.getString(R.string.title_login_dialog_title),
    //            res.getString(R.string.title_login_dialog_message),
    //            true
    //        );
    //        //progressDialog.setCancelable(true);
    //        return progressDialog;
    //    case CREATE:
    //        progressDialog = ProgressDialog.show(
    //            LoginScreen.this, 
    //            res.getString(R.string.title_create_dialog_title),
    //            res.getString(R.string.title_create_dialog_message),
    //            true
    //        );
    //        //progressDialog.setCancelable(true);
    //        return progressDialog;
    //    default:
    //        return null;
    //    }
    //}
    //
    //@Override
    //protected void onPrepareDialog(int id, Dialog dialog) {
    //	progressDialog.setProgress(0);
    //    progressThread = new ProgressThread(loginHandler);
    //    
    //    switch(id) {
    //    case LOGIN:
    //    	progressThread.setState(ProgressThread.STATE_LOGIN);
    //    	break;
    //    case CREATE:
    //    	progressThread.setState(ProgressThread.STATE_CREATE);
    //    	break;
    //    default:
    //        break;
    //    }
    //    
    //    progressThread.start();
    //}
}
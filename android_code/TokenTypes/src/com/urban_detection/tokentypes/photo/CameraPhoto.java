package com.urban_detection.tokentypes.photo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.urban_detection.tokentypes.GlobalData;
import com.urban_detection.tokentypes.R;

// ----------------------------------------------------------------------

public class CameraPhoto extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private android.hardware.Camera mCamera;
    private boolean mPreviewRunning;
    
    private ImageButton mapViewButton;
    private ImageButton takePictureButton;
    
    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    
    private String storagePath;
    private String path = "/dcim/Camera/";
    private String date;
    private String title = "cal-day-photo-hunt";
    private String extension = ".jpg";
    private String fullpath;
    private String filename;
    
    private Button yesButton;
    private Button noButton;
    private TextView debugImageText;
    
    private PictureCallback mPictureCallbackRaw;
    private PictureCallback mPictureCallbackJpeg;
    private ShutterCallback mShutterCallback;
    
    private Calendar calendar;
    private SimpleDateFormat timeStampFormat = 
        new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");    
    
    private Intent data;
    private Intent myIntent;

    int userID;
    String username;
    String password;
    
    private ZoomButtonsController zoomController;
	private ImageView myImageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.camera_layout);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        myImageView = (ImageView) findViewById(R.id.titlescreen_image);
        
        data = getIntent();
        
        setupIntent();
        setupZoom();
        setupStoragePath();
        setupUI();
        setupCallbacks();
        setupSurfaceView();
    }
    
    private void setupIntent() {
        data = getIntent();
        
        userID = data.getIntExtra("userID", -1);
        username = data.getStringExtra("username");
        password = data.getStringExtra("password");
    }
    
    private void setupReturnIntent(Intent intent) {
        intent.putExtra("userID", userID);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("filename", fullpath);
    }
    
    private void setupZoom() {
        //zoomController = new ZoomButtonsController(mSurfaceView);
        //zoomController.isVisible();
        //zoomController.setZoomInEnabled(true);
        //zoomController.setZoomOutEnabled(true);
        
    }
    
    private void setupStoragePath() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            
            //TODO: Flag ERROR - Can not write.
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            
            storagePath = "/emmc";
        }
    }
    
    private void setupUI() {
        //The 'Take-a-picture' button.
        takePictureButton = (ImageButton) findViewById(R.id.camera_imagebutton_takepicturebutton);
        takePictureButton.setOnClickListener(new SurfaceView.OnClickListener() {
            @Override
            public void onClick(View v) {
                myIntent = new Intent(
                    v.getContext(), 
                    com.urban_detection.tokentypes.
                        gui.SubmitScreen.class);
                
                ImageCaptureCallback iccb = null;
                buildFilename();
                mCamera.takePicture(mShutterCallback, null, mPictureCallbackJpeg);
            }
        });
        
    }
    
    
    private void setupCallbacks() {
        mShutterCallback = new ShutterCallback() {  
            public void onShutter() {  
                Log.e(getClass().getSimpleName(), "SHUTTER CALLBACK");  
            }  
        };
        
        mPictureCallbackJpeg = new PictureCallback() {  
            public void onPictureTaken(byte[] camData, Camera c) {  
                takePic(camData, c);
            }  
        };
    }
    
    private void takePic(byte[] camData, Camera c) {
        Log.d(getClass().getSimpleName(), "PICTURE CALLBACK JPEG: data.length = " + camData);
        
        try {
            /*
             write to local sandbox file system
             outStream =
             CameraDemo.this.openFileOutput(String.format("%d.jpg",
             System.currentTimeMillis()), 0);
             Or write to sdcard
            */
            fullpath = storagePath + path + filename;
            FileOutputStream outStream = new FileOutputStream(fullpath);
            Log.d("JPEG_Callback", "onPictureTaken - set path: " + fullpath);
            
            outStream.write(camData);
            outStream.flush();
            outStream.close();
            Log.d("JPEG_Callback", "onPictureTaken - wrote bytes: " + camData.length);
            
            prepareToSendPhoto();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void prepareToSendPhoto() {
        setupReturnIntent(myIntent);
        startActivityForResult(
            myIntent, GlobalData.ACTION_SUBMIT);
    }
    
    private void setupSurfaceView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    
    /**
     * Called to keep the device from resetting on a rotate.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it 
        // where to draw.
        mCamera = Camera.open();
        
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
            // TODO: add more exception handling logic here
        }
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        mCamera.stopPreview();
        mPreviewRunning = false;
        mCamera.release();
        mCamera = null;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        
        if (mPreviewRunning) {
            mCamera.stopPreview();
        }
        
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(w, h);
        //Parameters parameters = camera.getParameters();
        parameters.set("jpeg-quality", 100);
        //parameters.set("orientation", "portrait");
        //parameters.set("orientation", "landscape");
        //parameters.set("picture-size", "320X430");
        //parameters.set("rotation", 90);
        parameters.setPictureFormat(PixelFormat.JPEG);

        mCamera.setParameters(parameters);
        try {            
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        mCamera.startPreview();
        mPreviewRunning = true;
    }
    
    private void buildFilename() {
        calendar = Calendar.getInstance();
        date = timeStampFormat.format(new Date(System.currentTimeMillis()));//calendar.get(Calendar.DATE));
        
        filename = title + "_" + date + extension;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, null);
            finish();
        } else if (resultCode == RESULT_CANCELED) {
            mCamera.startPreview();
        }
    }
}


class ImageCaptureCallback implements PictureCallback  {  
    private OutputStream filoutputStream;  
    private String filename;
    
    public ImageCaptureCallback(OutputStream filoutputStream, String filename) {  
        this.filoutputStream = filoutputStream;  
        this.filename = filename;
    }  

    @Override    
    public void onPictureTaken(byte[] camData, android.hardware.Camera camera) {  
        try {  
            Log.v(getClass().getSimpleName(), "onPictureTaken=" + camData + " length = " + camData.length);  
            
            //Write the picture to someplace that WE want.
            //FileOutputStream buf = new FileOutputStream("/sdcard/dcim/Camera/" + filename);
            //buf.write(camData);
            //buf.flush();
            //buf.close();

            filoutputStream.write(camData);  
            filoutputStream.flush();  
            filoutputStream.close();
            Log.d("ImageCaptureCallback", "Took a picture: " + filename);
        } catch(Exception ex) {  
            ex.printStackTrace();  
        }  
    }
}

package com.urban_detection.tokentypes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * Sends and receives messages to/from the game server.
 */
public class WebMessenger {
    private static final String BASE_URL = "http://trackfx.org/PhotoGame/testdrive/";
    private static final String GETKEYWORD_R_ARG = "mobile/keyword";
    private static final String LOGIN_R_ARG = "mobile/login";
    
    public static String register(String username, String password, String email) {
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httppost = new HttpPost(BASE_URL);
        HttpPost httppost = new HttpPost(BASE_URL
                + "?r=mobile/register&username=" + username + "&password="
                + password + "&email=" + email);
        HttpResponse response = null;
        try {
            //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("r", GETKEYWORD_R_ARG));
            
            response = httpclient.execute(httppost);
            String processedResponse = Utils.convertInputStreamToString(response.getEntity().getContent());
            
            Log.i("WebMessenger - register", "Returning response.");
            return processedResponse;
        } catch (ClientProtocolException e) {
            Log.e("WebMessenger - register", "ERROR - ClientProtocolException.");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("WebMessenger - register", "ERROR - IOException.");
            e.printStackTrace();
        }

        Log.e("WebMessenger - register",
                "ERROR - Bailed out of message request.");
        return null;
    }

    /**
     * Requests a keyword from the server.
     */
    public static String getKeyword() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL);
        HttpResponse response = null;

        try {
            Log.i("WebMessenger - getKeyword", "Preparing keyword request.");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("r", GETKEYWORD_R_ARG));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            Log.i("WebMessenger - getKeyword", "Sending keyword request...");
            response = httpclient.execute(httppost);
            Log.i("WebMessenger - getKeyword", "Processing response...");
            String processedResponse = Utils.convertInputStreamToString(response
                    .getEntity().getContent());
            Log.i("WebMessenger - getKeyword", "Returning response.");
            return processedResponse;
        } catch (ClientProtocolException e) {
            Log.e("WebMessenger - getKeyword",
                    "ERROR - ClientProtocolException.");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("WebMessenger - getKeyword", "ERROR - IOException.");
            e.printStackTrace();
        }

        Log.e("WebMessenger - getKeyword",
                "ERROR - Bailed out of message request.");
        return null;
    }

    /**
     * Requests a keyword from the server.
     */
    public static String login(String username, String password) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL);
        //HttpPost httppost = new HttpPost(BASE_URL + "?r=mobile/login&username="
        //        + username + "&password=" + password);
        HttpResponse response = null;

        try {
            Log.i("WebMessenger - login", "Preparing keyword request.");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("r", "mobile/login"));
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            Log.i("WebMessenger - login", "Sending keyword request...");
            response = httpclient.execute(httppost);
            Log.i("WebMessenger - login", "Processing response...");
            String processedResponse = Utils.convertInputStreamToString(response
                    .getEntity().getContent());
            Log.i("WebMessenger - login", "Returning response.");
            return processedResponse;
        } catch (ClientProtocolException e) {
            Log.e("WebMessenger - login", "ERROR - ClientProtocolException.");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("WebMessenger - login", "ERROR - IOException.");
            e.printStackTrace();
        }

        Log.e("WebMessenger - login", "ERROR - Bailed out of message request.");
        return null;
    }

    public static String sendPic(
        int keywordID, int userID,
        String password, String filePath) 
    {
        return sendPic("" + keywordID, "" + userID, password, filePath);
    }
    
    /**
     * http://getablogger.blogspot.com/2008/01/android-how-to-post-file-to-php-server.html
     */
    public static String sendPic(
        String keywordID, String userID,
        String password, String filePath) 
    {
        Log.i("WebMessenger.sendPic", "Loading picture.");
        File file = new File(filePath);
        Log.i("WebMessenger.sendPic", "Picture loaded.");
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL);
                //+ "?r=mobile/upload&keyword=" + keywordID + "&userID=" + userID
                //+ "&password=" + password);
        
        //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
          reqEntity.addPart("r", new StringBody("mobile/upload"));
          reqEntity.addPart("user_id", new StringBody(userID));
          reqEntity.addPart("password", new StringBody(password));
          reqEntity.addPart("keyword", new StringBody(keywordID));
        } catch (UnsupportedEncodingException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
          return null;
        }Log.i("WebMessenger.sendPic", "Adding picture to request.");
        reqEntity.addPart("uploadedfile", new FileBody(file));
        httppost.setEntity(reqEntity);
        
        Log.i("WebMessenger.sendPic", "Sending picture request...");
        HttpResponse response;
        
        try {
            response = httpclient.execute(httppost);
            String processedResponse = Utils.convertInputStreamToString(response.getEntity().getContent());
        
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                Log.i("WebMessenger.sendPic", "RESPONSE: " + processedResponse);//, EntityUtils.toString(resEntity));
            }
            
            return processedResponse;
        } catch (ClientProtocolException e) {
            Log.e("WebMessenger - sendPic", "ERROR - ClientProtocolException.");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("WebMessenger - sendPic", "ERROR - IOException.");
            e.printStackTrace();
        }
        
        Log.e("WebMessenger - sendPic",
                "ERROR - Bailed out of message request.");
        return null;
        
        
        //URL connectURL
        //try{
        //    connectURL = new URL(urlString);
        //}catch(Exception ex){
        //    Log.i("URL FORMATION","MALFORMATED URL");
        //    return null;
        //}
        //
        //try
        //{
        //    HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
        //    conn.setDoInput(true);
        //    conn.setDoOutput(true);
        //    conn.setUseCaches(false);
        //    conn.setRequestMethod("POST");
        //    conn.setRequestProperty("Connection", "Keep-Alive");
        //    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
        //    DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
        //    
        //    dos.writeBytes(twoHyphens + boundary + lineEnd);
        //    dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + exsistingFileName +"\"" + lineEnd);
        //    dos.writeBytes(lineEnd);
        //}
    }

    public static String getGamesList() {
        // HttpClient httpclient = new DefaultHttpClient();
        // HttpGet httpget = new HttpGet(GETGAMESLIST_URL);
        // HttpResponse response = null;
        //
        // try {
        // response = httpclient.execute(httpget);
        // return convertInputStreamToString(response.getEntity().getContent());
        // } catch (ClientProtocolException e) {
        // } catch (IOException e) {
        // }

        return null;
    }

    public String sendClue(String caption, String pictureFilename) {
        // Create a new HttpClient and Post Header
        // HttpClient httpclient = new DefaultHttpClient();
        // HttpPut photoPost = null;
        // try {
        // photoPost = new HttpPut(sendPhotosURL + clueContents.getInt("id") +
        // ".json");
        // } catch (JSONException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // return null;
        // }
        // MultipartEntity nameValuePairsPhotos = new MultipartEntity();
        // nameValuePairsPhotos.addPart("clue[photo]", new FileBody(new
        // File(pictureFilename)));
        // photoPost.setEntity(nameValuePairsPhotos);
        //
        // // Execute HTTP Post Request
        // HttpResponse responsePhoto = httpclient.execute(photoPost);
        // return
        // convertInputStreamToString(responsePhoto.getEntity().getContent());
        // } catch (ClientProtocolException e) {
        // } catch (IOException e) {
        // }
        //
        return null;
    }
}

/*
Copyright ©2010 Created by James Holston, Greg Niemeyer, Pedro Cota,
Jonathan Hirschberg, Scott Hoag and Anna Vignet. Departments of
Anthropology and Art Practice, University of California, Berkeley. The
Regents of the University of California (Regents). All Rights
Reserved. Permission to use, copy, modify, and distribute this
software and its documentation for educational, research, and
not-for-profit purposes, without fee and without a signed licensing
agreement, is hereby granted, provided that the above copyright
notice, this paragraph and the following two paragraphs appear in all
copies, modifications, and distributions. Contact The Office of
Technology Licensing, UC Berkeley, 2150 Shattuck Avenue, Suite 510,
Berkeley, CA 94720-1620, (510) 643-7201, for commercial licensing
opportunities.

IN NO EVENT SHALL REGENTS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
REGENTS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

REGENTS SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF
ANY, PROVIDED HEREUNDER IS PROVIDED "AS IS". REGENTS HAS NO OBLIGATION
TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
MODIFICATIONS.
 */

package com.urban_detection.tokentypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
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

	private static String convertInputStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return convertStreamToString(reader);
	}

	private static String convertFileReaderToString(FileReader fr) {
		BufferedReader reader = new BufferedReader(fr);
		return convertStreamToString(reader);
	}

	private static String convertStreamToString(BufferedReader reader) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return sb.toString();
	}

	public static String register(String username, String password, String email) {
		HttpClient httpclient = new DefaultHttpClient();
		// HttpPost httppost = new HttpPost(BASE_URL);
		HttpPost httppost = new HttpPost(BASE_URL
				+ "?r=mobile/register&username=" + username + "&password="
				+ password + "&email=" + email);
		HttpResponse response = null;
		try {
			Log.i("WebMessenger - register", "Preparing register request.");
			Log.i("WebMessenger - register", "Sending register request...");
			response = httpclient.execute(httppost);
			Log.i("WebMessenger - register", "Processing response...");
			String processedResponse = convertInputStreamToString(response
					.getEntity().getContent());
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
			String processedResponse = convertInputStreamToString(response
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
		// HttpPost httppost = new HttpPost(BASE_URL);
		HttpPost httppost = new HttpPost(BASE_URL + "?r=mobile/login&username="
				+ username + "&password=" + password);
		HttpResponse response = null;

		try {
			Log.i("WebMessenger - login", "Preparing keyword request.");
			// List<NameValuePair> nameValuePairs = new
			// ArrayList<NameValuePair>(3);
			// nameValuePairs.add(new BasicNameValuePair("r", LOGIN_R_ARG));
			// nameValuePairs.add(new BasicNameValuePair("username", username));
			// nameValuePairs.add(new BasicNameValuePair("password", password));
			// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			Log.i("WebMessenger - login", "Sending keyword request...");
			response = httpclient.execute(httppost);
			Log.i("WebMessenger - login", "Processing response...");
			String processedResponse = convertInputStreamToString(response
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

	public static String sendPic(String keyWord, String userID,
			String password, String filePath) {
		File file = new File(filePath);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(BASE_URL
				+ "# ?r=mobile/upload&keyword=" + keyWord + "&userID=" + userID
				+ "&password=" + password);

		FileBody bin = new FileBody(file);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		reqEntity.addPart("myFile", bin);
		httppost.setEntity(reqEntity);

		HttpResponse response = null;

		try {
			Log.i("WebMessenger - sendPic",
					"Preparing picture sending request.");
			Log.i("WebMessenger - sendPic",
					"Sending picture sending request...");
			response = httpclient.execute(httppost);
			Log.i("WebMessenger - sendPic", "Processing response...");
			String processedResponse = convertInputStreamToString(response
					.getEntity().getContent());
			Log.i("WebMessenger - sendPic", "Returning response.");

			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				Log.i("RESPONSE", EntityUtils.toString(resEntity));
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

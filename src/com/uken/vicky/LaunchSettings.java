package com.uken.vicky;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchSettings extends UiAutomatorTestCase {
	HttpClient httpClient = new DefaultHttpClient();

	public void testDemo() throws UiObjectNotFoundException, InterruptedException {
	  Log.i("vicky", "here");
      // Simulate a short press on the HOME button.
      getUiDevice().pressHome();
      
      // We’re now in the home screen. Next, we want to simulate 
      // a user bringing up the All Apps screen.
      // If you use the uiautomatorviewer tool to capture a snapshot 
      // of the Home screen, notice that the All Apps button’s 
      // content-description property has the value “Apps”.  We can 
      // use this property to create a UiSelector to find the button. 
      UiObject allAppsButton = new UiObject(new UiSelector()
         .description("Apps"));
      
      // Simulate a click to bring up the All Apps screen.
      allAppsButton.clickAndWaitForNewWindow();
      
      // In the All Apps screen, the Settings app is located in 
      // the Apps tab. To simulate the user bringing up the Apps tab,
      // we create a UiSelector to find a tab with the text 
      // label “Apps”.
      UiObject appsTab = new UiObject(new UiSelector()
         .text("Kik"));
      
      // Simulate a click to enter the Apps tab.
      appsTab.clickAndWaitForNewWindow();


      while(true) {
    	  if (hasConversation()) {
    		  chat();
    	  } else {
    		Thread.sleep(10000);
    	  }
      }
	}
	
	protected boolean hasConversation() {
		UiSelector firstEl = new UiSelector()
				.className("android.widget.ListView").index(3)
				.childSelector(new UiSelector().index(0));
		UiObject conversation = new UiObject(firstEl);
		return conversation.exists();
	}


	protected void chat() throws UiObjectNotFoundException {
		// Select First conversation
		UiSelector firstEl = new UiSelector()
				.className("android.widget.ListView").index(3)
				.childSelector(new UiSelector().index(0));
		UiObject conversation = new UiObject(firstEl);
		conversation.clickAndWaitForNewWindow();

		// Reads Text
		UiObject text = new UiObject(find("ListView-2.LinearLayout-2.TextView-0").instance(0));
		String response;
		if (text.exists()) {
			String userInput = text.getText();
			// Get response from the server
			response = getResponseFor(userInput.toString());
		} else {
			response = "What?";
		}

		// Write response back
		UiObject textBox = new UiObject(find("EditText-0"));
		UiObject submitButton = new UiObject(find("LinearLayout-5.ImageView-3"));
		textBox.setText(response);
		submitButton.click();

		// Delete conversation
		UiObject conversarionManager = new UiObject(new UiSelector().className(
				"android.widget.ImageView").instance(2));
		conversarionManager.clickAndWaitForNewWindow();
		UiObject closeButton = new UiObject(find("TextView-3"));
		closeButton.clickAndWaitForNewWindow();
		UiObject confirmCloseButton = new UiObject(find("Button-1"));
		confirmCloseButton.click();
	}

	
	private String getResponseFor(String userInput){
//		return "Did you just say '" + userInput + "'?";
		System.out.println("gettting response");
		String encodedInput;
		try {
			encodedInput = URLEncoder.encode(userInput, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return "I beg your pardon?";
		}
//		String endpoint = "http://api.program-o.com/v2.3.1/chatbot/?bot_id=6&convo_id=4815162342&say=" + encodedInput;
		String endpoint = "http://192.168.32.51:8080/hubot/tell/?message=" + encodedInput;
		HttpGet get = new HttpGet(endpoint);
		try {
			HttpResponse response = httpClient.execute(get);
			String res = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(res);
			System.out.println("resposne" + res);
//			return json.getString("botsay");
			return json.getString("message");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "I'm sorry, I'm suffering a horrible case of ClientProtocolException";
		} catch (IOException e) {
			e.printStackTrace();
			return "I'm sorry, I'm suffering a horrible case of IOException";
		} catch (JSONException e) {
			e.printStackTrace();
			return "I'm sorry, I'm suffering a horrible case of JSONException";
		}
	}


	private UiSelector find(String selectString) throws UiObjectNotFoundException {
		UiSelector currentSel = new UiSelector().index(0);
		String[] elements = selectString.split("\\.");
		for (String element : elements) {
			String[] classIndex = element.split("-");
			String klassName = "android.widget." + classIndex[0];
			int elIndex = Integer.parseInt(classIndex[1]);
			currentSel = currentSel.childSelector(
					new UiSelector().className(klassName).index(elIndex)
			);
		}
		return currentSel;
	}
	
	private UiSelector findIndexPath(int[] indexes) throws UiObjectNotFoundException {
		UiSelector currentSel = new UiSelector().index(0);
		for (int i: indexes) {
			currentSel = currentSel.childSelector(
					new UiSelector().index(i)
			);
		}
		return currentSel;
	}
}

// TODO: if kik is already in a conversation window, it'll break. If it breaks, kill kik and start over

package com.prefabsoft.android.registration;

import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.prefabsoft.android.AbstractAsyncActivity;

public class RegistrationActivity extends AbstractAsyncActivity {

	protected static final String TAG = RegistrationActivity.class.getSimpleName();
	
	private EditText emailEdit;
	private EditText passwordEdit;
	private EditText passwordConfirmEdit;
	
	private String emailString;
	private String passwordString;

	//***************************************
    // Activity methods
    //***************************************
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		emailEdit = (EditText) this.findViewById(R.id.emailEdit);
		passwordEdit = (EditText) this.findViewById(R.id.passwordEdit);
		passwordConfirmEdit = (EditText) this
				.findViewById(R.id.passwordConfirmEdit);
	}

	//***************************************
    // Private methods
    //***************************************	
	public void onRegisterClick(View v) {
		
		emailString = emailEdit.getText().toString();
		passwordString = passwordEdit.getText().toString();
		String passwordConfirmString = passwordConfirmEdit.getText().toString();
		
		Log.d(TAG + " onRegisterClick", "emailEdit: " + emailString
				+ " passwordEdit: " + passwordString
				+ " passwordConfirmEdit: " + passwordConfirmString);
		
		if(!passwordString.equals(passwordConfirmString)){
			Log.d(TAG + " onRegisterClick", "Passwords do not match.");
			showResult("Passwords do not match, please try again");
		}else{
			Log.d(TAG + " onRegisterClick", "Passwords do match, posting registration.");
			
			new PostMessageTask().execute();
			
			showResult("Registration succesful! Thanks!");
		}
	}
	
	private void showResult(String result)
	{
		if (result != null){
			// display a notification to the user with the response message
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(this, "I got null, something happened!", Toast.LENGTH_LONG).show();
		}
	}
	
	
	//***************************************
    // Private classes
    //***************************************
	private class PostMessageTask extends AsyncTask<Void, Void, String> 
	{	
		private MultiValueMap<String, String> map;
		
		@Override
		protected void onPreExecute() 
		{
			// before the network request begins, show a progress indicator
			showLoadingProgressDialog();
			
			// assemble the map
			map = new LinkedMultiValueMap<String, String>();
			map.add("email", emailString);
			map.add("password", passwordString);
			
			map.add("id", "1");
			map.add("subject", "subject");
			map.add("text", "text");
		}
		
		@Override
		protected String doInBackground(Void... params) 
		{
			try 
			{
				// The URL for making the POST request
//				final String url = getString(R.string.temp_uri);// + "/register";
//				final String url = getString(R.string.base_uri) + "/register";
				final String url = getString(R.string.prod_base_uri) + "/register";
				
				Log.d(TAG + " doInBackground", "url: "+url+ 
						"email: "+emailString+" password: "+passwordString);
				
				// Create a new RestTemplate instance
				RestTemplate restTemplate = new RestTemplate();
				
				Log.d(TAG + " doInBackground","map: "+map);
				// Make the network request, posting the message, expecting a String in response from the server
				ResponseEntity<String> response = restTemplate.postForEntity(url, map, String.class);
				
				// Return the response body to display to the user
				return response.getBody();
			} 
			catch(Exception e) 
			{
				Log.e(TAG, e.getMessage(), e);
			} 
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) 
		{
			// after the network request completes, hid the progress indicator
			dismissProgressDialog();
			
			// return the response body to the calling class
			showResult(result);
		}
	}
}
package com.linhnguyen.googleweatherthread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	String urls = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=VietNam&format=json&num_of_days=5&key=9ee5cd598c0309e0e1cae6aaae8671c8a3035773";
	String result;
	TextView txtcountry, txtcurrent, txttemprature, txthumidity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				result = readJSONURL(urls);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject(result);

							JSONObject data = jsonObject.getJSONObject("data");

							JSONArray current_condition = data
									.getJSONArray("current_condition");
							JSONObject jO = current_condition.getJSONObject(0);
							String temp_C = jO.getString("temp_C");
							String humidity = jO.getString("humidity");

							JSONArray request = data.getJSONArray("request");
							JSONObject jORequest = request.getJSONObject(0);
							String query = jORequest.getString("query");

							JSONArray weather = data.getJSONArray("weather");
							JSONObject jOWeather = weather.getJSONObject(0);
							JSONArray weatherDesc = jOWeather
									.getJSONArray("weatherDesc");
							JSONObject jOWeatherDesc = weatherDesc
									.getJSONObject(0);
							String value = jOWeatherDesc.getString("value");

							txtcountry = (TextView) findViewById(R.id.txtcountry);
							txtcountry.setText(query);
							txttemprature = (TextView) findViewById(R.id.txttemprature);
							txttemprature.setText(temp_C + "\u2103");
							txtcurrent = (TextView) findViewById(R.id.txtcurrent);
							txtcurrent.setText(value);
							txthumidity = (TextView) findViewById(R.id.txthumidity);
							txthumidity.setText(humidity);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				// threadMsg(result);
			}

		});
		thread.start();

	}

	public String readJSONURL(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				Log.e("JSON", "Failed to download file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

}

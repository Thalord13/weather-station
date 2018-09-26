package com.example.weatherstation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	ImageView iv;
	TextView cond;
	EditText txtCity, txtTemp, txtHumidity;
	Button btnOkey, btnCancel;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        this.iv = (ImageView) this.findViewById(R.id.imageView1);
        this.txtCity = (EditText) this.findViewById(R.id.editText1);
        this.txtTemp = (EditText) this.findViewById(R.id.editText2);
        this.txtHumidity = (EditText) this.findViewById(R.id.editText3);
        this.cond = (TextView) this.findViewById(R.id.textView1);
        
        this.btnOkey = (Button) this.findViewById(R.id.button1);
        this.btnCancel = (Button) this.findViewById(R.id.button2);
        
        this.btnOkey.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
    } 	

	@Override
	public void onClick(View arg0) {
		
		switch(arg0.getId()){
		case R.id.button1:

			String city = this.txtCity.getText().toString();
			if(!city.equals("")){
				try {
					URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=80be1b641d4c8c90e187da407478d461");
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String weather=br.readLine();
					conn.disconnect();
					
					JSONObject json = new JSONObject(weather);
					JSONObject main = json.getJSONObject("main");
						double temp=main.getDouble("temp")-273.2;
						double humidity=main.getDouble("humidity");
						
					JSONArray weath=json.getJSONArray("weather");
						JSONObject witem=weath.getJSONObject(0);
						String condition=witem.getString("main");
						String icon=witem.getString("icon");
						
						//this.txtTemp.setText(temp+" °C");
						this.txtTemp.setText(String.format("%.4f",temp)+" °C");
						this.txtHumidity.setText(humidity+" %");
						this.cond.setText(condition);
						
						url=new URL("http://openweathermap.org/img/w/"+icon+".png");
						conn=(HttpURLConnection) url.openConnection();
						InputStream is = conn.getInputStream();
						Bitmap bm = BitmapFactory.decodeStream(is);
						is.close();
						conn.disconnect();
						
						this.iv.setImageBitmap(bm);
						
				} catch (MalformedURLException e) {
					Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else Toast.makeText(this, "Please Fill the city, country field", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button2:
			this.txtCity.setText("");
			this.txtTemp.setText("");
			this.txtHumidity.setText("");
			this.txtCity.requestFocus();
		}
		
	}
    
}

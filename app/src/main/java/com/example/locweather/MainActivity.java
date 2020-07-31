package com.example.locweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etLocation;
    Button btnFind;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLocation = findViewById(R.id.etLocation);
        btnFind = findViewById(R.id.btnFind);
        tvResult = findViewById(R.id.tvResult);


        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = etLocation.getText().toString();
                if(loc.length()==0)
                {
                    etLocation.setError("location is empty");
                    etLocation.requestFocus();
                    return;
                }
                String a1 = "http://api.openweathermap.org/data/2.5/weather?units=metric";
                String a2 = "&q="+loc;
                String a3 = "&appid=c6e315d09197cec231495138183954bd";
                MyTask t1 = new MyTask();
                t1.execute(a1+a2+a3);
            }
        });
    }

    class MyTask extends AsyncTask<String,Void,Double>
    {
        double temperature;
        String jsonStr = "";
        String line = "";
        String searchResult = "";



        @Override
        protected Double doInBackground(String... strings) {
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
                while ((line = reader.readLine())!= null)
                    jsonStr += line + "\n";
                if(jsonStr != null)
                {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject quote = jsonObject.getJSONObject("main");
                    temperature = quote.getDouble("temp");
                }

            }
            catch (Exception e )
            {
                e.printStackTrace();
            }
            return  temperature;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            tvResult.setText("temp = "+aDouble);
        }
    }
}

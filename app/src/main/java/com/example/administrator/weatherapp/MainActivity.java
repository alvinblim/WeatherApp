package com.example.administrator.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
        tvLocation =(TextView) findViewById(R.id.location);
        tvTemperature = (TextView)findViewById(R.id.temperature);
        tvHumidity = (TextView)findViewById(R.id.humidity);
        tvWindSpeed = (TextView)findViewById(R.id.wind_speed);
        tvCloudiness = (TextView)findViewById(R.id.cloudiness);
        btnRefresh = (Button)findViewById(R.id.btn_Refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new WeatherDataRetrival().execute();

            }

        });
        ivIcon = (ImageView)findViewById(R.id.icon);

    }

    private TextView tvLocation,tvTemperature,tvHumidity,tvWindSpeed,tvCloudiness;
    private TextView btnRefresh;
    private ImageView ivIcon;
private static final String WEATHER_SOURCE = "http://api.openweathermap.org/data/2.5/weather?APPID=82445b6c96b99bc3ffb78a4c0e17fca5&mode=json&id=1735161";

    private class WeatherDataRetrival extends AsyncTask< Void ,Integer,String>
    {
        @Override
       protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... arg0)
        {
            NetworkInfo networkInfo =((ConnectivityManager)MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected())
            {
                try {
                    URL url = new URL(WEATHER_SOURCE);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if(responseCode==HttpURLConnection.HTTP_OK)
                    {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        if(bufferedReader!=null)
                        {
                            String readline;
                            StringBuffer strBuffer = new StringBuffer();
                            while ((readline=bufferedReader.readLine())!=null)
                            {
                                strBuffer.append(readline);

                            }
                            return strBuffer.toString();

                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {

            }


            return null;
        }


        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try {
                if(result!=null)
                {
                    final JSONObject weatherJSON = new JSONObject(result);
    tvLocation.setText(weatherJSON.getString("name")+","+weatherJSON.getJSONObject("sys").getString("country"));
         tvWindSpeed.setText(String.valueOf(weatherJSON.getJSONObject("wind").getDouble("speed"))+"mps");
                    tvCloudiness.setText(String.valueOf(weatherJSON.getJSONObject("clouds").getInt("all"))+"%");
                    final JSONObject mainJSON = weatherJSON.getJSONObject("main");
                    tvTemperature.setText(String.valueOf(mainJSON.getDouble("temp")-273.15));
                    tvHumidity.setText(String.valueOf(mainJSON.getInt("humidity"))+"%");
    final JSONArray weatherJSONArray = weatherJSON.getJSONArray("weather");
                    if(weatherJSONArray.length()>0)
                    {
                        int code = weatherJSONArray.getJSONObject(0).getInt("id");
                       ivIcon.setImageResource(getIcon(code));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Integer getIcon(int code) {
        //return 1;
       // switch (code)
       //{
           // case 200:
           // case 201:

             //  return R.drawable.ic_thunderstorm_large;

           // case 800:
             //   return R.drawable.ic_day_clear_large;

             //   default:
               //     return 0;

       // }
        if (code>=200 && code<=250)
        {
            return R.drawable.ic_thunderstorm_large;
        } else if (code>=300 && code<=321)
        {
            return R.drawable.ic_drizzle_large;

        }else if (code >=500 && code<=531)
        {
            return  R.drawable.ic_rain_large;

        }else if(code>=600&& code<=622)
        {
            return R.drawable.ic_snow_large;
        }else if (code==800)
        {
            return R.drawable.ic_day_clear_large;
                    }else if(code==801)
                    {
                        return R.drawable.ic_day_few_clouds_large;
    }else if (code==802)
    {
        return R.drawable.ic_scattered_clouds_large;

    }else if (code>=803 &&code<=804)
    {
        return R.drawable.ic_broken_clouds_large;
    }else if(code>=701 && code <=762)
    {
        return R.drawable.ic_tornado_large;
    }else if(code>=781 && code <=900)
    {
        return R.drawable.ic_tornado_large;
    }else if(code==905)
    {
        return R.drawable.ic_windy_large;
    }else if(code==906)
    {
        return  R.drawable.ic_hail_large;
    }
     return 0;


    }
}

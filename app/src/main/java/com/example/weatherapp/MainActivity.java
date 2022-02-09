package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText eT;
    Button go;
        private final String url = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "328fb6e04cbf27cbaf0310a59bc6211a";
    TextView tV1,tV2, tV3, tvt1, tvt2, tvt3;//text view temperature
    ImageView iv1, iv2, iv3;
    ArrayList<String> arrayList = new ArrayList<>();


    RecyclerView rV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eT = findViewById(R.id.location);
        go = findViewById(R.id.go);
        tV1 = findViewById(R.id.textView1);
        tV2 = findViewById(R.id.textView2);
        tV3 = findViewById(R.id.textView3);
        tvt1 = findViewById(R.id.textTemp1);
        tvt2 = findViewById(R.id.textTemp2);
        tvt3 = findViewById(R.id.textTemp3);
        iv1 = findViewById(R.id.imageView1);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);

    }

    public void getForecast(View view) {
        String url2 = "";
        String zip = eT.getText().toString().trim();//trim deletes trailing spaces
        if (zip.equals("")){
            Toast.makeText(getApplicationContext(),"City Cannot be empty",Toast.LENGTH_SHORT ).show();
        }
        else{
            url2 = url + "?zip=" + zip + "&appid="+appid;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                //clear arraylist
                arrayList.clear();

                String forecast = "";

                try{

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject cityObj = jsonObject.getJSONObject("city");
                    String cityName = cityObj.getString("name");

                    JSONArray list = jsonObject.getJSONArray("list");
                    JSONObject firstObj = list.getJSONObject(0);
                    JSONObject secondObj = list.getJSONObject(1);
                    JSONObject thirdObj = list.getJSONObject(2);
                    JSONObject main1 = firstObj.getJSONObject("main");
                    JSONObject main2 = secondObj.getJSONObject("main");
                    JSONObject main3 = thirdObj.getJSONObject("main");

                    double temp1 = main1.getInt("temp");
                    double temp2 = main2.getInt("temp");
                    double temp3 = main3.getInt("temp");

                    temp1 = (((temp1 - 273.15)*9)/5)+32;
                    temp2 = (((temp2 - 273.15)*9)/5)+32;
                    temp3 = (((temp3 - 273.15)*9)/5)+32;

                    JSONArray weatherArray1 = firstObj.getJSONArray("weather");
                    JSONArray weatherArray2 = secondObj.getJSONArray("weather");
                    JSONArray weatherArray3 = thirdObj.getJSONArray("weather");

                    JSONObject weatherObj1 = weatherArray1.getJSONObject(0);
                    JSONObject weatherObj2 = weatherArray2.getJSONObject(0);
                    JSONObject weatherObj3 = weatherArray3.getJSONObject(0);

                    String mainWeather1 = weatherObj1.getString("main");//weather-main
                    String description1= weatherObj1.getString("description");
                    String mainWeather2 = weatherObj2.getString("main");//weather-main
                    String description2= weatherObj2.getString("description");
                    String mainWeather3 = weatherObj3.getString("main");//weather-main
                    String description3= weatherObj3.getString("description");



                    //determine the dates
                    Date date1 = new Date();
                    SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
                    String dateStr1 = format1.format(date1);

                    Date date2 = new Date(new Date().getTime() + 86400000);
                    SimpleDateFormat format2 = new SimpleDateFormat("MM-dd-yyyy");
                    String dateStr2 = format2.format(date2);

                    Date date3 = new Date(new Date().getTime() + 2*86400000);
                    SimpleDateFormat format3 = new SimpleDateFormat("MM-dd-yyyy");
                    String dateStr3 = format3.format(date3);


                    arrayList.add("City - " + cityName);
                    arrayList.add("          " +dateStr1 + "          " + description1);
                    arrayList.add("          " +dateStr2 + "          " + description2);
                    arrayList.add("          " +dateStr3 + "          " + description3);

                    rV = findViewById(R.id.id_recycler);
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), arrayList);

                    //add a divider to the recyclerview
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
                    rV.addItemDecoration(dividerItemDecoration);

                    //set the custom adapter
                    rV.setAdapter(adapter);
                    rV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



                    tV1.setText(arrayList.get(1).trim());
                    tV2.setText(arrayList.get(2).trim());
                    tV3.setText(arrayList.get(3).trim());

                    ArrayList<ImageView> imgList = new ArrayList<>();
                    imgList.add(iv1);
                    imgList.add(iv2);
                    imgList.add(iv3);
                    for (int  i = 0; i < imgList.size(); i++){
                        if (arrayList.get(i+1).contains("clear")){
                            imgList.get(i).setImageResource(R.drawable.clearsky);
                        }
                        else if (arrayList.get(i+1).contains("few")){
                            imgList.get(i).setImageResource(R.drawable.fewclouds);
                        }
                        else if (arrayList.get(i+1).contains("scattered")){
                            imgList.get(i).setImageResource(R.drawable.clouds);
                        }
                        else if (arrayList.get(i+1).contains("broken")){
                            imgList.get(i).setImageResource(R.drawable.broken);
                        }
                        else if (arrayList.get(i+1).contains("rain")){
                            imgList.get(i).setImageResource(R.drawable.rain);
                        }
                        else if (arrayList.get(i+1).contains("snow")){
                            imgList.get(i).setImageResource(R.drawable.snow);
                        }
                    }
                    int a=(int)Math.round(temp1);
                    tvt1.setText("     " + (int)Math.round(temp1) + "F");
                    tvt2.setText("     " + (int)Math.round(temp2) + "F");
                    tvt3.setText("     " + (int)Math.round(temp3) + "F");


//                    Toast.makeText(getApplicationContext(),  String.valueOf(dt) , Toast.LENGTH_SHORT).show();

                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
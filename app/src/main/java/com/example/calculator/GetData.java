package com.example.calculator;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.calculator.databinding.ActivityConvertBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GetData {

    public static StringRequest getSymbols(ArrayList<String> symbols, ActivityConvertBinding binding){

        String url = "http://data.fixer.io/api/symbols?access_key=c56f01d5a419f780789b14ae85df2d79";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Log.d("jsonObject","jsonObject === " + jsonObject);
                    JSONObject symbolsObject = jsonObject.getJSONObject("symbols");
                    JSONArray keys = symbolsObject.names();

                    if (keys != null) {
                        for (int i = 0; i < keys.length(); i++) {
                            String key = keys.getString(i);
                            symbols.add(key);
                        }
                    }

                    binding.progressBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return request;
    }

    public static StringRequest convert(String from, String to, String amount, ActivityConvertBinding binding, Context applicationContext){
        String url = "http://data.fixer.io/api/latest?access_key=c56f01d5a419f780789b14ae85df2d79"
                + "&base=" + from + "&symbols=" + to;

        StringRequest request =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Log.d("jsonObject","jsonObject === " + jsonObject);


                    if (jsonObject.getBoolean("success")){

                        long timestamp = jsonObject.getLong("timestamp");
                        Log.d("time",  "Timestamp = " + timestamp);
                        Date date = new Date(timestamp*1000);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.getDefault());
                        String formattedTime = dateFormat.format(date);
                        Log.d("time",  "Date = " + formattedTime);
                        binding.info.setText("Data provided by fixer, updated on " + formattedTime);

                        double rate = jsonObject.getJSONObject("rates").getDouble(to);
                        Log.d("rate","rate is == " + rate);
                        double convertedAmount = Double.parseDouble(amount) * rate;
                        binding.convertedAmount.setText(String.format("%.2f", convertedAmount));

                    }else{
                        Toast.makeText(applicationContext,"The current subscription plan does not support this API endpoint! "
                                + jsonObject.getJSONObject("error").getString("type"),Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        return request;
    }
}

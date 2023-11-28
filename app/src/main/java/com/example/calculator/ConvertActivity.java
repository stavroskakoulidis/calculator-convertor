package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.calculator.databinding.ActivityConvertBinding;

import java.util.ArrayList;

public class ConvertActivity extends AppCompatActivity {

    private ActivityConvertBinding binding;
    private ArrayList<String> symbols = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConvertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        if (savedInstanceState == null){
            StringRequest symbolsRequest = GetData.getSymbols(symbols,binding);
            queue.add(symbolsRequest);
            adapter = new ArrayAdapter<String>(this, R.layout.list_item, symbols);
            binding.from.setAdapter(adapter);
            binding.to.setAdapter(adapter);
        }

        binding.amountToConvertEditTxt.setText(intent.getStringExtra("Result"));

        binding.convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String from = binding.from.getText().toString();
                String to = binding.to.getText().toString();
                String amount = binding.amountToConvertEditTxt.getText().toString();

                if (!from.isEmpty() && !to.isEmpty() && !amount.isEmpty()){
                    StringRequest convertRequest = GetData.convert(from, to, amount, binding, getApplicationContext());
                    queue.add(convertRequest);
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong Input!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (savedInstanceState != null){
            binding.convertedAmount.setText(savedInstanceState.getString("converted"));
            binding.info.setText(savedInstanceState.getString("info"));
            binding.progressBar.setVisibility(View.INVISIBLE);
            symbols = savedInstanceState.getStringArrayList("symbols");
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("converted", binding.convertedAmount.getText().toString());
        outState.putString("info",binding.info.getText().toString());
        outState.putStringArrayList("symbols", symbols);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, savedInstanceState.getStringArrayList("symbols"));
        binding.from.setAdapter(adapter);
        binding.to.setAdapter(adapter);
    }
}
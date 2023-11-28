package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.calculator.databinding.ActivityMainBinding;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();

        if (savedInstanceState != null){
            binding.textResult.setText(savedInstanceState.getString("result"));
            binding.textOperation.setText(savedInstanceState.getString("operation"));
        }

    }

    private void setListeners(){

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button buttonClicked = (Button) view;
                String buttonText = buttonClicked.getText().toString();
                String expressionToCalculate = binding.textOperation.getText().toString();
                binding.textResult.setVisibility(View.VISIBLE);


                if (buttonText.equals("C")){
                    binding.textOperation.setText("");
                    binding.textResult.setText("");
                    return;
                }

                if (buttonText.equals("=")){
                    binding.textOperation.setText(binding.textResult.getText());
                    //binding.textResult.setText("");
                    binding.textResult.setVisibility(View.INVISIBLE);
                    return;
                }


                if (buttonText.equals("⌫")){
                    if (!expressionToCalculate.isEmpty()){
                        expressionToCalculate = expressionToCalculate.substring(0, expressionToCalculate.length()-1);
                    }else {
                        return;
                    }
                }else{
                    expressionToCalculate += buttonText;
                }

                binding.textOperation.setText(expressionToCalculate);

                expressionToCalculate = expressionToCalculate.replace("÷","/");
                expressionToCalculate = expressionToCalculate.replace("×","*");

                String result = evaluateExpression(expressionToCalculate);

                if (!result.equals("Error")){
                    binding.textResult.setText(result);
                }
            }
        };

        binding.buttonC.setOnClickListener(listener);
        binding.buttonPercent.setOnClickListener(listener);
        binding.buttonBackspace.setOnClickListener(listener);
        binding.buttonDiv.setOnClickListener(listener);
        binding.button7.setOnClickListener(listener);
        binding.button8.setOnClickListener(listener);
        binding.button9.setOnClickListener(listener);
        binding.buttonMultiply.setOnClickListener(listener);
        binding.button4.setOnClickListener(listener);
        binding.button5.setOnClickListener(listener);
        binding.button6.setOnClickListener(listener);
        binding.buttonSubtraction.setOnClickListener(listener);
        binding.button1.setOnClickListener(listener);
        binding.button2.setOnClickListener(listener);
        binding.button3.setOnClickListener(listener);
        binding.buttonAddition.setOnClickListener(listener);
        binding.button00.setOnClickListener(listener);
        binding.button0.setOnClickListener(listener);
        binding.buttonDecimal.setOnClickListener(listener);
        binding.buttonEquals.setOnClickListener(listener);

        binding.convertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConvertActivity.class);
                intent.putExtra("Result",binding.textResult.getText().toString());
                startActivity(intent);
            }
        });

    }

    private String evaluateExpression(String expression){

        try {
            Expression e = new ExpressionBuilder(expression).build();
            String result = String.format("%.2f", e.evaluate());
            if (result.endsWith(".00")){
                result = result.replace(".00","");
            }
            return result;
        }catch (Exception e){
            return "Error";
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("operation", binding.textOperation.getText().toString());
        outState.putString("result", binding.textResult.getText().toString());
    }
}
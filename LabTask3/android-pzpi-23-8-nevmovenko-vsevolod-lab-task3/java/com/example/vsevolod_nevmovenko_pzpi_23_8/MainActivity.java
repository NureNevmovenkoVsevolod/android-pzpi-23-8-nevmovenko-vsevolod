package com.example.lab3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private String currentInput = "";
    private boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = findViewById(R.id.resultText);

        // Кнопки цифр та операторів
        setupButtonClickListener(R.id.button0, "0");
        setupButtonClickListener(R.id.button1, "1");
        setupButtonClickListener(R.id.button2, "2");
        setupButtonClickListener(R.id.button3, "3");
        setupButtonClickListener(R.id.button4, "4");
        setupButtonClickListener(R.id.button5, "5");
        setupButtonClickListener(R.id.button6, "6");
        setupButtonClickListener(R.id.button7, "7");
        setupButtonClickListener(R.id.button8, "8");
        setupButtonClickListener(R.id.button9, "9");

        setupOperatorClickListener(R.id.buttonAdd, "+");
        setupOperatorClickListener(R.id.buttonSubtract, "-");
        setupOperatorClickListener(R.id.buttonMultiply, "*");
        setupOperatorClickListener(R.id.buttonDivide, "/");

        // Кнопка для очищення
        Button buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(v -> clear());

        // Кнопка для рівно
        Button buttonEquals = findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(v -> calculate());
    }

    private void setupButtonClickListener(int buttonId, String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (isNewInput) {
                currentInput = value;
                isNewInput = false;
            } else {
                currentInput += value; 
            }
            resultText.setText(currentInput); 
        });
    }

    private void setupOperatorClickListener(int buttonId, String operatorValue) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (!currentInput.isEmpty()) {
                currentInput += operatorValue;
                resultText.setText(currentInput);
                isNewInput = false;
            }
        });
    }

    private void calculate() {
        if (!currentInput.isEmpty()) {
            try {
                double result = evaluateExpression(currentInput);
                resultText.setText(String.valueOf(result));
                currentInput = String.valueOf(result); 
                isNewInput = true;
            } catch (Exception e) {
                resultText.setText("Error");
            }
        }
    }

    private double evaluateExpression(String expression) {
        String[] tokens = expression.split("(?=[-+*/])|(?<=[-+*/])");
        double result = Double.parseDouble(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            double num = Double.parseDouble(tokens[i + 1]);
            switch (operator) {
                case "+":
                    result += num;
                    break;
                case "-":
                    result -= num;
                    break;
                case "*":
                    result *= num;
                    break;
                case "/":
                    if (num != 0) {
                        result /= num;
                    } else {
                        throw new ArithmeticException("Divide by zero");
                    }
                    break;
            }
        }
        return result;
    }

    private void clear() {
        currentInput = "";
        resultText.setText("0");
        isNewInput = true;
    }
}

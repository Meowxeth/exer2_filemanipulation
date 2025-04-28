package com.example.april12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText editPlayer;
    EditText editScore;
    TextView textViewFile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Scanner scanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editPlayer = findViewById(R.id.editTextDrink);
        editScore = findViewById(R.id.editTextPrice);
        sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        textViewFile = findViewById(R.id.textViewFile);

        scanner = new Scanner(getResources().openRawResource(R.raw.vending_desc));
        StringBuilder sb = new StringBuilder();
        while(scanner.hasNextLine()){
            sb.append(scanner.nextLine());
            sb.append("\n");
        }
        textViewFile.setText(sb.toString());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void goDesc(View view){
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
    }

    public void changePrice(View view){
        try {
            editor = sharedPreferences.edit();
            editor.putString("Drink", editPlayer.getEditableText().toString());
            editor.putInt("Price", Integer.parseInt(editScore.getEditableText().toString()));
            editor.commit();
        } catch (Exception e) {
            if (e instanceof NumberFormatException){
                Toast.makeText(this, "Please buy first!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

}

    public void getDrink(View view){
        editPlayer.setText(sharedPreferences.getString("Drink", ""));
        editScore.setText(String.valueOf(sharedPreferences.getInt("Price", 0)));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("Drink", editPlayer.getEditableText().toString());
        outState.putInt("Price", Integer.parseInt(editScore.getEditableText().toString()));

    }


    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if(savedInstanceState != null){
            editScore.setText(savedInstanceState.getString("Drink"));
            editPlayer.setText(String.valueOf(savedInstanceState.getInt("Price", 0)));
        }
    }
}
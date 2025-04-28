package com.example.april12;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class NextActivity extends AppCompatActivity {

    Scanner scanner;
    EditText editTextFileName;
    EditText editTextContent;
    String state;
    File myFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_next);
        editTextFileName = findViewById(R.id.editTextFileName);
        editTextContent = findViewById(R.id.editTextContent);
        state = Environment.getExternalStorageState();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void doSave(View view){
        if(state.equals(Environment.MEDIA_MOUNTED)){
            myFile = new File(getExternalFilesDir(null), editTextFileName.getEditableText().toString());
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(myFile);
                fileWriter.write(editTextContent.getEditableText().toString());
                fileWriter.flush();
                fileWriter.close();
                Toast.makeText(this, "File saved!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }

        } else{
            Toast.makeText(this, "Cannot fetch external storage!", Toast.LENGTH_LONG).show();
        }
    }

    public void doLoad(View view){
        if(state.equals(Environment.MEDIA_MOUNTED)){
            try {
                scanner = new Scanner(new File(getExternalFilesDir(null), editTextFileName.getEditableText().toString()));
                StringBuilder sb = new StringBuilder();
                while(scanner.hasNextLine()){
                    sb.append(scanner.nextLine());
                    sb.append("\n");
                }
                editTextContent.setText(sb.toString());
            } catch (IOException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(this, "Cannot fetch external storage!", Toast.LENGTH_LONG).show();
        }
    }

    public void goBack(View view){
        finish();
    }

    public void listProducts(View view) {
        File directory = getExternalFilesDir(null);
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                StringBuilder productList = new StringBuilder("Products:\n");
                for (File file : files) {
                    productList.append("- ").append(file.getName()).append("\n");
                }
                editTextContent.setText(productList);
            } else {
                Toast.makeText(this, "No products found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Storage not available!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteProduct(View view) {
        File directory = getExternalFilesDir(null);
        if (directory != null) {
            File fileToDelete = new File(directory, editTextFileName.getEditableText().toString());
            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                    Toast.makeText(this, "Product deleted!", Toast.LENGTH_SHORT).show();
                    listProducts(view);
                } else {
                    Toast.makeText(this, "Failed to delete product!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Product not found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
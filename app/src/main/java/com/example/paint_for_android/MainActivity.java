package com.example.paint_for_android;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private PaintView paintView;
    private static final int REQUEST_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paintView = findViewById(R.id.paintView);

        Button buttonClear = findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clear();
            }
        });

        Button buttonUndo = findViewById(R.id.buttonUndo);
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.undo();
            }
        });



        Button buttonColor = findViewById(R.id.buttonColor);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectColor();
            }
        });

        Button buttonBrushSize = findViewById(R.id.buttonBrushSize);
        buttonBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBrushSize();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }
    }

    private void saveDrawing() {
        Bitmap bitmap = paintView.save();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/paint.png");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "Drawing saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save drawing!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectColor() {
        final String[] colors = {"Negro", "Rojo", "Verde", "Azul", "Amarillo","Cyan","Blanco"};
        final int[] colorValues = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.WHITE};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Color");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                paintView.setColor(colorValues[which]);
            }
        });
        builder.show();
    }

    private void selectBrushSize() {
        final String[] sizes = {"Small", "Medium", "Large"};
        final float[] sizeValues = {5f, 12f, 20f}; // Valores de tamaño de pincel correspondientes a las opciones

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Brush Size");
        builder.setItems(sizes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                paintView.setBrushSize(sizeValues[which]);
            }
        });
        builder.show();
    }}

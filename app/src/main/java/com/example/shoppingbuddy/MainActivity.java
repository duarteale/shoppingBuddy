package com.example.shoppingbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btNuevo, btHistorial, btProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botones del layout
        btNuevo = findViewById(R.id.btNuevo);
        btHistorial = findViewById(R.id.btHistorial);
        btProductos = findViewById(R.id.btProductos);

        // Acciones (por ahora vacías)
        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevo = new Intent(getApplicationContext(), Nuevo.class);
                startActivity(nuevo);
            }
        });

        btHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historial = new Intent(getApplicationContext(), Historial.class);
                startActivity(historial);
            }
        });

        btProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productos = new Intent(getApplicationContext(), Productos.class);
                startActivity(productos);
            }
        });
    }
}

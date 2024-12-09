package com.example.shoppingbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Historial extends AppCompatActivity {

    private RecyclerView rvHistorial;
    private AdaptadorHistorial historialAdapter;
    private List<String> historialCompras;
    private SQLiteHelperDB dbHelper;  // Instancia de SQLiteHelperDB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Configuración de la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_historial);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.volver);
        toolbar.setNavigationOnClickListener(view -> finish());

        // Inicializar el RecyclerView
        rvHistorial = findViewById(R.id.rvHistorial);
        rvHistorial.setLayoutManager(new LinearLayoutManager(this)); // Uso de LinearLayoutManager

        // Instanciar SQLiteHelperDB
        dbHelper = new SQLiteHelperDB(this);

        // Obtener el historial de compras desde la base de datos
        historialCompras = dbHelper.obtenerHistorialCompras();

        // Configurar el adaptador para el RecyclerView
        historialAdapter = new AdaptadorHistorial(historialCompras);
        rvHistorial.setAdapter(historialAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Acción al pulsar la flecha de retroceso en la Toolbar
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Historial.this, MainActivity.class);  // Navegar de vuelta a MainActivity
        startActivity(intent);
    }
}

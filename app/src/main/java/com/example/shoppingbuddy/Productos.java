package com.example.shoppingbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

public class Productos extends AppCompatActivity {

    private EditText etBuscarProducto;
    private ListView lvProductos;
    private AdaptadorProductos adaptadorProductos;
    private SQLiteHelperDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Toolbar toolbar = findViewById(R.id.toolbar_productos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Productos");
        toolbar.setNavigationIcon(R.drawable.volver);
        toolbar.setNavigationOnClickListener(view -> finish());

        etBuscarProducto = findViewById(R.id.et_buscar_producto);
        lvProductos = findViewById(R.id.lv_productos);

        db = new SQLiteHelperDB(this);

        adaptadorProductos = new AdaptadorProductos(this, db.obtenerTodosLosProductos());
        lvProductos.setAdapter(adaptadorProductos);

        etBuscarProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptadorProductos.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
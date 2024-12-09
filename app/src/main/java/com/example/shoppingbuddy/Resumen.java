package com.example.shoppingbuddy;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

        import java.util.List;

public class Resumen extends AppCompatActivity {

    private TextView tvFecha;
    private TextView tvTotal;
    private RecyclerView rvProductos;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        // Obtener la compra seleccionada
        String compraSeleccionada = getIntent().getStringExtra("compra");

        // Mostrar la información de la compra
        tvFecha = findViewById(R.id.tvFecha);
        tvTotal = findViewById(R.id.tvTotal);
        rvProductos = findViewById(R.id.rvProductos);
        btnVolver = findViewById(R.id.btnVolver);

        Toolbar toolbar = findViewById(R.id.toolbar_resumen_compra);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.volver);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Obtener el ID de la compra
        int compraId = -1;
        SQLiteHelperDB dbHelper = new SQLiteHelperDB(this);
        List<String> historialCompras = dbHelper.obtenerHistorialCompras();
        for (String compra : historialCompras) {
            if (compra.contains(compraSeleccionada)) {
                String[] partes = compra.split(" - ");
                compraId = Integer.parseInt(partes[0]);
                break;
            }
        }

        // Obtener la información de la compra desde la base de datos
        Compra compra = dbHelper.obtenerCompra(compraId);

        // Mostrar la información de la compra
        tvFecha.setText(compra.getFecha());
        tvTotal.setText(String.valueOf(dbHelper.obtenerTotalDeCompra(compraId)));

        // Mostrar los productos de la compra
        List<Producto> productos = dbHelper.obtenerProductosDeCompra(compraId);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        AdaptadorProducto adaptador = new AdaptadorProducto(productos);
        rvProductos.setAdapter(adaptador);

        // Agregar listener al botón de volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
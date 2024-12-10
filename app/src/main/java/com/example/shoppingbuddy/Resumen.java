package com.example.shoppingbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Resumen extends AppCompatActivity {

    private TextView tvNombreCompra;
    private TextView tvFecha;
    private TextView tvTotal;
    private RecyclerView rvProductos;
    private Button btnVolver;
    private String nombreCompra;
    private String fecha;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        Log.d("********", "Actividad Resumen creada");
        try {

            // Obtener la compra seleccionada
            nombreCompra = getIntent().getStringExtra("compra");
            Log.d("********", "Compra seleccionada: " + nombreCompra);

            // Mostrar la información de la compra
            tvNombreCompra = findViewById(R.id.tvNombreCompra);
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
            SQLiteHelperDB dbHelper = new SQLiteHelperDB(this);
            int compraId = dbHelper.obtenerIdCompra(nombreCompra);
            Log.d("********", "ID de la compra: " + compraId);

            if (compraId == -1) {
                Log.e("********", "La compra no se encuentra en la base de datos");
                Toast.makeText(this, "La compra no se encuentra en la base de datos", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Obtener la información de la compra desde la base de datos
            Compra compra = dbHelper.obtenerCompra(compraId);
            if (compra == null) {
                Log.e("********", "Error al obtener la compra");
                Toast.makeText(this, "Error al obtener la compra", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Obtener la fecha y el total de la compra
            fecha = compra.getFecha();
            total = dbHelper.obtenerTotalDeCompra(compraId);

            // Mostrar la información de la compra
            String nombreCompraTexto = "Nombre de compra: ";
            String nombreCompraValor = nombreCompra.split(" - ")[0];
            SpannableString nombreCompraSpannable = new SpannableString(nombreCompraTexto + nombreCompraValor);
            nombreCompraSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.celeste)), 0, nombreCompraTexto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nombreCompraSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, nombreCompraTexto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nombreCompraSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), nombreCompraTexto.length(), nombreCompraSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvNombreCompra.setText(nombreCompraSpannable);

            String fechaTexto = "Fecha y hora: ";
            String fechaValor = fecha;
            SpannableString fechaSpannable = new SpannableString(fechaTexto + fechaValor);
            fechaSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.celeste)), 0, fechaTexto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            fechaSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, fechaTexto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            fechaSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), fechaTexto.length(), fechaSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvFecha.setText(fechaSpannable);

            String totalTexto = "TOTAL: ";
            String totalValor = "$" + String.format("%.2f", total);
            SpannableString totalSpannable = new SpannableString(totalTexto + totalValor);
            totalSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.celeste)), 0, totalTexto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            totalSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), totalTexto.length(), (totalTexto + totalValor).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            totalSpannable.setSpan(new StyleSpan(Typeface.BOLD), 0, totalTexto.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTotal.setText(totalSpannable);

            // Mostrar los productos de la compra
            List<Producto> productos = dbHelper.obtenerProductosDeCompra(compraId);
            rvProductos.setLayoutManager(new LinearLayoutManager(this));
            AdaptadorProducto adaptador = new AdaptadorProducto(productos);
            rvProductos.setAdapter(adaptador);
            Log.d("********", "Productos de la compra mostrados");

            // Agregar listener al botón de volver
            btnVolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e("********", "Error al crear la actividad Resumen", e);
        }
    }
}
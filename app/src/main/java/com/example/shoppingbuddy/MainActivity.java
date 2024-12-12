package com.example.shoppingbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
                mostrarDialogoTipoCompra();
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
    // Método para mostrar el diálogo de tipo de compra
    private void mostrarDialogoTipoCompra() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String titulo = "Tipo de Compra";
        SpannableString spannableString = new SpannableString(titulo);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.celeste)), 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setTitle(spannableString);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText etImporte = new EditText(this);
        etImporte.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etImporte.setHintTextColor(getResources().getColor(R.color.black));
        etImporte.setTextColor(getResources().getColor(R.color.black));
        etImporte.setHint("0.00");
        layout.addView(etImporte);

        Button btnConPresupuesto = new Button(this);
        btnConPresupuesto.setText("Con Presupuesto");
        btnConPresupuesto.setTextColor(getResources().getColor(R.color.black));
        btnConPresupuesto.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dorado)));

        btnConPresupuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String importe = etImporte.getText().toString();
                if (!importe.isEmpty()) {
                    double presupuesto = Double.parseDouble(importe.replace("$", ""));
                    Intent intent = new Intent(MainActivity.this, Presupuesto.class);
                    intent.putExtra("presupuesto", presupuesto);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Ingrese un importe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(btnConPresupuesto);

        Button btnCompraLibre = new Button(this);
        btnCompraLibre.setText("Compra Libre");
        btnCompraLibre.setTextColor(getResources().getColor(R.color.black));
        btnCompraLibre.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dorado)));

        btnCompraLibre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Nuevo.class);
                startActivity(intent);
            }
        });
        layout.addView(btnCompraLibre);
        builder.setView(layout);
        AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        int ancho = (int) (getResources().getDisplayMetrics().widthPixels * 0.7);
        dialog.getWindow().setLayout(ancho, WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white))); // Cambia el color de fondo del diálogo
    }
}
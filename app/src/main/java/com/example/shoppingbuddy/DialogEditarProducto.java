package com.example.shoppingbuddy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class DialogEditarProducto extends Dialog {

    private Context context;
    private Producto producto;

    public DialogEditarProducto(Context context, Producto producto) {
        super(context);
        this.context = context;
        this.producto = producto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_editar_producto);

        EditText etNombre = findViewById(R.id.et_nombre);
        etNombre.setText(producto.getNombre());

        EditText etPrecio = findViewById(R.id.et_precio);
        etPrecio.setText(String.valueOf(producto.getPrecioUnitario()));

        Button btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            double precio = Double.parseDouble(etPrecio.getText().toString());

            producto.setNombre(nombre);
            producto.setPrecioUnitario(precio);

            SQLiteHelperDB db = new SQLiteHelperDB(context);
            db.actualizarProducto(producto);

            dismiss();
        });
    }
}
package com.example.shoppingbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public class Presupuesto extends AppCompatActivity {

    private Toolbar toolbar;
    private SQLiteHelperDB db;
    private RecyclerView rvProductos;
    private ArrayList<Producto> productosActuales = new ArrayList<>();
    private ArrayList<Producto> productosTemporales = new ArrayList<>();
    private AdaptadorProducto adaptadorProducto;
    private Spinner spinnerCantidad;
    private AutoCompleteTextView etBuscarProducto;
    private EditText etNombreProducto;
    private EditText etPrecioProducto;
    private TextView tvTotal;
    private Button btnAgregarProducto;
    private Button btnCancelar;
    private Button btnFinalizar;
    private TextView tvPresupuestoRestante;
    private double presupuestoInicial;
    private double totalActual;
    private List<String> historialCompras = new ArrayList<>();
    private List<Producto> listaProductos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        // Inicializar variables
        Intent intent = getIntent();
        presupuestoInicial = intent.getDoubleExtra("presupuesto", 0.0);
        totalActual = 0.0;

        // Inicializar elementos de la UI
        toolbar = findViewById(R.id.toolbar_presupuesto);
        rvProductos = findViewById(R.id.rvProductos);
        spinnerCantidad = findViewById(R.id.spinnerCantidad);
        etBuscarProducto = findViewById(R.id.etBuscarProducto);
        etNombreProducto = findViewById(R.id.etNombreProducto);
        etPrecioProducto = findViewById(R.id.etPrecioProducto);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnFinalizar = findViewById(R.id.btnFinalizar);
        tvTotal = findViewById(R.id.tvTotal);
        tvPresupuestoRestante = findViewById(R.id.tv_presupuesto_restante);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);

        // Inicializar la base de datos
        db = new SQLiteHelperDB(this);

        toolbar.setNavigationIcon(R.drawable.volver);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        adaptadorProducto = new AdaptadorProducto(productosActuales);
        rvProductos.setAdapter(adaptadorProducto);

        cargarSpinnerCantidad();

        btnAgregarProducto.setOnClickListener(view -> agregarProducto());
        btnCancelar.setOnClickListener(view -> finish());
        btnFinalizar.setOnClickListener(view -> mostrarDialogoGuardarCompra());

        etBuscarProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    etNombreProducto.setText("");
                    etPrecioProducto.setText("");
                } else {
                    buscarProductos();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void actualizarPresupuestoRestante() {
        double presupuestoRestante = presupuestoInicial - totalActual;

        if (presupuestoRestante >= 0) {
            tvPresupuestoRestante.setTextColor(getResources().getColor(R.color.verde));
        } else {
            tvPresupuestoRestante.setTextColor(getResources().getColor(R.color.rojo));
        }

        tvPresupuestoRestante.setText(String.format("$%.2f", presupuestoRestante));
    }

    private void cargarSpinnerCantidad() {
        List<String> cantidadOpciones = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            cantidadOpciones.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cantidadOpciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCantidad.setAdapter(adapter);
    }

    private void mostrarDialogoGuardarCompra() {
        final EditText input = new EditText(Presupuesto.this);
        input.setHint("Nombre de la compra");

        new AlertDialog.Builder(Presupuesto.this)
                .setTitle("Guardar como:")
                .setView(input)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombreCompra = input.getText().toString().trim();
                    if (nombreCompra.isEmpty()) {
                        Toast.makeText(Presupuesto.this, "Debe ingresar un nombre para la compra", Toast.LENGTH_SHORT).show();
                    } else if (historialCompras.contains(nombreCompra)) {
                        Toast.makeText(Presupuesto.this, "Ese nombre ya está en el historial", Toast.LENGTH_SHORT).show();
                    } else {
                        guardarCompraEnHistorial(nombreCompra, productosTemporales);
                        historialCompras.add(nombreCompra);
                        productosTemporales.clear();
                        adaptadorProducto.notifyDataSetChanged();
                        Toast.makeText(Presupuesto.this, "Compra guardada como " + nombreCompra, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Presupuesto.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarCompraEnHistorial(String nombreCompra, ArrayList<Producto> productos) {
        SQLiteHelperDB dbHelper = new SQLiteHelperDB(this);
        dbHelper.guardarCompra(nombreCompra, productos);
    }

    private void buscarProductos() {
        String nombre = etBuscarProducto.getText().toString();
        SQLiteHelperDB dbHelper = new SQLiteHelperDB(this);
        List<Producto> productos = dbHelper.buscarProductosPorNombre(nombre);

        if (productos.size() > 0) {
            // Autocompletar los campos con el primer producto encontrado
            Producto producto = productos.get(0);
            etNombreProducto.setText(producto.getNombre());
            etPrecioProducto.setText(String.valueOf(producto.getPrecioUnitario()));
            spinnerCantidad.setSelection(producto.getCantidad() - 1); // Seleccionar la cantidad
        } else {
            // No se encontraron productos, permitir agregar un nuevo producto
            etNombreProducto.setText(nombre);
            etPrecioProducto.setText("");
            spinnerCantidad.setSelection(0); // Seleccionar la cantidad 1 por defecto
        }
    }

    private void agregarProducto() {
        String nombreProducto = etNombreProducto.getText().toString().trim();
        String precioTexto = etPrecioProducto.getText().toString().trim();

        if (nombreProducto.isEmpty() || precioTexto.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int cantidadSeleccionada = Integer.parseInt(spinnerCantidad.getSelectedItem().toString());
            double precioProducto = Double.parseDouble(precioTexto);

            Producto productoExistente = db.buscarProductoPorNombre(nombreProducto);
            Producto productoAgregar = null;

            if (productoExistente != null) {
                double precioAnterior = productoExistente.getPrecioUnitario();
                double precioNuevo = Double.parseDouble(precioTexto);
                if (precioNuevo > precioAnterior) {
                    productoExistente.setIconoCambioPrecio(R.drawable.up_arrow);
                } else if (precioNuevo < precioAnterior) {
                    productoExistente.setIconoCambioPrecio(R.drawable.down_arrow);
                } else {
                    productoExistente.setIconoCambioPrecio(R.drawable.equal);
                }
                productoExistente.setPrecioUnitario(precioNuevo);
                productoExistente.setCantidad(cantidadSeleccionada);
                productoAgregar = productoExistente;
            } else {
                productoAgregar = new Producto(0, nombreProducto, cantidadSeleccionada, precioProducto, R.drawable.equal);
            }

            if (!productosTemporales.contains(productoAgregar)) {
                productosTemporales.add(productoAgregar);
                adaptadorProducto.actualizarProductos(productosTemporales);
                adaptadorProducto.notifyDataSetChanged();
            }

            double total = calcularTotal(productosTemporales);
            tvTotal.setText("TOTAL: $" + String.format("%.2f", total));
            double presupuestoRestante = presupuestoInicial - total;
            tvPresupuestoRestante.setText("$" + String.format("%.2f", presupuestoRestante));
            if (presupuestoRestante < 0) {
                tvPresupuestoRestante.setTextColor(getResources().getColor(R.color.rojo));
            } else {
                tvPresupuestoRestante.setTextColor(getResources().getColor(R.color.verde));
            }

            etNombreProducto.setText("");
            etPrecioProducto.setText("");

            Toast.makeText(this, "Producto agregado con éxito.", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingresa un precio válido.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error", "Error al agregar producto", e);
            Toast.makeText(this, "Error al agregar producto", Toast.LENGTH_SHORT).show();
        }
    }

    private double calcularTotal(ArrayList<Producto> productos) {
        double total = 0;
        for (Producto producto : productos) {
            total += producto.getPrecioUnitario() * producto.getCantidad();
        }
        return total;
    }
}
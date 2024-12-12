package com.example.shoppingbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SQLiteHelperDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "compras.db";
    private static final int DATABASE_VERSION = 1;

    // Tablas
    public static final String TABLE_COMPRAS = "compras";
    public static final String TABLE_PRODUCTOS = "productos";

    // Columnas de la tabla de compras
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_FECHA = "fecha";

    // Columnas de la tabla de productos
    public static final String COLUMN_PRODUCTO_ID = "producto_id";
    public static final String COLUMN_PRODUCTO_NOMBRE = "producto_nombre";
    public static final String COLUMN_PRODUCTO_PRECIO = "producto_precio";
    public static final String COLUMN_PRODUCTO_CANTIDAD = "producto_cantidad";
    public static final String COLUMN_COMPRA_ID = "compra_id";  // Relaciona productos con una compra
    public static final String COLUMN_ICONO_CAMBIO_PRECIO = "icono_cambio_precio";  // Nueva columna
    public static final String TABLE_PRODUCTO = "producto";

    public SQLiteHelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de compras
        String CREATE_TABLE_COMPRAS = "CREATE TABLE " + TABLE_COMPRAS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOMBRE + " TEXT,"
                + COLUMN_FECHA + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_COMPRAS);

        // Crear tabla de productos
        String CREATE_TABLE_PRODUCTOS = "CREATE TABLE " + TABLE_PRODUCTOS + "("
                + COLUMN_PRODUCTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PRODUCTO_NOMBRE + " TEXT,"
                + COLUMN_PRODUCTO_PRECIO + " REAL,"
                + COLUMN_PRODUCTO_CANTIDAD + " INTEGER,"
                + COLUMN_ICONO_CAMBIO_PRECIO + " INTEGER"  // Nueva columna
                + ")";
        db.execSQL(CREATE_TABLE_PRODUCTOS);

        // Crear tabla de productos de una compra
        String CREATE_TABLE_PRODUCTO = "CREATE TABLE " + TABLE_PRODUCTO + "("
                + COLUMN_COMPRA_ID + " INTEGER,"
                + COLUMN_PRODUCTO_NOMBRE + " TEXT,"
                + COLUMN_PRODUCTO_PRECIO + " REAL,"
                + COLUMN_PRODUCTO_CANTIDAD + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_COMPRA_ID + ") REFERENCES " + TABLE_COMPRAS + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_TABLE_PRODUCTO);
    }
    public void eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTOS, COLUMN_PRODUCTO_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void actualizarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTO_NOMBRE, producto.getNombre());
        values.put(COLUMN_PRODUCTO_PRECIO, producto.getPrecioUnitario());
        db.update(TABLE_PRODUCTOS, values, COLUMN_PRODUCTO_ID + " = ?", new String[]{String.valueOf(producto.getId())});
        db.close();
    }
    public Producto buscarProductoPorNombre(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTOS + " WHERE " + COLUMN_PRODUCTO_NOMBRE + " = ?";
        String[] args = new String[]{nombre};
        Cursor cursor = db.rawQuery(query, args);

        Producto producto = null;
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_ID);
            int nombreColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_NOMBRE);
            int precioColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_PRECIO);
            int cantidadColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_CANTIDAD);
            int iconoCambioPrecioColumnIndex = cursor.getColumnIndex(COLUMN_ICONO_CAMBIO_PRECIO);

            if (idColumnIndex != -1 && nombreColumnIndex != -1 && precioColumnIndex != -1 && cantidadColumnIndex != -1 && iconoCambioPrecioColumnIndex != -1) {
                int id = cursor.getInt(idColumnIndex);
                String nombreProducto = cursor.getString(nombreColumnIndex);
                double precio = cursor.getDouble(precioColumnIndex);
                int cantidad = cursor.getInt(cantidadColumnIndex);
                int iconoCambioPrecio = cursor.getInt(iconoCambioPrecioColumnIndex);
                producto = new Producto(id, nombreProducto, cantidad, precio, iconoCambioPrecio);
            } else {
                Log.e("Error", "Columnas no encontradas en el cursor");
            }
        }
        cursor.close();
        db.close();
        return producto;
    }

    public List<Producto> buscarProductosPorNombre(String nombre) {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTOS + " WHERE " + COLUMN_PRODUCTO_NOMBRE + " LIKE ?";
        String[] args = new String[]{"%" + nombre + "%"};
        Cursor cursor = db.rawQuery(query, args);

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_ID);
                int nombreColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_NOMBRE);
                int precioColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_PRECIO);
                int cantidadColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_CANTIDAD);
                int iconoCambioPrecioColumnIndex = cursor.getColumnIndex(COLUMN_ICONO_CAMBIO_PRECIO);

                if (idColumnIndex != -1 && nombreColumnIndex != -1 && precioColumnIndex != -1 && cantidadColumnIndex != -1 && iconoCambioPrecioColumnIndex != -1) {
                    int id = cursor.getInt(idColumnIndex);
                    String nombreProducto = cursor.getString(nombreColumnIndex);
                    double precio = cursor.getDouble(precioColumnIndex);
                    int cantidad = cursor.getInt(cantidadColumnIndex);
                    int iconoCambioPrecio = cursor.getInt(iconoCambioPrecioColumnIndex);
                    productos.add(new Producto(id, nombreProducto, cantidad, precio, iconoCambioPrecio));
                } else {
                    Log.e("Error", "Columnas no encontradas en el cursor");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productos;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }
    //Método para guardar producto en Nuevo
    public void guardarProducto(Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTO_NOMBRE, producto.getNombre());
        values.put(COLUMN_PRODUCTO_PRECIO, producto.getPrecioUnitario());
        values.put(COLUMN_PRODUCTO_CANTIDAD, producto.getCantidad());
        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }

    // Método para guardar una compra
    public long guardarCompra(String nombreCompra, ArrayList<Producto> productos) {

        Log.d("SQLiteHelperDB", "guardarCompra: Iniciando...");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("SQLiteHelperDB", "guardarCompra: Base de datos abierta");
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombreCompra);
        values.put(COLUMN_FECHA, getCurrentDateTime());  // Guardar la fecha actual
        Log.d("SQLiteHelperDB", "guardarCompra: Valores para la compra: " + values);

        long id = db.insert(TABLE_COMPRAS, null, values);
        Log.d("SQLiteHelperDB", "guardarCompra: ID de la compra: " + id);

        // Guardar los productos de la compra
        for (Producto producto : productos) {
            Log.d("SQLiteHelperDB", "guardarCompra: Producto: " + producto.getNombre());
            // Verificar si el producto ya existe en la tabla de productos
            if (productoExiste(db, producto.getNombre())) {
                Log.d("SQLiteHelperDB", "guardarCompra: Producto existe, actualizando precio...");
                actualizarProductoPrecio(db, producto.getNombre(), producto.getPrecioUnitario());
            } else {
                Log.d("SQLiteHelperDB", "guardarCompra: Producto no existe, creando nuevo...");
                ContentValues productoValues = new ContentValues();
                productoValues.put(COLUMN_PRODUCTO_NOMBRE, producto.getNombre());
                productoValues.put(COLUMN_PRODUCTO_PRECIO, producto.getPrecioUnitario());
                productoValues.put(COLUMN_PRODUCTO_CANTIDAD, 1); // Agregar cantidad 1
                long productoId = db.insert(TABLE_PRODUCTOS, null, productoValues);
                Log.d("SQLiteHelperDB", "guardarCompra: ID del producto: " + productoId);
            }

            // Guardar el producto en la tabla de productos de la compra
            ContentValues compraValues = new ContentValues();
            compraValues.put(COLUMN_COMPRA_ID, id);
            compraValues.put(COLUMN_PRODUCTO_NOMBRE, producto.getNombre());
            compraValues.put(COLUMN_PRODUCTO_PRECIO, producto.getPrecioUnitario());
            compraValues.put(COLUMN_PRODUCTO_CANTIDAD, 1); // Agregar cantidad 1
            long compraProductoId = db.insert(TABLE_PRODUCTO, null, compraValues);
            Log.d("SQLiteHelperDB", "guardarCompra: ID del producto en la compra: " + compraProductoId);
        }

        db.close();
        Log.d("SQLiteHelperDB", "guardarCompra: Base de datos cerrada");
        return id;
    }

    // Método para verificar si un producto existe
    public boolean productoExiste(SQLiteDatabase db, String nombre) {
        Log.d("SQLiteHelperDB", "productoExiste: Verificando si el producto existe...");
        String query = "SELECT * FROM " + TABLE_PRODUCTO + " WHERE " + COLUMN_PRODUCTO_NOMBRE + " = ?";
        String[] args = new String[]{nombre};
        Cursor cursor = db.rawQuery(query, args);
        boolean existe = cursor.moveToFirst();
        Log.d("SQLiteHelperDB", "productoExiste: ¿Existe el producto? " + existe);
        cursor.close();
        return existe;
    }

    // Método para actualizar el precio de un producto
    public void actualizarProductoPrecio(SQLiteDatabase db, String nombre, double precio) {
        Log.d("SQLiteHelperDB", "actualizarProductoPrecio: Actualizando el precio del producto...");
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTO_PRECIO, precio);
        db.update(TABLE_PRODUCTOS, values, COLUMN_PRODUCTO_NOMBRE + " = ?", new String[]{nombre});
        Log.d("SQLiteHelperDB", "actualizarProductoPrecio: Precio actualizado");
    }

    // Método para agregar un producto a una compra
    public void agregarProducto(int compraId, Producto producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTO_NOMBRE, producto.getNombre());
        values.put(COLUMN_PRODUCTO_PRECIO, producto.getPrecioUnitario());
        values.put(COLUMN_PRODUCTO_CANTIDAD, producto.getCantidad());
        values.put(COLUMN_COMPRA_ID, compraId);

        db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
    }
    public int obtenerIdCompra(String nombreCompra) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] partes = nombreCompra.split(" - ");
        String nombre = partes[0];
        Cursor cursor = db.query("compras", new String[]{"id"}, "nombre = ?", new String[]{nombre}, null, null, null);
        int idCompra = -1;
        if (cursor.moveToFirst()) {
            idCompra = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return idCompra;
    }

    // Método para obtener los productos de una compra
    public List<Producto> obtenerProductosDeCompra(int compraId) {
        Log.d("********", "Obteniendo productos de compra con ID " + compraId);

        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTO, null, COLUMN_COMPRA_ID + "=?", new String[]{String.valueOf(compraId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int nombreColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_NOMBRE);
                int precioColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_PRECIO);
                int cantidadColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_CANTIDAD);

                if (nombreColumnIndex != -1 && precioColumnIndex != -1 && cantidadColumnIndex != -1) {
                    String nombreProducto = cursor.getString(nombreColumnIndex);
                    double precio = cursor.getDouble(precioColumnIndex);
                    int cantidad = cursor.getInt(cantidadColumnIndex);
                    productos.add(new Producto(0, nombreProducto, cantidad, precio, 0));
                } else {
                    Log.e("Error", "Columnas no encontradas en el cursor");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productos;
    }

    public double obtenerTotalDeCompra(int compraId) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTO, new String[]{COLUMN_PRODUCTO_PRECIO, COLUMN_PRODUCTO_CANTIDAD}, COLUMN_COMPRA_ID + "=?", new String[]{String.valueOf(compraId)}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int precioColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_PRECIO);
                int cantidadColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_CANTIDAD);

                if (precioColumnIndex != -1 && cantidadColumnIndex != -1) {
                    double precio = cursor.getDouble(precioColumnIndex);
                    int cantidad = cursor.getInt(cantidadColumnIndex);
                    total += precio * cantidad;
                } else {
                    Log.e("Error", "Columnas no encontradas en el cursor");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return total;
    }
    // Método para obtener la fecha y hora actual
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public Compra obtenerCompra(int compraId) {
        Log.d("********", "Obteniendo compra con ID " + compraId);
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_COMPRAS + " WHERE " + COLUMN_ID + " = ?";
        String[] args = new String[]{String.valueOf(compraId)};
        Cursor cursor = db.rawQuery(query, args);

        Compra compra = null;
        if (cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
            int nombreColumnIndex = cursor.getColumnIndex(COLUMN_NOMBRE);
            int fechaColumnIndex = cursor.getColumnIndex(COLUMN_FECHA);

            if (idColumnIndex != -1 && nombreColumnIndex != -1 && fechaColumnIndex != -1) {
                int id = cursor.getInt(idColumnIndex);
                String nombre = cursor.getString(nombreColumnIndex);
                String fecha = cursor.getString(fechaColumnIndex);
                compra = new Compra(id, nombre, fecha);
            } else {
                Log.e("Error", "Columnas no encontradas en el cursor");
            }
        }
        cursor.close();
        db.close();
        return compra;
    }

    // Método para obtener el historial de compras
    public List<String> obtenerHistorialCompras() {
        List<String> compras = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_COMPRAS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
                int nombreColumnIndex = cursor.getColumnIndex(COLUMN_NOMBRE);
                int fechaColumnIndex = cursor.getColumnIndex(COLUMN_FECHA);

                if (idColumnIndex != -1 && nombreColumnIndex != -1 && fechaColumnIndex != -1) {
                    String nombre = cursor.getString(nombreColumnIndex);
                    String fecha = cursor.getString(fechaColumnIndex);
                    compras.add(nombre + " - " + fecha);  // Combina el nombre y la fecha
                } else {
                    Log.e("Error", "Columnas no encontradas en el cursor");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return compras;
    }

    public double obtenerPrecioAnterior(int productoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PRODUCTO_PRECIO + " FROM " + TABLE_PRODUCTOS + " WHERE " + COLUMN_PRODUCTO_ID + " = ?";
        String[] args = new String[]{String.valueOf(productoId)};
        Cursor cursor = db.rawQuery(query, args);

        double precioAnterior = 0;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_PRECIO);
            if (columnIndex != -1) {
                precioAnterior = cursor.getDouble(columnIndex);
            } else {
                Log.e("Error", "Columna no encontrada en el cursor");
            }
        }
        cursor.close();
        db.close();
        return precioAnterior;
    }
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTOS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_ID);
                int nombreColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_NOMBRE);
                int precioColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_PRECIO);
                int cantidadColumnIndex = cursor.getColumnIndex(COLUMN_PRODUCTO_CANTIDAD);

                if (idColumnIndex != -1 && nombreColumnIndex != -1 && precioColumnIndex != -1 && cantidadColumnIndex != -1) {
                    int id = cursor.getInt(idColumnIndex);
                    String nombreProducto = cursor.getString(nombreColumnIndex);
                    double precio = cursor.getDouble(precioColumnIndex);
                    int cantidad = cursor.getInt(cantidadColumnIndex);
                    productos.add(new Producto(id, nombreProducto, cantidad, precio,0));
                } else {
                    Log.e("Error", "Columnas no encontradas en el cursor");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productos;
    }
}
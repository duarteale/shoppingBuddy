package com.example.shoppingbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

public class AdaptadorProductos extends BaseAdapter implements Filterable {

    private Context context;
    private List<Producto> productos;
    private SQLiteHelperDB db;

    public AdaptadorProductos(Context context, List<Producto> productos) {
        this.context = context;
        this.productos = productos;
        this.db = new SQLiteHelperDB(context);
    }

    @Override
    public int getCount() {
        return productos.size();
    }

    @Override
    public Object getItem(int position) {
        return productos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        Producto producto = productos.get(position);

        TextView tvNombre = view.findViewById(R.id.tv_nombre);
        tvNombre.setText(producto.getNombre());

        TextView tvPrecio = view.findViewById(R.id.tv_precio);
        tvPrecio.setText(String.valueOf(producto.getPrecioUnitario()));

        Button btnEditar = view.findViewById(R.id.btn_editar);
        btnEditar.setOnClickListener(v -> {
            // Abrir diálogo para editar el producto
            DialogEditarProducto dialog = new DialogEditarProducto(context, producto);
            dialog.show();
        });

        Button btnEliminar = view.findViewById(R.id.btn_eliminar);
        btnEliminar.setOnClickListener(v -> {
            // Eliminar el producto de la base de datos
            db.eliminarProducto(producto.getId());
            productos.remove(position);
            notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public Filter getFilter() {
        return new FiltroProductos();
    }

    private class FiltroProductos extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filtro = constraint.toString();
            List<Producto> productosFiltrados = db.buscarProductosPorNombre(filtro);
            FilterResults resultados = new FilterResults();
            resultados.values = productosFiltrados;
            resultados.count = productosFiltrados.size();
            return resultados;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productos = (List<Producto>) results.values;
            notifyDataSetChanged();
        }
    }
}
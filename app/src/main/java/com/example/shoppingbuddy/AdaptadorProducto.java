package com.example.shoppingbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorProducto extends RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder> {

    private ArrayList<Producto> productos;

    public AdaptadorProducto(List<Producto> productos) {
        this.productos = new ArrayList<>(productos);
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ProductoViewHolder(view);
    }
    public void actualizarProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        if (producto != null) {
            holder.tvNombreProducto.setText(producto.getNombre());
            holder.tvCantidadProducto.setText("Cantidad: " + producto.getCantidad());
            holder.tvPrecioProducto.setText("Precio unitario: $" + producto.getPrecioUnitario());
            holder.iconoCambioPrecio.setImageResource(producto.getIconoCambioPrecio());
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvCantidadProducto, tvPrecioProducto;

        public ImageView iconoCambioPrecio;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvCantidadProducto = itemView.findViewById(R.id.tvCantidadProducto);
            tvPrecioProducto = itemView.findViewById(R.id.tvPrecioProducto);
            iconoCambioPrecio = itemView.findViewById(R.id.iconoCambioPrecio);
        }
    }
}
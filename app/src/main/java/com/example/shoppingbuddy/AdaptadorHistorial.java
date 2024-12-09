package com.example.shoppingbuddy;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.HistorialViewHolder> {

    private List<String> historialCompras;

    public AdaptadorHistorial(List<String> historialCompras) {
        this.historialCompras = historialCompras;
    }

    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    public void onBindViewHolder(HistorialViewHolder holder, int position) {
        String compra = historialCompras.get(position);
        holder.tvHistorial.setText(compra);

        // Agregar listener para seleccionar la compra
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la compra seleccionada
                int currentPosition = holder.getAdapterPosition();
                String compraSeleccionada = historialCompras.get(currentPosition);

                // Crear un Intent para pasar la compra seleccionada a la nueva actividad
                Intent intent = new Intent(v.getContext(), Resumen.class);
                intent.putExtra("compra", compraSeleccionada);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historialCompras.size();
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistorial;

        public HistorialViewHolder(View itemView) {
            super(itemView);
            tvHistorial = itemView.findViewById(R.id.tvHistorial);
        }
    }
}
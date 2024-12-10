package com.example.shoppingbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.HistorialViewHolder> {

    private List<String> historialCompras;
    private OnItemClickListener mListener;

    public AdaptadorHistorial(List<String> historialCompras) {
        this.historialCompras = historialCompras;
    }

    @Override
    public HistorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistorialViewHolder holder, int position) {
        String compra = historialCompras.get(position);
        holder.tvHistorial.setText(compra);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historialCompras.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistorial;

        public HistorialViewHolder(View itemView) {
            super(itemView);
            tvHistorial = itemView.findViewById(R.id.tvHistorial);
        }
    }
}
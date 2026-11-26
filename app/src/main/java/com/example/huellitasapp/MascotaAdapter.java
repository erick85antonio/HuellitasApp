package com.example.huellitasapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private Context context;
    private List<Mascota> listaMascotas;

    public MascotaAdapter(Context context, List<Mascota> listaMascotas) {
        this.context = context;
        this.listaMascotas = listaMascotas;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        Mascota mascota = listaMascotas.get(position);
        holder.tvNombre.setText(mascota.getNombre());
        holder.tvEspecieEdad.setText(mascota.getEspecie() + " - " + mascota.getEdad() + " años");
        holder.tvEstado.setText("Estado: " + mascota.getEstado());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VerActivity.class);
            intent.putExtra("MASCOTA_ID", mascota.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEspecieEdad, tvEstado;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvItemNombre);
            tvEspecieEdad = itemView.findViewById(R.id.tvItemEspecieEdad);
            tvEstado = itemView.findViewById(R.id.tvItemEstado);
        }
    }
}

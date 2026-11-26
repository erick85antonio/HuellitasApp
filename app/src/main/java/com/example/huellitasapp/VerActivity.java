package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerActivity extends AppCompatActivity {

    private TextView tvNombre, tvEspecie, tvEdad, tvVacunado, tvEstado, tvDescripcion;
    private FirebaseFirestore db;
    private String mascotaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        db = FirebaseFirestore.getInstance();
        mascotaId = getIntent().getStringExtra("MASCOTA_ID");

        tvNombre = findViewById(R.id.tvVerNombre);
        tvEspecie = findViewById(R.id.tvVerEspecie);
        tvEdad = findViewById(R.id.tvVerEdad);
        tvVacunado = findViewById(R.id.tvVerVacunado);
        tvEstado = findViewById(R.id.tvVerEstado);
        tvDescripcion = findViewById(R.id.tvVerDescripcion);
        Button btnIrEditar = findViewById(R.id.btnIrEditar);
        Button btnEliminar = findViewById(R.id.btnEliminar);

        cargarDetalleMascota();

        btnIrEditar.setOnClickListener(v -> {
            Intent intent = new Intent(VerActivity.this, EditarActivity.class);
            intent.putExtra("MASCOTA_ID", mascotaId);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void cargarDetalleMascota() {
        if (mascotaId == null) return;

        db.collection("mascotas").document(mascotaId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Mascota mascota = documentSnapshot.toObject(Mascota.class);
                        if (mascota != null) {
                            tvNombre.setText(mascota.getNombre());
                            tvEspecie.setText("Especie: " + mascota.getEspecie());
                            tvEdad.setText("Edad: " + mascota.getEdad() + " años");
                            tvVacunado.setText("Vacunado: " + (mascota.isVacunado() ? "Sí" : "No"));
                            tvEstado.setText("Estado: " + mascota.getEstado());
                            tvDescripcion.setText("Descripción: " + mascota.getDescripcion());
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show());
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este registro? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarMascota())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarMascota() {
        db.collection("mascotas").document(mascotaId).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerActivity.this, ListarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show());
    }
}

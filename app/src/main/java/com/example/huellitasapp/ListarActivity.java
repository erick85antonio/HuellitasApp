package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ListarActivity extends AppCompatActivity {

    private RecyclerView rvMascotas;
    private MascotaAdapter adapter;
    private List<Mascota> listaMascotas;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        db = FirebaseFirestore.getInstance();
        rvMascotas = findViewById(R.id.rvMascotas);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregar);

        listaMascotas = new ArrayList<>();
        adapter = new MascotaAdapter(this, listaMascotas);

        rvMascotas.setLayoutManager(new LinearLayoutManager(this));
        rvMascotas.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ListarActivity.this, AgregarActivity.class);
            startActivity(intent);
        });

        escucharCambiosEnTiempoReal();
    }

    private void escucharCambiosEnTiempoReal() {
        db.collection("mascotas").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error al cargar datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (value != null) {
                listaMascotas.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Mascota mascota = doc.toObject(Mascota.class);
                    if (mascota != null) {
                        mascota.setId(doc.getId());
                        listaMascotas.add(mascota);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}

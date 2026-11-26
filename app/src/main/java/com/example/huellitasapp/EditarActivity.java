package com.example.huellitasapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditarActivity extends AppCompatActivity {

    private EditText etNombre, etEdad, etDescripcion;
    private Spinner spEspecie, spEstado;
    private CheckBox cbVacunado;
    private FirebaseFirestore db;
    private String mascotaId;

    private final String[] especies = {"Perro", "Gato", "Otro"};
    private final String[] estados = {"Disponible", "En proceso", "Adoptado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        db = FirebaseFirestore.getInstance();
        mascotaId = getIntent().getStringExtra("MASCOTA_ID");

        etNombre = findViewById(R.id.etEditarNombre);
        spEspecie = findViewById(R.id.spEditarEspecie);
        etEdad = findViewById(R.id.etEditarEdad);
        cbVacunado = findViewById(R.id.cbEditarVacunado);
        spEstado = findViewById(R.id.spEditarEstado);
        etDescripcion = findViewById(R.id.etEditarDescripcion);
        Button btnActualizar = findViewById(R.id.btnActualizar);

        configurarSpinners();
        cargarDatosActuales();

        btnActualizar.setOnClickListener(v -> actualizarMascota());
    }

    private void configurarSpinners() {
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especies);
        spEspecie.setAdapter(adapterEspecie);

        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        spEstado.setAdapter(adapterEstado);
    }

    private void cargarDatosActuales() {
        if (mascotaId == null) return;

        db.collection("mascotas").document(mascotaId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Mascota mascota = documentSnapshot.toObject(Mascota.class);
                        if (mascota != null) {
                            etNombre.setText(mascota.getNombre());
                            etEdad.setText(String.valueOf(mascota.getEdad()));
                            cbVacunado.setChecked(mascota.isVacunado());
                            etDescripcion.setText(mascota.getDescripcion());

                            int posEspecie = Arrays.asList(especies).indexOf(mascota.getEspecie());
                            if (posEspecie >= 0) spEspecie.setSelection(posEspecie);

                            int posEstado = Arrays.asList(estados).indexOf(mascota.getEstado());
                            if (posEstado >= 0) spEstado.setSelection(posEstado);
                        }
                    }
                });
    }

    private void actualizarMascota() {
        String nombre = etNombre.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || edadStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> actualizaciones = new HashMap<>();
        actualizaciones.put("nombre", nombre);
        actualizaciones.put("especie", spEspecie.getSelectedItem().toString());
        actualizaciones.put("edad", Integer.parseInt(edadStr));
        actualizaciones.put("vacunado", cbVacunado.isChecked());
        actualizaciones.put("estado", spEstado.getSelectedItem().toString());
        actualizaciones.put("descripcion", descripcion);

        db.collection("mascotas").document(mascotaId)
                .update(actualizaciones)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditarActivity.this, ListarActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

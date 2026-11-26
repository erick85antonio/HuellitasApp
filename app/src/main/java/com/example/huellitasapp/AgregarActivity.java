package com.example.huellitasapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AgregarActivity extends AppCompatActivity {

    private EditText etNombre, etEdad, etDescripcion;
    private Spinner spEspecie, spEstado;
    private CheckBox cbVacunado;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        db = FirebaseFirestore.getInstance();

        etNombre = findViewById(R.id.etNombre);
        spEspecie = findViewById(R.id.spEspecie);
        etEdad = findViewById(R.id.etEdad);
        cbVacunado = findViewById(R.id.cbVacunado);
        spEstado = findViewById(R.id.spEstado);
        etDescripcion = findViewById(R.id.etDescripcion);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        configurarSpinners();

        btnGuardar.setOnClickListener(v -> guardarMascota());
    }

    private void configurarSpinners() {
        String[] especies = {"Perro", "Gato", "Otro"};
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especies);
        spEspecie.setAdapter(adapterEspecie);

        String[] estados = {"Disponible", "En proceso", "Adoptado"};
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, estados);
        spEstado.setAdapter(adapterEstado);
    }

    private void guardarMascota() {
        String nombre = etNombre.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (nombre.isEmpty() || edadStr.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad = Integer.parseInt(edadStr);
        String especie = spEspecie.getSelectedItem().toString();
        String estado = spEstado.getSelectedItem().toString();
        boolean vacunado = cbVacunado.isChecked();

        Mascota nuevaMascota = new Mascota(nombre, especie, edad, vacunado, estado, descripcion);

        db.collection("mascotas")
                .add(nuevaMascota)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Mascota registrada con éxito", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

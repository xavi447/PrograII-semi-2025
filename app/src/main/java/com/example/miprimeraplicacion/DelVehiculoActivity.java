package com.example.miprimeraplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class DelVehiculoActivity extends AppCompatActivity {

    Spinner spinnerTipoPlaca;
    EditText editTextNumeroPlaca, editTextCodigoRuta;
    CheckBox checkExtranjera;
    EditText txtClase, txtMarca, txtModelo, txtColor;
    Button btnSiguiente;

    DB_vehiculos dbVehiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_vehiculo);

        // Inicializar base de datos
        dbVehiculos = new DB_vehiculos(this);

        // Referencias a los campos del layout (asegúrate que los IDs coincidan)
        spinnerTipoPlaca = findViewById(R.id.spinnerTipoPlaca);
        editTextNumeroPlaca = findViewById(R.id.editTextNumeroPlaca);
        editTextCodigoRuta = findViewById(R.id.editTextCodigoRuta);
        checkExtranjera = findViewById(R.id.checkExtranjera);

        txtClase = findViewById(R.id.txtClase);
        txtMarca = findViewById(R.id.txtMarca);
        txtModelo = findViewById(R.id.txtModelo);
        txtColor = findViewById(R.id.txtColor);

        btnSiguiente = findViewById(R.id.btnSiguiente);

        // Buscar vehículo cuando el EditText de número de placa pierda foco
        editTextNumeroPlaca.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String numeroPlaca = editTextNumeroPlaca.getText().toString().trim();
                if (!numeroPlaca.isEmpty()) {
                    buscarVehiculoPorPlaca(numeroPlaca);
                } else {
                    limpiarCampos();
                }
            }
        });

        // Acción del botón Siguiente (navegar a la siguiente actividad)
        btnSiguiente.setOnClickListener(v -> {
            Intent intent = new Intent(DelVehiculoActivity.this, falta.class);
            intent.putExtra("usuario_logueado", getIntent().getStringExtra("usuario_logueado"));
            startActivity(intent);
        });
    }

    private void buscarVehiculoPorPlaca(String numeroPlaca) {
        Cursor cursor = dbVehiculos.buscarPorPlaca(numeroPlaca);
        if (cursor != null && cursor.moveToFirst()) {
            String tipoPlaca = cursor.getString(cursor.getColumnIndexOrThrow("tipo_placa"));
            String codigoRuta = cursor.getString(cursor.getColumnIndexOrThrow("codigo_ruta"));
            int placaExtranjeraInt = cursor.getInt(cursor.getColumnIndexOrThrow("placa_extranjera"));
            String clase = cursor.getString(cursor.getColumnIndexOrThrow("clase"));
            String marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"));
            String modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo"));
            String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));

            setSpinnerValue(spinnerTipoPlaca, tipoPlaca);
            editTextCodigoRuta.setText(codigoRuta != null ? codigoRuta : "");
            checkExtranjera.setChecked(placaExtranjeraInt == 1);

            txtClase.setText(clase);
            txtMarca.setText(marca);
            txtModelo.setText(modelo);
            txtColor.setText(color);

            cursor.close();
        } else {
            limpiarCampos();
        }
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
        spinner.setSelection(0); // Valor por defecto si no coincide
    }

    private void limpiarCampos() {
        spinnerTipoPlaca.setSelection(0);
        editTextCodigoRuta.setText("");
        checkExtranjera.setChecked(false);
        txtClase.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtColor.setText("");
    }
}

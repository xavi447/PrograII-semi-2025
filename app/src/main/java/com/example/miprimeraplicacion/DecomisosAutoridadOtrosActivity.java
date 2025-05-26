package com.example.miprimeraplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class DecomisosAutoridadOtrosActivity extends AppCompatActivity {

    EditText etONI, etPuesto;
    CheckBox cbBA, cbEJ, cbYSU;
    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decomisos_deautoridad_otros);

        etONI = findViewById(R.id.etONI);
        etPuesto = findViewById(R.id.etPuestoPolicial);
        cbBA = findViewById(R.id.cbBA);
        cbEJ = findViewById(R.id.cbEJ);
        cbYSU = findViewById(R.id.cbYSU);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // Obtener el usuario logueado
        String usuario = getIntent().getStringExtra("usuario_logueado");

        DB_agentes dbAgentes = new DB_agentes(this);
        Cursor cursor = dbAgentes.obtenerDatosAgente(usuario);

        if (cursor.moveToFirst()) {
            etONI.setText(cursor.getString(cursor.getColumnIndexOrThrow("oni")));
            etPuesto.setText(cursor.getString(cursor.getColumnIndexOrThrow("puesto_policial")));
            cbBA.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("es_ba")) == 1);
            cbEJ.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("es_ej")) == 1);
            cbYSU.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow("es_ysu")) == 1);

            // Bloquear campos para que no se editen
            etONI.setEnabled(false);
            etPuesto.setEnabled(false);
            cbBA.setEnabled(false);
            cbEJ.setEnabled(false);
            cbYSU.setEnabled(false);
        }
        // Configurar el botón Siguiente
        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la pantalla de multas
                startActivity(new Intent(DecomisosAutoridadOtrosActivity.this, MultasActivity.class));
            }
        });
    }
}
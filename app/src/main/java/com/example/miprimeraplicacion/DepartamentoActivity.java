package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DepartamentoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.departamento);

        Button btnGuardarMulta = findViewById(R.id.btnGuardarMulta);
        btnGuardarMulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar mensaje de éxito
                Toast.makeText(DepartamentoActivity.this, "Multa guardada con éxito", Toast.LENGTH_SHORT).show();

                // Redirigir a DelConductorActivity
                startActivity(new Intent(DepartamentoActivity.this, DelConductorActivity.class));

                // Opcional: cerrar esta actividad si no se necesita volver atrás
                // finish();
            }
        });
    }
}
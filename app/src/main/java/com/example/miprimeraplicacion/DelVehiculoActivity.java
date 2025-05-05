package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DelVehiculoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_vehiculo);

        // Configuración básica del botón Siguiente (solo navegación)
        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación simple a la pantalla de Decomisos
                startActivity(new Intent(DelVehiculoActivity.this, DecomisosAutoridadActivity.class));
            }
        });
    }
}
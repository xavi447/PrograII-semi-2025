package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DelConductorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_conductor);

        // Configuración básica del botón
        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegación simple sin validaciones
                Intent intent = new Intent(DelConductorActivity.this, DelVehiculoActivity.class);
                intent.putExtra("usuario_logueado", getIntent().getStringExtra("usuario_logueado"));
                startActivity(intent);
            }
        });
    }
}
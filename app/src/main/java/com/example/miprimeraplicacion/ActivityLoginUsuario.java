package com.example.miprimeraplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityLoginUsuario extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        // Obtener referencia al botón de regresar
        ImageButton btnRegresar = findViewById(R.id.btnRegresar);

        // Establecer el comportamiento al hacer clic en el botón de regresar
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Regresar a la actividad anterior
                onBackPressed();  // Esto terminará la actividad actual y volverá a la anterior
            }
        });
    }
}



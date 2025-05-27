package com.example.miprimeraplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class DelConductorActivity extends AppCompatActivity {

    EditText licenciaInput, apellido1Input, apellido2Input, apellido3Input;
    EditText nombre1Input, nombre2Input, nombre3Input;
    CheckBox claseExtranjera, claseJuvenil, claseMotocicleta, claseParticular, clasePesadaT, clasePesadaL, claseLiviana;

    DB_conductores dbConductores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_conductor);

        // Inicializar base de datos
        dbConductores = new DB_conductores(this);

        // Referencias a campos
        licenciaInput = findViewById(R.id.licenciaInput);
        apellido1Input = findViewById(R.id.apellido1Input);
        apellido2Input = findViewById(R.id.apellido2Input);
        apellido3Input = findViewById(R.id.apellido3Input);
        nombre1Input = findViewById(R.id.nombre1Input);
        nombre2Input = findViewById(R.id.nombre2Input);
        nombre3Input = findViewById(R.id.nombre3Input);

        claseExtranjera = findViewById(R.id.claseExtranjera);
        claseJuvenil = findViewById(R.id.claseJuvenil);
        claseMotocicleta = findViewById(R.id.claseMotocicleta);
        claseParticular = findViewById(R.id.claseParticular);
        clasePesadaT = findViewById(R.id.clasePesadaT);
        clasePesadaL = findViewById(R.id.clasePesadaL);
        claseLiviana = findViewById(R.id.claseLiviana);

        // Buscar automáticamente cuando se pierda el foco del campo licencia
        licenciaInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                buscarConductor(licenciaInput.getText().toString().trim());
            }
        });

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

    private void buscarConductor(String licencia) {
        Cursor cursor = dbConductores.buscarPorLicencia(licencia);
        if (cursor.moveToFirst()) {
            apellido1Input.setText(cursor.getString(cursor.getColumnIndexOrThrow("apellido1")));
            apellido2Input.setText(cursor.getString(cursor.getColumnIndexOrThrow("apellido2")));
            nombre1Input.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre1")));
            nombre2Input.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre2")));

            String clase = cursor.getString(cursor.getColumnIndexOrThrow("clase"));
            actualizarClase(clase);
        }
        cursor.close();
    }

    private void actualizarClase(String clase) {
        claseExtranjera.setChecked("Extranjera".equalsIgnoreCase(clase));
        claseJuvenil.setChecked("Juvenil".equalsIgnoreCase(clase));
        claseMotocicleta.setChecked("Motocicleta".equalsIgnoreCase(clase));
        claseParticular.setChecked("Particular".equalsIgnoreCase(clase));
        clasePesadaT.setChecked("Pesada-T".equalsIgnoreCase(clase));
        clasePesadaL.setChecked("Pesada-L".equalsIgnoreCase(clase));
        claseLiviana.setChecked("Liviana".equalsIgnoreCase(clase));
    }
}
package com.example.miprimeraplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast; // <<<< Añadir importación
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList; // <<<< Añadir importación

public class DecomisosAutoridadOtrosActivity extends AppCompatActivity {

    EditText etONI, etPuesto;
    CheckBox cbBA, cbEJ, cbYSU;
    Button btnSiguiente; // Declarado solo una vez aquí

    // Variables para almacenar todos los datos recibidos
    private String usuarioLogueado;
    private String conductorLicencia, conductorApellido1, conductorApellido2, conductorApellido3,
            conductorNombre1, conductorNombre2, conductorNombre3, conductorClaseLicencia;
    private String vehiculoTipoPlaca, vehiculoNumeroPlaca, vehiculoCodigoRuta, vehiculoClase,
            vehiculoMarca, vehiculoModelo, vehiculoColor;
    private boolean vehiculoPlacaExtranjera; // Esto es un boolean
    private String fechaHoraInfraccion;
    private ArrayList<String> fotosEvidenciaPaths;

    DB_agentes dbAgentes; // Declarar la instancia de DB_agentes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decomisos_deautoridad_otros);

        etONI = findViewById(R.id.etONI);
        etPuesto = findViewById(R.id.etPuestoPolicial);
        cbBA = findViewById(R.id.cbBA);
        cbEJ = findViewById(R.id.cbEJ);
        cbYSU = findViewById(R.id.cbYSU);
        btnSiguiente = findViewById(R.id.btnSiguiente); // Inicializar el botón aquí

        // Inicializar DB_agentes
        dbAgentes = new DB_agentes(this);

        // RECIBIR: Todos los datos previos usando el Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuarioLogueado = extras.getString("usuario_logueado");
            conductorLicencia = extras.getString("conductor_licencia");
            conductorApellido1 = extras.getString("conductor_apellido1");
            conductorApellido2 = extras.getString("conductor_apellido2");
            conductorApellido3 = extras.getString("conductor_apellido3");
            conductorNombre1 = extras.getString("conductor_nombre1");
            conductorNombre2 = extras.getString("conductor_nombre2");
            conductorNombre3 = extras.getString("conductor_nombre3");
            conductorClaseLicencia = extras.getString("conductor_clase_licencia");
            vehiculoTipoPlaca = extras.getString("vehiculo_tipo_placa");
            vehiculoNumeroPlaca = extras.getString("vehiculo_numero_placa");
            vehiculoCodigoRuta = extras.getString("vehiculo_codigo_ruta");
            // CORRECCIÓN AQUÍ: Usar extras.getBoolean() para recuperar el boolean del Bundle
            vehiculoPlacaExtranjera = extras.getBoolean("vehiculo_placa_extranjera", false); // Default a false si no se encuentra
            vehiculoClase = extras.getString("vehiculo_clase");
            vehiculoMarca = extras.getString("vehiculo_marca");
            vehiculoModelo = extras.getString("vehiculo_modelo");
            vehiculoColor = extras.getString("vehiculo_color");
            fechaHoraInfraccion = extras.getString("fecha_hora_infraccion");
            fotosEvidenciaPaths = extras.getStringArrayList("fotos_evidencia_paths");
        }

        // Opcional: una verificación básica para asegurar que se recibieron los datos principales
        if (usuarioLogueado == null || conductorLicencia == null || vehiculoNumeroPlaca == null || fechaHoraInfraccion == null) {
            Toast.makeText(this, "Error: Datos previos incompletos en DecomisosAutoridadOtrosActivity.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar datos del agente logueado
        Cursor cursor = dbAgentes.obtenerDatosAgente(usuarioLogueado); // Usar la variable de instancia

        if (cursor != null && cursor.moveToFirst()) { // Añadir null check
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
            cursor.close(); // ¡Importante cerrar el cursor!
        } else {
            Toast.makeText(this, "No se encontraron datos para el agente logueado.", Toast.LENGTH_SHORT).show();
            // Considera qué hacer si no se encuentran datos del agente (p.ej. volver al login)
        }


        // Configurar el botón Siguiente
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recolectar datos del agente que están en los EditText (aunque estén deshabilitados)
                // Usamos los valores actuales de los EditText/CheckBoxes que ya se autocompletaron
                String agenteOni = etONI.getText().toString();
                String agentePuesto = etPuesto.getText().toString();
                // Los booleanos de es_ba, es_ej, es_ysu no los estamos pasando a la tabla Multas directamente,
                // pero podrías hacerlo si fueran relevantes para el registro de la multa o si la tabla
                // DB_multas tiene columnas para ellos.

                // PASAR: Navegar a la pantalla de multas, pasando todos los datos acumulados
                Intent intent = new Intent(DecomisosAutoridadOtrosActivity.this, MultasActivity.class);

                // Datos del Agente (usuario_logueado ya lo llevamos, y ahora ONI y Puesto)
                intent.putExtra("usuario_logueado", usuarioLogueado);
                intent.putExtra("agente_oni", agenteOni);
                intent.putExtra("agente_puesto", agentePuesto);

                // Datos del Conductor (recibidos de la actividad anterior)
                intent.putExtra("conductor_licencia", conductorLicencia);
                intent.putExtra("conductor_apellido1", conductorApellido1);
                intent.putExtra("conductor_apellido2", conductorApellido2);
                intent.putExtra("conductor_apellido3", conductorApellido3);
                intent.putExtra("conductor_nombre1", conductorNombre1);
                intent.putExtra("conductor_nombre2", conductorNombre2);
                intent.putExtra("conductor_nombre3", conductorNombre3);
                intent.putExtra("conductor_clase_licencia", conductorClaseLicencia);

                // Datos del Vehículo (recibidos de la actividad anterior)
                intent.putExtra("vehiculo_tipo_placa", vehiculoTipoPlaca);
                intent.putExtra("vehiculo_numero_placa", vehiculoNumeroPlaca);
                intent.putExtra("vehiculo_codigo_ruta", vehiculoCodigoRuta);
                intent.putExtra("vehiculo_placa_extranjera", vehiculoPlacaExtranjera); // Pasa el boolean correctamente
                intent.putExtra("vehiculo_clase", vehiculoClase);
                intent.putExtra("vehiculo_marca", vehiculoMarca);
                intent.putExtra("vehiculo_modelo", vehiculoModelo);
                intent.putExtra("vehiculo_color", vehiculoColor);

                // Datos de la Falta (Fecha/Hora y Fotos - recibidos de la actividad anterior)
                intent.putExtra("fecha_hora_infraccion", fechaHoraInfraccion);
                intent.putStringArrayListExtra("fotos_evidencia_paths", fotosEvidenciaPaths);

                startActivity(intent);
            }
        });
    }
}
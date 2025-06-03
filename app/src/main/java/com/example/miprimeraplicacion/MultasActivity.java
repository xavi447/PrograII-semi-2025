package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast; // <<<< Importar Toast
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList; // <<<< Importar ArrayList
import androidx.core.content.ContextCompat;
public class MultasActivity extends AppCompatActivity {

    private EditText inputMonto;
    private RadioGroup radioGroupCategoria;
    private Button btnSiguiente;

    // Variables para almacenar todos los datos recibidos
    private String usuarioLogueado;
    private String agenteOni, agentePuesto; // Datos del agente confirmados
    private String conductorLicencia, conductorApellido1, conductorApellido2, conductorApellido3,
            conductorNombre1, conductorNombre2, conductorNombre3, conductorClaseLicencia;
    private String vehiculoTipoPlaca, vehiculoNumeroPlaca, vehiculoCodigoRuta, vehiculoClase,
            vehiculoMarca, vehiculoModelo, vehiculoColor;
    private boolean vehiculoPlacaExtranjera;
    private String fechaHoraInfraccion;
    private ArrayList<String> fotosEvidenciaPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.de_multas); // Asegúrate que coincida con tu XML

        // 1. Obtener referencias de vistas
        inputMonto = findViewById(R.id.inputMonto);
        radioGroupCategoria = findViewById(R.id.radioGroupCategoria);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // RECIBIR: Todos los datos previos
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Datos del Agente
            usuarioLogueado = extras.getString("usuario_logueado");
            agenteOni = extras.getString("agente_oni");
            agentePuesto = extras.getString("agente_puesto");

            // Datos del Conductor
            conductorLicencia = extras.getString("conductor_licencia");
            conductorApellido1 = extras.getString("conductor_apellido1");
            conductorApellido2 = extras.getString("conductor_apellido2");
            conductorApellido3 = extras.getString("conductor_apellido3");
            conductorNombre1 = extras.getString("conductor_nombre1");
            conductorNombre2 = extras.getString("conductor_nombre2");
            conductorNombre3 = extras.getString("conductor_nombre3");
            conductorClaseLicencia = extras.getString("conductor_clase_licencia");

            // Datos del Vehículo
            vehiculoTipoPlaca = extras.getString("vehiculo_tipo_placa");
            vehiculoNumeroPlaca = extras.getString("vehiculo_numero_placa");
            vehiculoCodigoRuta = extras.getString("vehiculo_codigo_ruta");
            vehiculoPlacaExtranjera = extras.getBoolean("vehiculo_placa_extranjera", false); // Correcto para Bundle
            vehiculoClase = extras.getString("vehiculo_clase");
            vehiculoMarca = extras.getString("vehiculo_marca");
            vehiculoModelo = extras.getString("vehiculo_modelo");
            vehiculoColor = extras.getString("vehiculo_color");

            // Datos de la Falta
            fechaHoraInfraccion = extras.getString("fecha_hora_infraccion");
            fotosEvidenciaPaths = extras.getStringArrayList("fotos_evidencia_paths");
        }

        // Opcional: una verificación básica para asegurar que se recibieron los datos principales
        if (usuarioLogueado == null || conductorLicencia == null || vehiculoNumeroPlaca == null || fechaHoraInfraccion == null || agenteOni == null) {
            Toast.makeText(this, "Error: Datos previos incompletos en MultasActivity.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // 2. Configurar listener para cambios en RadioGroup
        radioGroupCategoria.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioLeve) {
                    setMontoAutomatico(inputMonto, "50.00");
                } else if (checkedId == R.id.radioGrave) {
                    setMontoAutomatico(inputMonto, "100.00");
                } else if (checkedId == R.id.radioMuyGrave) {
                    setMontoAutomatico(inputMonto, "150.00");
                } else if (checkedId == R.id.radioOtra) {
                    setMontoEditable(inputMonto);
                }
            }
        });

        // 3. Configurar botón Siguiente
        btnSiguiente.setOnClickListener(v -> {
            // Recolectar datos de la multa
            String montoMulta = inputMonto.getText().toString().trim();
            String categoriaMulta = "";
            int selectedId = radioGroupCategoria.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Por favor, seleccione una categoría de multa.", Toast.LENGTH_SHORT).show();
                return;
            } else if (selectedId == R.id.radioLeve) {
                categoriaMulta = "Leve";
            } else if (selectedId == R.id.radioGrave) {
                categoriaMulta = "Grave";
            } else if (selectedId == R.id.radioMuyGrave) {
                categoriaMulta = "Muy Grave";
            } else if (selectedId == R.id.radioOtra) {
                categoriaMulta = "Otra";
                if (montoMulta.isEmpty()) {
                    Toast.makeText(this, "Por favor, ingrese el monto de la multa.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Opcional: Validar que el monto sea un número válido si la categoría es "Otra"
                try {
                    Double.parseDouble(montoMulta);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Monto de multa inválido.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (montoMulta.isEmpty()) { // Esto puede pasar si no se selecciona ninguna y radioOtra tampoco se llena
                Toast.makeText(this, "El monto de la multa no puede estar vacío.", Toast.LENGTH_SHORT).show();
                return;
            }


            // PASAR: Crear Intent y adjuntar TODOS los datos acumulados
            Intent intent = new Intent(MultasActivity.this, UbicacionActivity.class);

            // Datos del Agente (recibidos y confirmados aquí)
            intent.putExtra("usuario_logueado", usuarioLogueado);
            intent.putExtra("agente_oni", agenteOni);
            intent.putExtra("agente_puesto", agentePuesto);

            // Datos del Conductor (recibidos)
            intent.putExtra("conductor_licencia", conductorLicencia);
            intent.putExtra("conductor_apellido1", conductorApellido1);
            intent.putExtra("conductor_apellido2", conductorApellido2);
            intent.putExtra("conductor_apellido3", conductorApellido3);
            intent.putExtra("conductor_nombre1", conductorNombre1);
            intent.putExtra("conductor_nombre2", conductorNombre2);
            intent.putExtra("conductor_nombre3", conductorNombre3);
            intent.putExtra("conductor_clase_licencia", conductorClaseLicencia);

            // Datos del Vehículo (recibidos)
            intent.putExtra("vehiculo_tipo_placa", vehiculoTipoPlaca);
            intent.putExtra("vehiculo_numero_placa", vehiculoNumeroPlaca);
            intent.putExtra("vehiculo_codigo_ruta", vehiculoCodigoRuta);
            intent.putExtra("vehiculo_placa_extranjera", vehiculoPlacaExtranjera);
            intent.putExtra("vehiculo_clase", vehiculoClase);
            intent.putExtra("vehiculo_marca", vehiculoMarca);
            intent.putExtra("vehiculo_modelo", vehiculoModelo);
            intent.putExtra("vehiculo_color", vehiculoColor);

            // Datos de la Falta (recibidos)
            intent.putExtra("fecha_hora_infraccion", fechaHoraInfraccion);
            intent.putStringArrayListExtra("fotos_evidencia_paths", fotosEvidenciaPaths);

            // Datos de la Multa (propios de esta actividad)
            intent.putExtra("multa_monto", montoMulta);
            intent.putExtra("multa_categoria", categoriaMulta);

            startActivity(intent);
        });
    }

    private void setMontoAutomatico(EditText editText, String monto) {
        editText.setText(monto);
        editText.setFocusable(false);
        editText.setClickable(false);
        // Usar ContextCompat para colores para compatibilidad con versiones antiguas
        editText.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
    }

    private void setMontoEditable(EditText editText) {
        editText.setText("");
        editText.setHint("Ingrese monto");
        editText.setFocusableInTouchMode(true);
        editText.setClickable(true);
        // Asegúrate de que R.drawable.edit_text_bg exista
        editText.setBackgroundResource(R.drawable.edit_text_bg);
    }
}
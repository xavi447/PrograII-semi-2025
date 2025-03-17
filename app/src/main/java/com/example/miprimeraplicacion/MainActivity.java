package com.example.miprimeraplicacion;


import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempVal;
    DB db;
    String accion = "nuevo", idAmigo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DB(this);
        btn = findViewById(R.id.btnGuardarAmigo);
        btn.setOnClickListener(view -> guardarAmigo());

        fab = findViewById(R.id.fabListaAmigos);
        fab.setOnClickListener(view -> abrirVentana());
        mostrarDatos();
    }


    private void mostrarDatos() {
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("amigos"));
                idAmigo = datos.getString("idAmigo");

                tempVal = findViewById(R.id.txtNombre);
                tempVal.setText(datos.getString("nombre"));

                tempVal = findViewById(R.id.txtDireccion);
                tempVal.setText(datos.getString("direccion"));

                tempVal = findViewById(R.id.txtTelefono);
                tempVal.setText(datos.getString("telefono"));

                tempVal = findViewById(R.id.txtEmail);
                tempVal.setText(datos.getString("email"));

                tempVal = findViewById(R.id.txtDui);
                tempVal.setText(datos.getString("dui"));
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
        private void abrirVentana () {
            Intent intent = new Intent(this, lista_amigos.class);
            startActivity(intent);
        }
        private void guardarAmigo () {
            tempVal = findViewById(R.id.txtNombre);
            String nombre = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDireccion);
            String direccion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtTelefono);
            String telefono = tempVal.getText().toString();
            tempVal = findViewById(R.id.txtEmail);
            String email = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDui);
            String dui = tempVal.getText().toString();

            String[] datos = {"", nombre, direccion, telefono, email, dui, ""};
            db.administrar_amigos("agregar", datos);
            Toast.makeText(getApplicationContext(), "Registro guardado con exito.", Toast.LENGTH_LONG).show();
            abrirVentana();


        }
    }






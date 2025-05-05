package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class MultasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.de_multas); // Asegúrate que coincida con tu XML

        // 1. Obtener referencias de vistas
        final EditText inputMonto = findViewById(R.id.inputMonto);
        final RadioGroup radioGroup = findViewById(R.id.radioGroupCategoria);
        Button btnSiguiente = findViewById(R.id.btnSiguiente);

        // 2. Configurar listener para cambios en RadioGroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
            startActivity(new Intent(MultasActivity.this, DepartamentoActivity.class));
        });
    }

    private void setMontoAutomatico(EditText editText, String monto) {
        editText.setText(monto);
        editText.setFocusable(false);
        editText.setClickable(false);
        editText.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void setMontoEditable(EditText editText) {
        editText.setText("");
        editText.setHint("Ingrese monto");
        editText.setFocusableInTouchMode(true);
        editText.setClickable(true);
        editText.setBackgroundResource(R.drawable.edit_text_bg);
    }
}
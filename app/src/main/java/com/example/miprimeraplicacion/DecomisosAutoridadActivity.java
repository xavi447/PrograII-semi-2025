package com.example.miprimeraplicacion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DecomisosAutoridadActivity extends AppCompatActivity {

    private EditText etFechaDecomiso;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.falta);

        // Configuración del TimePicker
        TimePicker timePicker = findViewById(R.id.timePickerDecomiso);
        etFechaDecomiso = findViewById(R.id.etFechaDecomiso);

        // Establecer hora actual por defecto
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(currentHour);
            timePicker.setMinute(currentMinute);
            timePicker.setIs24HourView(false); // Formato de 12 horas (AM/PM)
        } else {
            timePicker.setCurrentHour(currentHour);
            timePicker.setCurrentMinute(currentMinute);
        }

        // Configurar el selector de fecha
        setupDatePicker();

        // Configurar el botón Siguiente
        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(v ->
                startActivity(new Intent(DecomisosAutoridadActivity.this, DecomisosAutoridadOtrosActivity.class))
        );
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        etFechaDecomiso.setOnClickListener(v ->
                new DatePickerDialog(
                        this,
                        dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
        );
    }

    private void updateDateLabel() {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        etFechaDecomiso.setText(sdf.format(calendar.getTime()));
    }
}
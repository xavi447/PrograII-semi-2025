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

public class MainActivity extends AppCompatActivity {


    Button btn;
    TextView tempVal;
    RadioGroup rgb;
    RadioButton opt;
    Spinner spn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempVal = findViewById(R.id.txtNum1);
                double num1 = Double.parseDouble(tempVal.getText().toString());
                tempVal = findViewById(R.id.txtNum2);
                double num2 = Double.parseDouble(tempVal.getText().toString());

                double respuesta = 0.0;
                String msg = "";
                spn = findViewById(R.id.spnOpciones);
                switch (spn.getSelectedItemPosition()){
                    case 0:
                        respuesta = num1 + num2;
                        msg = "La suma es: "+ respuesta;
                        break;
                    case 1:
                        respuesta = num1 - num2;
                        msg = "La resta es: "+ respuesta;
                        break;
                    case 2:
                        respuesta = num1 * num2;
                        msg = "La multiplicacion es: "+ respuesta;
                        break;
                    case 3:
                        respuesta = num1 / num2;
                        msg = "La division es: "+ respuesta;
                        break;
                    case 4:
                        //Factor
                        if (num1 >= 0) {
                            int fact = 1;
                            for (int i = 1; i <= num1; i++) {
                                fact *= i;
                            }
                            msg = "El factorial de " + num1 + " es: " + fact;
                        }  else {
                            msg = "Error: No se puede calcular el factorial de un número negativo.";
                        }
                        break;
                    case 5:
                        // Porcentaje
                        respuesta = (num1 / 100.0) * num2;
                        msg = num1 + "% de " + num2 + " es: " + respuesta;
                        break;
                    case 6:
                        // Exponenciación
                        respuesta = Math.pow(num1, num2);
                        msg = num1 + " elevado a " + num2 + " es: " + respuesta;
                        break;
                    case 7:
                        // Raíz
                        if (num2 != 0) {
                            respuesta = Math.pow(num1, 1.0 / num2);
                            msg = "La raíz de índice " + num2 + " de " + num1 + " es: " + respuesta;
                        } else {
                            msg = "Error: El índice de la raíz no puede ser cero.";
                        }
                        break;
                    case 8:
                        // Módulo
                        if (num2 != 0) {
                            respuesta = num1 % num2;
                            msg = "El módulo de " + num1 + " y " + num2 + " es: " + respuesta;
                        } else {
                            msg = "Error: No se puede calcular el módulo con divisor cero.";
                        }
                        break;
                }
                tempVal = findViewById(R.id.lblRespuesta);
                tempVal.setText("Respuesta: "+ respuesta);
            }
        });
    }
}





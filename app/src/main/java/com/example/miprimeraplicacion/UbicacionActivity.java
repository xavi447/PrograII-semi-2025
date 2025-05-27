package com.example.miprimeraplicacion;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UbicacionActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private TextView tvUbicacion;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.departamento);

        tvUbicacion = findViewById(R.id.tvUbicacion);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        pedirPermisoUbicacion();
    }

    private void pedirPermisoUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            obtenerUbicacion();
        }
    }

    @SuppressLint("MissingPermission")
    private void obtenerUbicacion() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                obtenerDireccion(location);
            } else {
                tvUbicacion.setText("No se pudo obtener la ubicación.");
            }
        });
    }

    private void obtenerDireccion(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaDirecciones = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!listaDirecciones.isEmpty()) {
                Address direccion = listaDirecciones.get(0);

                String calle = direccion.getThoroughfare();         // Calle o avenida
                String municipio = direccion.getSubAdminArea();     // Municipio
                String departamento = direccion.getAdminArea();     // Departamento o estado

                String resultado = "Departamento: " + (departamento != null ? departamento : "Desconocido") + "\n"
                        + "Municipio: " + (municipio != null ? municipio : "Desconocido") + "\n"
                        + "Calle: " + (calle != null ? calle : "Desconocida");

                tvUbicacion.setText(resultado);
            } else {
                tvUbicacion.setText("No se encontró dirección.");
            }
        } catch (IOException e) {
            tvUbicacion.setText("Error al obtener dirección.");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

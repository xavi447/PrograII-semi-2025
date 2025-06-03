package com.example.miprimeraplicacion;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

// <<< INICIO DE LAS IMPORTACIONES PARA iTEXT 7 >>>
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment; // Para alinear texto si lo necesitas
import com.itextpdf.kernel.colors.ColorConstants; // Para colores predefinidos (BLUE, BLACK, etc.)
import com.itextpdf.kernel.colors.DeviceRgb; // Para definir colores con RGB
// <<< FIN DE LAS IMPORTACIONES PARA iTEXT 7 >>>

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UbicacionActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_WRITE_STORAGE = 112;

    private TextView tvUbicacion;
    private FusedLocationProviderClient fusedLocationClient;
    private Button btnGuardarMulta;

    // Variables para almacenar todos los datos recibidos
    private String usuarioLogueado;
    private String agenteOni, agentePuesto;
    private String conductorLicencia, conductorApellido1, conductorApellido2, conductorApellido3,
            conductorNombre1, conductorNombre2, conductorNombre3, conductorClaseLicencia;
    private String vehiculoTipoPlaca, vehiculoNumeroPlaca, vehiculoCodigoRuta, vehiculoClase,
            vehiculoMarca, vehiculoModelo, vehiculoColor;
    private boolean vehiculoPlacaExtranjera;
    private String fechaHoraInfraccion;
    private ArrayList<String> fotosEvidenciaPaths;
    private String multaMonto, multaCategoria;

    // Variables para los datos de ubicación
    private String ubicacionDepartamento = "Desconocido";
    private String ubicacionMunicipio = "Desconocido";
    private String ubicacionCalle = "Desconocida";

    // Instancia de la base de datos de multas
    private DB_multas dbMultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.departamento);

        tvUbicacion = findViewById(R.id.tvUbicacion);
        btnGuardarMulta = findViewById(R.id.btnGuardarMulta);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        dbMultas = new DB_multas(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuarioLogueado = extras.getString("usuario_logueado");
            agenteOni = extras.getString("agente_oni");
            agentePuesto = extras.getString("agente_puesto");

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
            vehiculoPlacaExtranjera = extras.getBoolean("vehiculo_placa_extranjera", false);
            vehiculoClase = extras.getString("vehiculo_clase");
            vehiculoMarca = extras.getString("vehiculo_marca");
            vehiculoModelo = extras.getString("vehiculo_modelo");
            vehiculoColor = extras.getString("vehiculo_color");

            fechaHoraInfraccion = extras.getString("fecha_hora_infraccion");
            fotosEvidenciaPaths = extras.getStringArrayList("fotos_evidencia_paths");

            multaMonto = extras.getString("multa_monto");
            multaCategoria = extras.getString("multa_categoria");
        }

        if (usuarioLogueado == null || conductorLicencia == null || vehiculoNumeroPlaca == null ||
                fechaHoraInfraccion == null || agenteOni == null || multaMonto == null) {
            Toast.makeText(this, "Error: Datos previos incompletos en UbicacionActivity. No se puede guardar la multa.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        pedirPermisoUbicacion();

        btnGuardarMulta.setOnClickListener(v -> {
            guardarMultaEnBaseDeDatos();
        });
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
                tvUbicacion.setText("No se pudo obtener la ubicación. Intente de nuevo.");
                Toast.makeText(this, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            tvUbicacion.setText("Error al obtener la ubicación: " + e.getMessage());
            Toast.makeText(this, "Error al obtener la ubicación: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void obtenerDireccion(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaDirecciones = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!listaDirecciones.isEmpty()) {
                Address direccion = listaDirecciones.get(0);

                ubicacionCalle = direccion.getThoroughfare() != null ? direccion.getThoroughfare() : "Desconocida";
                ubicacionMunicipio = direccion.getLocality() != null ? direccion.getLocality() : "Desconocido";
                ubicacionDepartamento = direccion.getAdminArea() != null ? direccion.getAdminArea() : "Desconocido";


                String resultado = "Departamento: " + ubicacionDepartamento + "\n"
                        + "Municipio: " + ubicacionMunicipio + "\n"
                        + "Calle: " + ubicacionCalle;

                tvUbicacion.setText(resultado);
                Toast.makeText(this, "Ubicación obtenida correctamente.", Toast.LENGTH_SHORT).show();

            } else {
                tvUbicacion.setText("No se encontró dirección para la ubicación.");
                Toast.makeText(this, "No se encontró dirección para la ubicación.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            tvUbicacion.setText("Error al obtener dirección: " + e.getMessage());
            Toast.makeText(this, "Error al obtener dirección: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. No se podrá guardar la ubicación de la multa.", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePdf();
            } else {
                Toast.makeText(this, "Permiso de almacenamiento denegado. No se puede guardar el PDF de la multa.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarMultaEnBaseDeDatos() {
        String fotosPathsString = "";
        if (fotosEvidenciaPaths != null && !fotosEvidenciaPaths.isEmpty()) {
            fotosPathsString = String.join(";", fotosEvidenciaPaths);
        }

        long idMulta = dbMultas.insertarMulta(
                usuarioLogueado,
                agenteOni,
                agentePuesto,
                conductorLicencia,
                conductorApellido1,
                conductorApellido2,
                conductorApellido3,
                conductorNombre1,
                conductorNombre2,
                conductorNombre3,
                conductorClaseLicencia,
                vehiculoTipoPlaca,
                vehiculoNumeroPlaca,
                vehiculoCodigoRuta,
                vehiculoPlacaExtranjera ? 1 : 0,
                vehiculoClase,
                vehiculoMarca,
                vehiculoModelo,
                vehiculoColor,
                fechaHoraInfraccion,
                fotosPathsString,
                multaMonto,
                multaCategoria,
                ubicacionDepartamento,
                ubicacionMunicipio,
                ubicacionCalle
        );

        if (idMulta != -1) {
            Toast.makeText(this, "Multa guardada exitosamente con ID: " + idMulta, Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                generatePdf();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                } else {
                    generatePdf();
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al guardar la multa.", Toast.LENGTH_LONG).show();
        }
    }

    private void generatePdf() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String pdfFileName = "MULTA_" + vehiculoNumeroPlaca + "_" + timeStamp + ".pdf";

        OutputStream outputStream = null;
        Uri pdfUri = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "MisMultas");

                pdfUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                if (pdfUri == null) {
                    throw new IOException("Failed to create new MediaStore record.");
                }
                outputStream = resolver.openOutputStream(pdfUri);

            } else {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File appDir = new File(downloadsDir, "MisMultas");
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }
                File pdfFile = new File(appDir, pdfFileName);
                outputStream = new FileOutputStream(pdfFile);
                pdfUri = Uri.fromFile(pdfFile);
            }

            if (outputStream == null) {
                Toast.makeText(this, "Error: No se pudo obtener el stream de salida para el PDF.", Toast.LENGTH_LONG).show();
                return;
            }

            // --- Creación del documento PDF con iText 7 ---
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf); // document acepta ahora el PdfDocument

            // --- ESTILO DE FUENTES (usando iText 7 Colors y font.setFontSize) ---
            // iText 7 usa ColorConstants para colores predefinidos o DeviceRgb para RGB personalizado.
            // Para cambiar el tamaño y el color, lo aplicas directamente al objeto Paragraph.

            // Azul
            com.itextpdf.layout.element.Paragraph titleParagraph =
                    new Paragraph("REPORTE DE MULTA")
                            .setFontColor(ColorConstants.BLUE)
                            .setFontSize(18)
                            .setBold(); // Para negrita

            // Gris Oscuro (puedes usar ColorConstants.DARK_GRAY o DeviceRgb)
            DeviceRgb darkGray = new DeviceRgb(64, 64, 64);
            com.itextpdf.layout.element.Paragraph sectionParagraph;

            // Negro
            com.itextpdf.layout.element.Paragraph dataParagraph;
            com.itextpdf.layout.element.Paragraph smallDataParagraph;

            // --- CONTENIDO DEL PDF ---
            document.add(titleParagraph.setTextAlignment(TextAlignment.CENTER)); // Centrar el título
            document.add(new Paragraph("Fecha de Generación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()))
                    .setFontSize(10)
                    .setFontColor(ColorConstants.BLACK));
            document.add(new Paragraph("\n"));

            // AGENTE
            sectionParagraph = new Paragraph("--- DATOS DEL AGENTE ---")
                    .setFontColor(darkGray)
                    .setFontSize(14)
                    .setBold();
            document.add(sectionParagraph);
            dataParagraph = new Paragraph("Usuario Logueado: " + (usuarioLogueado != null ? usuarioLogueado : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("ONI: " + (agenteOni != null ? agenteOni : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Puesto: " + (agentePuesto != null ? agentePuesto : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            document.add(new Paragraph("\n"));

            // CONDUCTOR
            sectionParagraph = new Paragraph("--- DATOS DEL CONDUCTOR ---")
                    .setFontColor(darkGray)
                    .setFontSize(14)
                    .setBold();
            document.add(sectionParagraph);
            dataParagraph = new Paragraph("Licencia: " + (conductorLicencia != null ? conductorLicencia : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Nombres: " + (conductorNombre1 != null ? conductorNombre1 : "") + " " + (conductorNombre2 != null ? conductorNombre2 : "") + " " + (conductorNombre3 != null ? conductorNombre3 : ""))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Apellidos: " + (conductorApellido1 != null ? conductorApellido1 : "") + " " + (conductorApellido2 != null ? conductorApellido2 : "") + " " + (conductorApellido3 != null ? conductorApellido3 : ""))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Clase Licencia: " + (conductorClaseLicencia != null ? conductorClaseLicencia : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            document.add(new Paragraph("\n"));

            // VEHÍCULO
            sectionParagraph = new Paragraph("--- DATOS DEL VEHÍCULO ---")
                    .setFontColor(darkGray)
                    .setFontSize(14)
                    .setBold();
            document.add(sectionParagraph);
            dataParagraph = new Paragraph("Tipo de Placa: " + (vehiculoTipoPlaca != null ? vehiculoTipoPlaca : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Número de Placa: " + (vehiculoNumeroPlaca != null ? vehiculoNumeroPlaca : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Código de Ruta: " + (vehiculoCodigoRuta != null ? vehiculoCodigoRuta : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Placa Extranjera: " + (vehiculoPlacaExtranjera ? "Sí" : "No"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Clase: " + (vehiculoClase != null ? vehiculoClase : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Marca: " + (vehiculoMarca != null ? vehiculoMarca : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Modelo: " + (vehiculoModelo != null ? vehiculoModelo : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Color: " + (vehiculoColor != null ? vehiculoColor : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            document.add(new Paragraph("\n"));

            // FALTA
            sectionParagraph = new Paragraph("--- DATOS DE LA FALTA Y MULTA ---")
                    .setFontColor(darkGray)
                    .setFontSize(14)
                    .setBold();
            document.add(sectionParagraph);
            dataParagraph = new Paragraph("Fecha y Hora Infracción: " + (fechaHoraInfraccion != null ? fechaHoraInfraccion : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Monto de la Multa: " + (multaMonto != null ? multaMonto : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Categoría de la Multa: " + (multaCategoria != null ? multaCategoria : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);


            if (fotosEvidenciaPaths != null && !fotosEvidenciaPaths.isEmpty()) {
                document.add(new Paragraph("Rutas de Fotos de Evidencia:")
                        .setFontSize(10)
                        .setFontColor(ColorConstants.BLACK));
                for (String path : fotosEvidenciaPaths) {
                    document.add(new Paragraph("- " + path)
                            .setFontSize(8)
                            .setFontColor(ColorConstants.BLACK));
                }
            } else {
                document.add(new Paragraph("Fotos de Evidencia: Ninguna")
                        .setFontSize(10)
                        .setFontColor(ColorConstants.BLACK));
            }
            document.add(new Paragraph("\n"));

            // UBICACIÓN
            sectionParagraph = new Paragraph("--- UBICACIÓN ---")
                    .setFontColor(darkGray)
                    .setFontSize(14)
                    .setBold();
            document.add(sectionParagraph);
            dataParagraph = new Paragraph("Departamento: " + (ubicacionDepartamento != null ? ubicacionDepartamento : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Municipio: " + (ubicacionMunicipio != null ? ubicacionMunicipio : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            dataParagraph = new Paragraph("Calle/Dirección: " + (ubicacionCalle != null ? ubicacionCalle : "N/A"))
                    .setFontSize(10).setFontColor(ColorConstants.BLACK);
            document.add(dataParagraph);
            document.add(new Paragraph("\n"));


            document.close();
            // Ya no es necesario cerrar outputStream aquí, iText 7 lo gestiona con PdfWriter.close() implícitamente al cerrar document.

            Toast.makeText(this, "PDF guardado exitosamente en " + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? "Descargas/MisMultas/" : "su almacenamiento público/MisMultas/") + pdfFileName, Toast.LENGTH_LONG).show();

            if (pdfUri != null) {
                abrirPdf(pdfUri);
            }

        } catch (IOException e) { // Solo IOException, DocumentException ya no es directamente lanzada aquí
            e.printStackTrace();
            Toast.makeText(this, "Error al generar el PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void abrirPdf(Uri pdfUri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se encontró una aplicación para abrir PDFs. " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
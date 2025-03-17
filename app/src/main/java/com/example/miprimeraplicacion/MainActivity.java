package com.example.miprimeraplicacion;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText txtNombre, txtMarca, txtCodigo, txtDescripcion, txtPresentacion, txtPrecio;
    private ImageButton imgFotoProducto;
    private Button btnGuardarProducto;
    private FloatingActionButton fabListaProductos;
    private DB db;
    private String urlCompletaFoto = "";
    private Intent tomarFotoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        txtNombre = findViewById(R.id.txtNombre);
        txtMarca = findViewById(R.id.txtMarca);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPresentacion = findViewById(R.id.txtPresentacion);
        txtPrecio = findViewById(R.id.txtPrecio);
        imgFotoProducto = findViewById(R.id.imgFotoProducto);
        btnGuardarProducto = findViewById(R.id.btnGuardarProducto);
        fabListaProductos = findViewById(R.id.fabListaProductos);

        db = new DB(this);

        // Configurar el botón para guardar el producto
        btnGuardarProducto.setOnClickListener(view -> guardarProducto());

        // Configurar el botón para tomar la foto
        imgFotoProducto.setOnClickListener(view -> tomarFoto());

        // Configurar el botón flotante para regresar a la lista de productos
        fabListaProductos.setOnClickListener(view -> abrirListaProductos());

    }

    private void tomarFoto() {
        // Abrir directamente la cámara sin verificar permisos
        abrirCamara();
    }

    private void abrirCamara() {
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoProducto = null;
        try {
            fotoProducto = crearImagenProducto();
            if (fotoProducto != null) {
                Uri uriFotoProducto;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uriFotoProducto = FileProvider.getUriForFile(MainActivity.this,
                            "com.example.miprimeraplicacion.fileprovider", fotoProducto);
                } else {
                    uriFotoProducto = Uri.fromFile(fotoProducto);
                }
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoProducto);
                if (tomarFotoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(tomarFotoIntent, 1);
                } else {
                    mostrarMsg("No se encontró una aplicación de cámara en el dispositivo.");
                }
            } else {
                mostrarMsg("No se pudo crear la imagen.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                imgFotoProducto.setImageURI(Uri.parse(urlCompletaFoto));
            } else {
                mostrarMsg("No se tomó la foto.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private File crearImagenProducto() throws Exception {
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_" + fechaHoraMs + "_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dirAlmacenamiento.exists()) {
            if (!dirAlmacenamiento.mkdirs()) {
                throw new Exception("No se pudo crear el directorio de almacenamiento.");
            }
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }

    private void guardarProducto() {
        // Obtener los valores de los campos
        String nombre = txtNombre.getText().toString();
        String marca = txtMarca.getText().toString();
        String codigo = txtCodigo.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String presentacion = txtPresentacion.getText().toString();
        String precio = txtPrecio.getText().toString();

        // Validar que todos los campos estén llenos
        if (nombre.isEmpty() || marca.isEmpty() || codigo.isEmpty() || descripcion.isEmpty() || presentacion.isEmpty() || precio.isEmpty()) {
            mostrarMsg("Todos los campos son obligatorios.");
            return;
        }

        // Validar que el precio sea un número válido
        double precioDouble;
        try {
            precioDouble = Double.parseDouble(precio);
        } catch (NumberFormatException e) {
            mostrarMsg("El precio debe ser un número válido.");
            return;
        }

        // Validar que se haya tomado una foto
        if (urlCompletaFoto.isEmpty()) {
            mostrarMsg("Debes tomar una foto del producto.");
            return;
        }

        // Guardar el producto en la base de datos
        String[] datos = {"", codigo, nombre, marca, descripcion, presentacion, String.valueOf(precioDouble), urlCompletaFoto};
        String resultado = db.administrar_productos("nuevo", datos);
        mostrarMsg(resultado);

        if (resultado.equals("ok")) {
            limpiarCampos();
            abrirListaProductos();
        }

        // Cerrar la conexión a la base de datos
        db.close();
    }

    private void abrirListaProductos() {
        Intent intent = new Intent(this, ListaProductosActivity.class);
        startActivity(intent);
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtMarca.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtPresentacion.setText("");
        txtPrecio.setText("");
        imgFotoProducto.setImageResource(R.mipmap.ic_launcher_round);
        urlCompletaFoto = "";
    }

    private void mostrarMsg(String msg) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
    }
}


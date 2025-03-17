package com.example.miprimeraplicacion;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaProductosActivity extends Activity {
    Bundle parametros = new Bundle();
    ListView ltsProductos;
    Cursor cProductos;
    DB db;
    final ArrayList<Producto> alProductos = new ArrayList<>();
    final ArrayList<Producto> alProductosCopia = new ArrayList<>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    Producto miProducto;
    FloatingActionButton fab;

    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos);
        db = new DB(this);
        parametros.putString("accion", "nuevo");
        fab = findViewById(R.id.fabAgregarProducto);
        fab.setOnClickListener(view -> abrirVentana());
        obtenerDatosProductos();
        buscarProductos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getString("descripcion"));
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            if (item.getItemId() == R.id.mnxNuevo) {
                abrirVentana();
            } else if (item.getItemId() == R.id.mnxModificar) {
                parametros.putString("accion", "modificar");
                parametros.putString("producto", jsonArray.getJSONObject(posicion).toString());
                abrirVentana();
            } else if (item.getItemId() == R.id.mnxEliminar) {
                eliminarProducto();
            }
            return true;
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            return super.onContextItemSelected(item);
        }
    }

    private void eliminarProducto() {
        try {
            String descripcion = jsonArray.getJSONObject(posicion).getString("descripcion");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("¿Está seguro de eliminar el producto?");
            confirmacion.setMessage(descripcion);
            confirmacion.setPositiveButton("Sí", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_productos("eliminar", new String[]{jsonArray.getJSONObject(posicion).getString("idProducto")});
                    if (respuesta.equals("ok")) {
                        obtenerDatosProductos();
                        mostrarMsg("Producto eliminado con éxito");
                    } else {
                        mostrarMsg("Error: " + respuesta);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            confirmacion.create().show();
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void abrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }

    private void obtenerDatosProductos() {
        try {
            Cursor cursor = db.lista_productos();
            if (cursor.moveToFirst()) {
                jsonArray = new JSONArray();
                do {
                    jsonObject = new JSONObject();
                    jsonObject.put("idProducto", cursor.getString(0)); // idProducto
                    jsonObject.put("codigo", cursor.getString(1));     // codigo
                    jsonObject.put("nombre", cursor.getString(2));     // nombre
                    jsonObject.put("marca", cursor.getString(3));      // marca
                    jsonObject.put("descripcion", cursor.getString(4));// descripcion
                    jsonObject.put("presentacion", cursor.getString(5)); // presentacion
                    jsonObject.put("precio", cursor.getDouble(6));     // precio
                    jsonObject.put("foto", cursor.getString(7));       // foto
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
                mostrarDatosProductos();
            } else {
                mostrarMsg("No hay productos registrados.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarDatosProductos() {
        try {
            if (jsonArray.length() > 0) {
                ltsProductos = findViewById(R.id.ltsProductos);
                alProductos.clear();
                alProductosCopia.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    Producto producto = new Producto(
                            jsonObject.getString("idProducto"),
                            jsonObject.getString("codigo"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("marca"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("presentacion"),
                            jsonObject.getDouble("precio"),
                            jsonObject.getString("foto")
                    );
                    alProductos.add(producto);
                }
                alProductosCopia.addAll(alProductos);
                ltsProductos.setAdapter(new AdaptadorProductos(this, alProductos));
                registerForContextMenu(ltsProductos);
            } else {
                mostrarMsg("No hay productos registrados.");
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void buscarProductos() {
        TextView tempVal = findViewById(R.id.txtBuscarProductos);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alProductos.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if (buscar.length() <= 0) {
                    alProductos.addAll(alProductosCopia);
                } else {
                    for (Producto item : alProductosCopia) {
                        if (item.getDescripcion().toLowerCase().contains(buscar) ||
                                item.getCodigo().toLowerCase().contains(buscar)) {
                            alProductos.add(item);
                        }
                    }
                    ltsProductos.setAdapter(new AdaptadorProductos(getApplicationContext(), alProductos));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
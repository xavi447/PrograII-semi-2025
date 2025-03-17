package com.example.miprimeraplicacion;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorProductos extends BaseAdapter {
    Context context;
    ArrayList<Producto> alProductos;
    Producto miProducto;
    LayoutInflater inflater;

    public AdaptadorProductos(Context context, ArrayList<Producto> alProductos) {
        this.context = context;
        this.alProductos = alProductos;
    }

    @Override
    public int getCount() {
        return alProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return alProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_producto, parent, false);
        try {
            miProducto = alProductos.get(position);

            // Asignar los datos del producto a los TextView
            TextView tempVal = itemView.findViewById(R.id.lblNombreProducto);
            tempVal.setText(miProducto.getNombre());

            tempVal = itemView.findViewById(R.id.lblMarcaProducto);
            tempVal.setText("Marca: " + miProducto.getMarca());

            tempVal = itemView.findViewById(R.id.lblCodigoProducto);
            tempVal.setText("Código: " + miProducto.getCodigo());

            tempVal = itemView.findViewById(R.id.lblDescripcionProducto);
            tempVal.setText("Descripción: " + miProducto.getDescripcion());

            tempVal = itemView.findViewById(R.id.lblPresentacionProducto);
            tempVal.setText("Presentación: " + miProducto.getPresentacion());

            tempVal = itemView.findViewById(R.id.lblPrecioProducto);
            tempVal.setText("Precio: $" + miProducto.getPrecio());

            // Cargar la imagen del producto
            ImageView img = itemView.findViewById(R.id.imgFotoProducto);
            if (miProducto.getFoto() != null && !miProducto.getFoto().isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(miProducto.getFoto());
                img.setImageBitmap(bitmap);
            } else {
                // Si no hay imagen, mostrar una imagen por defecto
                img.setImageResource(R.mipmap.ic_launcher_round);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}

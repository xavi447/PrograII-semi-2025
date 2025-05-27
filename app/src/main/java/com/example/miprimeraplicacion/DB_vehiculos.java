package com.example.miprimeraplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_vehiculos extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vehiculos.db";
    private static final int DATABASE_VERSION = 2;  // Cambiar versión para forzar upgrade

    public DB_vehiculos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE vehiculos (" +
                "tipo_placa TEXT," +
                "numero_placa TEXT PRIMARY KEY," +
                "codigo_ruta TEXT," +
                "placa_extranjera INTEGER," +
                "clase TEXT," +
                "marca TEXT," +
                "modelo TEXT," +
                "color TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);

        // Ejemplo de datos de prueba:
        ContentValues v1 = new ContentValues();
        v1.put("tipo_placa", "Particular");
        v1.put("numero_placa", "P1234");
        v1.putNull("codigo_ruta");
        v1.put("placa_extranjera", 0);
        v1.put("clase", "Automóvil");
        v1.put("marca", "Toyota");
        v1.put("modelo", "2020");
        v1.put("color", "Rojo");
        db.insert("vehiculos", null, v1);

        ContentValues v2 = new ContentValues();
        v2.put("tipo_placa", "Transporte");
        v2.put("numero_placa", "AB1234");
        v2.put("codigo_ruta", "Ruta 15");
        v2.put("placa_extranjera", 0);
        v2.put("clase", "Bus");
        v2.put("marca", "Mercedes");
        v2.put("modelo", "2018");
        v2.put("color", "Azul");
        db.insert("vehiculos", null, v2);

        ContentValues v3 = new ContentValues();
        v3.put("tipo_placa", "Particular");
        v3.put("numero_placa", "X9999");
        v3.putNull("codigo_ruta");
        v3.put("placa_extranjera", 1);
        v3.put("clase", "Automóvil");
        v3.put("marca", "Honda");
        v3.put("modelo", "2019");
        v3.put("color", "Blanco");
        db.insert("vehiculos", null, v3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para simplificar borramos tabla y la volvemos a crear
        db.execSQL("DROP TABLE IF EXISTS vehiculos");
        onCreate(db);
    }

    // Método para buscar por numero_placa
    public Cursor buscarPorPlaca(String numeroPlaca) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("vehiculos", null, "numero_placa = ?", new String[]{numeroPlaca}, null, null, null);
    }
}
package com.example.miprimeraplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_agentes extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agentes_pnc.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla de agentes
    private static final String TABLE_AGENTES = "agentes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USUARIO = "usuario";
    private static final String COLUMN_CONTRASENA = "contrasena";

    // SQL para crear la tabla
    private static final String CREATE_TABLE_AGENTES =
            "CREATE TABLE " + TABLE_AGENTES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_USUARIO + " TEXT UNIQUE NOT NULL," +
                    COLUMN_CONTRASENA + " TEXT NOT NULL" +
                    ")";

    public DB_agentes(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_AGENTES);


        insertarAgente(db, "javier", "javier123");
        insertarAgente(db, "walter", "walter123");
        insertarAgente(db, "yomi", "yomi123");
        insertarAgente(db, "steven", "steven123");
    }

    private void insertarAgente(SQLiteDatabase db, String usuario, String contrasena) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USUARIO, usuario);
        values.put(COLUMN_CONTRASENA, contrasena);
        db.insert(TABLE_AGENTES, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENTES);
        onCreate(db);
    }

    public boolean verificarAgente(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columnas = {COLUMN_ID};
        String seleccion = COLUMN_USUARIO + " = ? AND " + COLUMN_CONTRASENA + " = ?";
        String[] argsSeleccion = {usuario, contrasena};

        Cursor cursor = db.query(
                TABLE_AGENTES,
                columnas,
                seleccion,
                argsSeleccion,
                null, null, null);

        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return existe;
    }
}

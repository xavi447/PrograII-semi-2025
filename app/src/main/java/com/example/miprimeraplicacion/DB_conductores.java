package com.example.miprimeraplicacion;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_conductores extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "conductores.db";
    private static final int DATABASE_VERSION = 1;

    public DB_conductores(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE conductores (" +
                "licencia TEXT PRIMARY KEY," +
                "apellido1 TEXT," +
                "apellido2 TEXT," +
                "nombre1 TEXT," +
                "nombre2 TEXT," +
                "clase TEXT" +
                ")";
        db.execSQL(CREATE_TABLE);


        ContentValues values = new ContentValues();
        values.put("licencia", "A12345678");
        values.put("apellido1", "Gómez");
        values.put("apellido2", "Martínez");
        values.put("nombre1", "Carlos");
        values.put("nombre2", "Alberto");
        values.put("clase", "Particular");
        db.insert("conductores", null, values);

        ContentValues values2 = new ContentValues();
        values2.put("licencia", "B98765432");
        values2.put("apellido1", "Ramírez");
        values2.put("apellido2", "López");
        values2.put("nombre1", "Andrea");
        values2.put("nombre2", "Sofía");
        values2.put("clase", "Liviana");
        db.insert("conductores", null, values2);

        ContentValues values3 = new ContentValues();
        values3.put("licencia", "C24681357");
        values3.put("apellido1", "Torres");
        values3.put("apellido2", "Herrera");
        values3.put("nombre1", "Luis");
        values3.put("nombre2", "Miguel");
        values3.put("clase", "Motocicleta");
        db.insert("conductores", null, values3);

        ContentValues values4 = new ContentValues();
        values4.put("licencia", "D13579246");
        values4.put("apellido1", "Díaz");
        values4.put("apellido2", "Morales");
        values4.put("nombre1", "Mariana");
        values4.put("nombre2", "Isabel");
        values4.put("clase", "Juvenil");
        db.insert("conductores", null, values4);

        ContentValues values5 = new ContentValues();
        values5.put("licencia", "E11223344");
        values5.put("apellido1", "Castillo");
        values5.put("apellido2", "Méndez");
        values5.put("nombre1", "Kevin");
        values5.put("nombre2", "Josué");
        values5.put("clase", "Pesada-L");
        db.insert("conductores", null, values5);

        ContentValues values6 = new ContentValues();
        values6.put("licencia", "F55667788");
        values6.put("apellido1", "Chávez");
        values6.put("apellido2", "Rivas");
        values6.put("nombre1", "Paola");
        values6.put("nombre2", "Fernanda");
        values6.put("clase", "Extranjera");
        db.insert("conductores", null, values6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS conductores");
        onCreate(db);
    }

    public Cursor buscarPorLicencia(String licencia) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("conductores", null, "licencia = ?", new String[]{licencia}, null, null, null);
    }
}
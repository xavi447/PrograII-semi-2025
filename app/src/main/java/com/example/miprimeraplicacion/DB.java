package com.example.miprimeraplicacion;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tiendaonline.db";
    private static final int DATABASE_VERSION = 1;

    // Sentencia SQL para crear la tabla de productos
    private static final String SQL_CREATE_TABLE_PRODUCTOS =
            "CREATE TABLE productos (" +
                    "idProducto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codigo TEXT, " +
                    "nombre TEXT, " + // Agregar la columna "nombre"
                    "marca TEXT, " +
                    "descripcion TEXT, " +
                    "presentacion TEXT, " +
                    "precio REAL, " +
                    "urlFoto TEXT)"; // Usar "urlFoto" en lugar de "foto"

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla de productos
        db.execSQL(SQL_CREATE_TABLE_PRODUCTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla anterior si existe
        db.execSQL("DROP TABLE IF EXISTS productos");
        // Crear la tabla nuevamente
        onCreate(db);
    }

    /**
     * Método para administrar productos (insertar, modificar, eliminar).
     *
     * @param accion La acción a realizar: "nuevo", "modificar", "eliminar".
     * @param datos  Un arreglo de Strings con los datos del producto.
     * @return Un mensaje indicando el resultado de la operación.
     */
    public String administrar_productos(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String mensaje = "ok";
            String sql = "";

            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO productos (codigo, nombre, marca, descripcion, presentacion, precio, urlFoto) " +
                            "VALUES ('" + datos[1] + "', '" + datos[2] + "', '" + datos[4] + "', '" + datos[3] + "', '" + datos[5] + "', " + datos[6] + ", '" + datos[7] + "')";
                    break;
                case "modificar":
                    sql = "UPDATE productos SET " +
                            "codigo = '" + datos[1] + "', " +
                            "nombre = '" + datos[2] + "', " +
                            "marca = '" + datos[3] + "', " +
                            "descripcion = '" + datos[4] + "', " +
                            "presentacion = '" + datos[5] + "', " +
                            "precio = " + datos[6] + ", " +
                            "urlFoto = '" + datos[7] + "' " +
                            "WHERE idProducto = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM productos WHERE idProducto = " + datos[0];
                    break;
            }

            db.execSQL(sql);
            db.close();
            return mensaje;
        } catch (Exception e) {
            return e.getMessage(); // Devuelve el mensaje de error
        }
    }

    /**
     * Método para obtener la lista de productos.
     *
     * @return Un Cursor con los datos de los productos.
     */
    public Cursor lista_productos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT idProducto, codigo, nombre, marca, descripcion, presentacion, precio, urlFoto FROM productos", null);
    }

    /**
     * Método para buscar productos por código o descripción.
     *
     * @param query El texto de búsqueda.
     * @return Un Cursor con los resultados de la búsqueda.
     */
    public Cursor buscar_productos(String query) {
        SQLiteDatabase db = getReadableDatabase();

        // Se asegura que el query no esté vacío y se realiza la búsqueda en todos los campos
        if (query == null || query.trim().isEmpty()) {
            return db.rawQuery("SELECT * FROM productos", null); // Si no hay búsqueda, traer todos los productos
        }

        // Usar LIKE para buscar en todos los campos: código, nombre, marca, descripción, presentación, precio
        String sql = "SELECT * FROM productos WHERE codigo LIKE ? OR nombre LIKE ? OR marca LIKE ? OR descripcion LIKE ? OR presentacion LIKE ? OR precio LIKE ?";
        String[] selectionArgs = new String[]{
                "%" + query + "%",
                "%" + query + "%",
                "%" + query + "%",
                "%" + query + "%",
                "%" + query + "%",
                "%" + query + "%"
        };

        return db.rawQuery(sql, selectionArgs);
    }
}